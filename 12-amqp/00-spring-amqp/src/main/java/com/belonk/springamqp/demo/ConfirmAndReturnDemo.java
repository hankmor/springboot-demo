package com.belonk.springamqp.demo;

import com.belonk.springamqp.domain.User;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;

import java.util.UUID;

/**
 * Created by sun on 2019/6/5.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class ConfirmAndReturnDemo {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



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

    public static void main(String[] args) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("192.168.0.27", 5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123456");

        // 开启消息确认和回调
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);

        Queue queue = new AnonymousQueue();
        AmqpAdmin admin = new RabbitAdmin(connectionFactory);
        admin.declareQueue(queue);

        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        // 设置为true，则消息改为手动返回
        template.setMandatory(true);
        // 设置消息返回回调，一个RabbitTemplate只能设置一次返回回调
        // 当消息不能成功投递，会抛出AmqpMessageReturnedException，该异常包含ReturnCallback所需的参数信息，此时会执行回调
        template.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            System.err.println("Return callback : ");
            System.err.println("    message : " + message);
            System.err.println("    reply   : " + replyCode + ", " + replyText);
        });
        // 消息确认回调，一个RabbitTemplate只能设置一次确认回调
        template.setConfirmCallback((correlationData, ack, cause) -> {
            System.err.println("Confirm callback : ");
            System.err.println("    correlationData : " + correlationData);
            System.err.println("    ack   : " + ack);
            System.err.println("    cause : " + cause);
        });

        String str = "this is foo.";
        template.convertAndSend(queue.getName(), (Object) str, new CorrelationData(UUID.randomUUID().toString()));
        System.err.println("Send : " + str);
        String foo = (String) template.receiveAndConvert(queue.getName());
        System.err.println("Received : " + foo);

        User user = new User("zhangsan");
        template.convertAndSend(queue.getName(), user, new CorrelationData(UUID.randomUUID().toString()));
        System.err.println("Send : " + user);
        User receivedUser = (User) template.receiveAndConvert(queue.getName());
        System.err.println("Received : " + receivedUser);

        // 不能成功投递消息
        System.err.println("Mock message body can't be sent.");
        str = "can not be sent.";
        // routing key无法匹配，则confirmCallback和returnCallback都会被触发，ack仍然为true
        template.convertAndSend("dontExist.exchange", (Object) str, new CorrelationData(UUID.randomUUID().toString()));
        // exchange找不到，则会触发confirmCallback，但是returnCallback不会被触发，ack为false，cause包含异常信息，此时channel会关闭
        template.convertAndSend("dontExist.exchange", "dontExist.routingKey", str, new CorrelationData(UUID.randomUUID().toString()));
    }


    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Protected Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



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