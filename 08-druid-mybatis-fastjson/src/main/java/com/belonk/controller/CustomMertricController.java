package com.belonk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.boot.actuate.metrics.buffer.BufferCounterService;
import org.springframework.boot.actuate.metrics.buffer.BufferGaugeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 自定义度量指标。
 * <p>
 * Created by sun on 2018/5/9.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/custom")
public class CustomMertricController {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static Logger log = LoggerFactory.getLogger(CustomMertricController.class);

    private volatile static int npeNumber;
    private volatile static int iaeNumber;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    // 无法注入DefaultXxx实现，低于Java8
    @Resource(type = BufferGaugeService.class)
    private GaugeService gaugeService;
    @Resource(type = BufferCounterService.class)
    private CounterService counterService;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constructors
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Public Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * /metrics：
     * 增加指标 "counter.visitNumber":7
     * 增加指标 "gauge.illegalArgumentExceptionNumber":2
     * 增加指标 "counter.nullPointerException":2
     *
     * @return
     */
    @GetMapping("/{id}")
    public String customMertric(@PathVariable Long id) {
        try {
            if (id == null) {
                throw new NullPointerException("Id must not be null.");
            }
            if (id > 100 || id <= 0) {
                throw new IllegalArgumentException("Id must be 1..100");
            }
            String mertricName = "visitNumber";
            counterService.increment(mertricName);
        } catch (NullPointerException e) {
            log.error("Exception : ", e);
            gaugeService.submit("nullPointerExceptionNumber", ++npeNumber);
        } catch (IllegalArgumentException e) {
            log.error("Exception : ", e);
            String mertricName = "exceptionNumber";
            gaugeService.submit("illegalArgumentExceptionNumber", ++iaeNumber);
        }
        return "hello";
    }
    
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Private Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */


}