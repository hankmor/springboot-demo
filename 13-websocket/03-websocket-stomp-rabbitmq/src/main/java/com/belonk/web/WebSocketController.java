package com.belonk.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by sun on 2019/10/28.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@Controller
public class WebSocketController {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static Logger log = LoggerFactory.getLogger(WebSocketController.class);

    public static final String EXCHANGE_TOPIC_NAME = "ws.rabbit.exchange.topic";
    public static final String QUEUE_NAME = "ws.rabbit.queue.destination";
    public static final String AMQ_QUEUE_NAME = "ws.rabbit.amq.queue.destination";

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    @Resource
    private RabbitTemplate rabbitTemplate;

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

    // 直接发消息到mq

    @RequestMapping("/send2mq")
    @ResponseBody
    public void send(String routingkey, String content, int type) {
        switch (type) {
            // /exchange
            case 1:
                rabbitTemplate.convertAndSend(EXCHANGE_TOPIC_NAME, routingkey, content);
                break;
            // /queue
            case 2:
                rabbitTemplate.convertAndSend("", QUEUE_NAME, content);
                break;
            // /amq/queue
            case 3:
                rabbitTemplate.convertAndSend("", AMQ_QUEUE_NAME, content);
                break;
            // /topic
            case 4:
                // rabbitTemplate.convertAndSend(QUEUE_NAME, routingkey, content);
                break;
            default:
        }
    }

    // ~ /exchange测试

    //先在mq创建一个topic类型的ws.rabbit.exchange.topic的exchange

    @MessageMapping("/send2mifei")
    @SendTo("/exchange/" + EXCHANGE_TOPIC_NAME + "/animal.rabbit.mifei")
    public String exchange1(String content) {
        return "destination : " + "/exchange/" + EXCHANGE_TOPIC_NAME + "/animal.rabbit.mifei" + ", content : " + content;
    }

    @MessageMapping("/send2peppa")
    @SendTo("/exchange/" + EXCHANGE_TOPIC_NAME + "/animal.pig")
    public String exchange2(String content) {
        return "destination : " + "/exchange/" + EXCHANGE_TOPIC_NAME + "/animal.pig" + ", content : " + content;
    }

    // ~ /queue测试

    @MessageMapping("/queue")
    @SendTo("/queue/" + QUEUE_NAME)
    public String queue(String content) {
        return "destination : " + "/queue/" + QUEUE_NAME + ", content : " + content;
    }

    // ~ /amq/queue测试

    // 先在mq中创建一个ws.rabbit.amq.queue.destination队列

    @MessageMapping("/amq/queue")
    @SendTo("/amq/queue/" + AMQ_QUEUE_NAME)
    public String amqQueue(String content) {
        return "destination : " + "/amq/queue/" + AMQ_QUEUE_NAME + ", content : " + content;
    }

    // ~ /topic测试

    @MessageMapping("/topic/debug")
    @SendTo("/topic/" + "*.debug.*")
    public String topicTest1(String content) {
        return "destination : " + "/topic *.debug.*, content : " + content;
    }

    @MessageMapping("/topic/info")
    @SendTo("/topic/" + "*.info.*")
    public String topicTest2(String content) {
        return "destination : " + "/topic *.info.*, content : " + content;
    }

    //todo ~ /temp-queue/测试

    @MessageExceptionHandler
    @SendTo("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Private Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */


}