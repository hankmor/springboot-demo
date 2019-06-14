package com.belonk.anno;

import com.belonk.config.RabbitConfiguration;
import com.belonk.util.Printer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by sun on 2019/6/14.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@Component
public class HelloWorldDemo {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void send(String msg) {
        rabbitTemplate.convertAndSend(RabbitConfiguration.ANONYMOUS_QUEUE_NAME, msg);
        Printer.p(this, "Send : " + msg);
    }

    @RabbitListener(queues = RabbitConfiguration.ANONYMOUS_QUEUE_NAME)
    public void receive(String msg) {
        Printer.p(this, "Received : " + msg);
    }
}