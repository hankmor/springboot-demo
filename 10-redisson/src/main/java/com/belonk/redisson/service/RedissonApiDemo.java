package com.belonk.redisson.service;

import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by sun on 2018/12/5.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class RedissonApiDemo {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static Logger log = LoggerFactory.getLogger(RedissonApiDemo.class);

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static RedissonClient client;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constructors
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private RedissonApiDemo() throws IOException {
        client = getClient();
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Public Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public static void main(String[] args) throws Exception {
        RedissonApiDemo demo = new RedissonApiDemo();

        // demo.atomicLong();
        // demo.lock();
        // demo.keyTest();
        // demo.bucketTest();
        // demo.streamTest();
        demo.geoTest();

        client.shutdown();
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Protected Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private RedissonClient getClient() throws IOException {
        Config config = null;
        config = Config.fromYAML(new InputStreamReader(ClassLoader.getSystemResourceAsStream("redisson.yaml"), StandardCharsets.UTF_8));
        return Redisson.create(config);
    }

    private void atomicLong() throws ExecutionException, InterruptedException {
        long v;
        RAtomicLong atomicLong = client.getAtomicLong("test-long");
        String name = atomicLong.getName();
        log.error("name : " + name);
        atomicLong.set(0);

        atomicLong.compareAndSet(0, 10);
        v = atomicLong.get();
        log.error("compareAndSet : " + v);

        atomicLong.compareAndSetAsync(10, 20);
        v = atomicLong.get();
        log.error("compareAndSetAsync : " + v);

        v = atomicLong.incrementAndGet();
        log.error("incrementAndGet : " + v);

        RFuture<Long> future = atomicLong.incrementAndGetAsync();
        v = future.get();
        log.error("incrementAndGetAsync : " + v);
    }

    /**
     * 每个Redisson对象实例都会有一个与之对应的Redis数据实例，可以通过调用getName方法来取得Redis数据实例的名称（key）,
     * 所有与Redis key相关的操作都归纳在RKeys这个接口里
     */
    private void keyTest() {
        RBucket<Object> objectBucket = client.getBucket("objectBucket");
        objectBucket.set("string");
        objectBucket.set(1.00d);
        objectBucket.set(1L);
        Object obj = objectBucket.get();
        log.error("obj : " + obj);
        RBucket<String> stringBucket = client.getBucket("stringBucket");
        stringBucket.set("string");
        stringBucket.set("bucket");
        String str = stringBucket.get();
        log.error("str : " + str);

        RKeys keys = client.getKeys();
        Iterable<String> allKeys = keys.getKeys();
        printKey(allKeys);

        Iterable<String> patternKeys = keys.getKeysByPattern("*Bucket");
        printKey(patternKeys);
    }

    /**
     * Redisson的分布式RBucketJava对象是一种通用对象桶可以用来存放任类型的对象。 除了同步接口外，还提供了异步（Async）、
     * 反射式（Reactive）和RxJava2标准的接口
     */
    private void bucketTest() {
        RBuckets buckets = client.getBuckets();
        Map<String, Object> loadedBuckets = buckets.get("objectBucket", "stringBucket");
        for (String s : loadedBuckets.keySet()) {
            log.error("bucket key : " + s);
            log.error("bucket value : " + loadedBuckets.get(s));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("objectBucket", Math.PI);
        map.put("stringBucket", "a string");

        // 利用Redis的事务特性，同时保存所有的通用对象桶，如果任意一个通用对象桶已经存在则放弃保存其他所有数据。
        buckets.trySet(map);
        // 同时保存全部通用对象桶。
        buckets.set(map);

        // 通过RBuckets接口实现批量操作多个RBucket对象
        RBucket<Object> objectBucket = client.getBucket("objectBucket");
        Object obj = objectBucket.get();
        log.error("obj : " + obj);
        log.error("objectBucket size : " + objectBucket.size());
        RBucket<String> stringBucket = client.getBucket("stringBucket");
        String str = stringBucket.get();
        log.error("str : " + str);
        log.error("stringBucket size : " + stringBucket.size());
    }

    private void printKey(Iterable<String> keys) {
        int cnt = 0;
        for (String key : keys) {
            log.error("key : " + key);
            cnt++;
        }
        log.error("keys count : " + cnt);
    }

    private void lock() {
        RLock lock = client.getLock("myLock");
        log.error("lock : " + lock);
        lock.tryLock();
        try {
            log.error("To do something.");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            log.error("Unlock ...");
            lock.unlock();
        }
    }

    /**
     * Redisson的分布式RBinaryStream Java对象同时提供了InputStream接口和OutputStream接口的实现。
     * 流的最大容量受Redis主节点的内存大小限制。
     *
     * @throws IOException
     */
    private void streamTest() throws IOException {
        RBinaryStream binaryStream = client.getBinaryStream("anyStream");
        String content = "Redisson的分布式RBinaryStream Java对象同时提供了InputStream接口和OutputStream接口的实现。流的最大容量受Redis主节点的内存大小限制。";
        binaryStream.set(content.getBytes(StandardCharsets.UTF_8));

        byte[] bytes = new byte[1024];
        InputStream inputStream = binaryStream.getInputStream();
        inputStream.read(bytes);
        log.error("string : " + new String(bytes, StandardCharsets.UTF_8));

        // 向后增加
        OutputStream outputStream = binaryStream.getOutputStream();
        bytes = "change string".getBytes();
        outputStream.write(bytes);
        bytes = binaryStream.get();
        log.error("string : " + new String(bytes, StandardCharsets.UTF_8));
    }

    /**
     * edisson的分布式RGeo Java对象是一种专门用来储存与地理位置有关的对象桶。除了同步接口外，还提供了异步（Async）、
     * 反射式（Reactive）和RxJava2标准的接口。
     * <p>
     * redis 3.2.0版本才开始支持GEOADD命令。
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private void geoTest() throws ExecutionException, InterruptedException {
        RGeo<String> geo = client.getGeo("geoTest");
        geo.add(new GeoEntry(13.361389, 38.115556, "Palermo"),
                new GeoEntry(15.087269, 37.502669, "Catania"));
        geo.addAsync(37.618423, 55.751244, "Moscow");

        // 计算距离
        Double distance = geo.dist("Palermo", "Catania", GeoUnit.METERS);
        log.error("Distance from Palermo to Catania : " + distance + " m");

        // 计算距离的GEO HASH值
        RFuture<Map<String, String>> fu = geo.hashAsync("Palermo", "Catania");
        Map<String, String> map = fu.get();
        for (String s : map.keySet()) {
            log.error("hashAsync key : " + s);
            log.error("hashAsync value : " + map.get(s));
        }

        Map<String, GeoPosition> positions = geo.pos("test2", "Palermo", "test3", "Catania", "test1");
        for (String s : positions.keySet()) {
            log.error("pos key : " + s);
            log.error("pos value : " + map.get(s));
        }

        // 计算中心点为圆心、距离为半径的圆形范围内的所有点
        List<String> cities = geo.radius(15, 37, 200, GeoUnit.KILOMETERS);
        log.error("cities : " + cities);

        // 查询范围内所有的点并返回GEO HASH值
        Map<String, GeoPosition> citiesWithPositions = geo.radiusWithPosition(15, 37, 200, GeoUnit.KILOMETERS);
        for (String s : citiesWithPositions.keySet()) {
            log.error("radiusWithPosition key : " + s);
            log.error("radiusWithPosition value : " + map.get(s));
        }
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Property accessors
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Inner classes
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */


}