package com.belonk.anno;

import com.belonk.config.RabbitConfiguration;
import com.belonk.domain.User;
import com.belonk.util.Printer;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
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
public class ConfirmAndReturnDemo {
    @Resource
    private RabbitTemplate callbackRabbitTemplate;

    private Sender sender = new Sender();

    public void send(User user) {
        sender.send(user);
        Printer.p("Send : " + user);
    }

    @Component
    public class Sender {
        public void send(User msg) {
            callbackRabbitTemplate.convertAndSend(RabbitConfiguration.ANONYMOUS_QUEUE_NAME_4, (Object) msg, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    message.getMessageProperties().getHeaders().put("custom_header", "this is a custom header.");
                    return message;
                }
            });
            Printer.p(this, "Send : " + msg);
        }

        @RabbitListener(bindings = {
                @QueueBinding(
                        value = @Queue(value = RabbitConfiguration.ANONYMOUS_QUEUE_NAME_4, durable = "false", autoDelete = "true", exclusive = "true"),
                        exchange = @Exchange("exist.exchange"), key = "exist.routingKey"
                )
        })
        public void receiveReply(User user) {
            Printer.p(this, "Received reply : " + user);
        }
    }

    @Component
    class Consumer {
        @RabbitListener(queues = RabbitConfiguration.ANONYMOUS_QUEUE_NAME_4)
        // 回复到默认的队列
        // @SendTo
        // 回复到不存在的exchange和routingkey
        // @SendTo("dontExist.exchange/dontExist.routingKey")
        // 回复到存在的exchange和routingkey
        @SendTo("exist.exchange/exist.routingKey")
        // 回复到默认的exchange和不存在的routingkey
        // @SendTo("dontExist.routingKey")
        public User receive(@Payload User user) {
            Printer.p(this, "Received : " + user);
            user.setName("王五改名字了");
            return user;
        }
    }
}