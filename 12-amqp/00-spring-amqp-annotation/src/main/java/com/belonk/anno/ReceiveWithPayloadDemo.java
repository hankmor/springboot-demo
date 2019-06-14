package com.belonk.anno;

import com.belonk.config.RabbitConfiguration;
import com.belonk.domain.User;
import com.belonk.util.Printer;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
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
public class ReceiveWithPayloadDemo {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public void send(User msg) {
        rabbitTemplate.convertAndSend(RabbitConfiguration.ANONYMOUS_QUEUE_NAME_2, (Object) msg, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().getHeaders().put("custom_header", "this is a custom header.");
                return message;
            }
        });
        Printer.p(this, "Send : " + msg);
    }

    @RabbitListener(queues = RabbitConfiguration.ANONYMOUS_QUEUE_NAME_2)
    public void receive(@Payload User user, Channel channel, @Header("custom_header") String header) {
        Printer.p(this, "Received : " + user);
        Printer.p(this, "Header   : " + header);
        Printer.p(this, "Channel  : " + channel.getChannelNumber());
    }
}