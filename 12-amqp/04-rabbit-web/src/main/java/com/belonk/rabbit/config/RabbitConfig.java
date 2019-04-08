package com.belonk.rabbit.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Created by sun on 2019/4/2.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class RabbitConfig {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static Logger log = LoggerFactory.getLogger(RabbitConfig.class);

    public static final String HELLO_QUEUE = "web.sample.hello.queue";
    public static final String HELLO_MORE_QUEUE = "web.sample.hello.more.queue";
    public static final String JSON_QUEUE = "web.sample.json.queue";
    public static final String OBJECT_QUEUE = "web.sample.object.queue";
    public static final String TOPIC_LAZY_QUEUE = "web.sample.topic.lazy.queue";
    public static final String TOPIC_RABBIT_QUEUE = "web.sample.topic.rabbit.queue";
    public static final String DIRECT_QUEUE = "web.sample.direct.queue";
    public static final String FANOUT1_QUEUE = "web.sample.fanout1.queue";
    public static final String FANOUT2_QUEUE = "web.sample.fanout2.queue";

    public static final String TOPIC_EXCHANGE = "web.sample.topic.exchange";
    public static final String DIRECT_EXCHANGE = "web.sample.direct.exchange";
    public static final String FANOUT_EXCHANGE = "web.sample.fanout.exchange";

    // public static final String TOPIC_ROUTING_KEY = "";

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

    //~ Queue申明

    // 持久化hello queue

    @Bean
    public Queue helloQueue() {
        return new Queue(HELLO_QUEUE);
    }

    @Bean
    public Queue helloMoreQueue() {
        return new Queue(HELLO_MORE_QUEUE);
    }

    @Bean
    public Queue jsonQueue() {
        return new Queue(JSON_QUEUE);
    }

    // 直接存储对象队列

    @Bean
    public Queue objectQueue() {
        return new Queue(OBJECT_QUEUE);
    }

    // topic queue

    @Bean
    public Queue topicLazyQueue() {
        return new Queue(TOPIC_LAZY_QUEUE);
    }

    @Bean
    public Queue topicRabbitQueue() {
        return new Queue(TOPIC_RABBIT_QUEUE);
    }

    @Bean
    public Queue directQueue() {
        return new Queue(DIRECT_QUEUE);
    }

    // fanout queue

    @Bean
    public Queue fanout1Queue() {
        return new Queue(FANOUT1_QUEUE);
    }

    @Bean
    public Queue fanout2Queue() {
        return new Queue(FANOUT2_QUEUE);
    }

    //~ Exchange申明

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    //~ Exchange和Queue绑定申明

    // topicQueue和topicExchange绑定

    @Bean
    public Binding bindingLazyTopic(Queue topicLazyQueue, TopicExchange topicExchange) {
        // routingKey为lazy.#
        return BindingBuilder.bind(topicLazyQueue).to(topicExchange).with("lazy.#");
    }

    @Bean
    public Binding bindingRabbitTopic(Queue topicRabbitQueue, TopicExchange topicExchange) {
        // routingKey为*.*.rabbit
        return BindingBuilder.bind(topicRabbitQueue).to(topicExchange).with("*.*.rabbit");
    }

    // 两个fanout queue和fanoutExchange绑定

    @Bean
    public Binding bindingFanout1(Queue fanout1Queue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanout1Queue).to(fanoutExchange);
    }

    @Bean
    public Binding bindingFanout2(Queue fanout2Queue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanout2Queue).to(fanoutExchange);
    }

    @Bean
    // @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate jsonRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        // 申明json转换器，及其转换类型，默认为SimpleMessageConverter
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate callbackRabbitTemplate(ConnectionFactory connectionFactor) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactor);
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info("correlationData : " + correlationData);
                log.info("ack : " + ack);
                log.info("cause : " + cause);
            }
        });
        return rabbitTemplate;
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