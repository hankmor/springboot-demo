package com.belonk.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sun on 2019/3/20.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
@Component
public class JsonMessageDemo {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static Logger log = LoggerFactory.getLogger(JsonMessageDemo.class);

    private static final String INFERRED_FOO_QUEUE = "sample.inferred.foo";

    private static final String INFERRED_BAR_QUEUE = "sample.inferred.bar";

    public static final String RECEIVE_AND_CONVERT_QUEUE = "sample.receive.and.convert";

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitTemplate jsonRabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

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

    public void runDemo() throws Exception {
        String json = "{\"foo\" : \"value\" }";
        Message jsonMsg = MessageBuilder.withBody(json.getBytes(Charset.forName("utf-8")))
                .andProperties(MessagePropertiesBuilder.newInstance().setContentType("application/json").build())
                .build();

        //~ Send to inferred queue which listened by method of listenForAFoo and listenForABar using @RabbitListener

        this.rabbitTemplate.send(INFERRED_FOO_QUEUE, jsonMsg);
        this.rabbitTemplate.send(INFERRED_BAR_QUEUE, jsonMsg);

        //~ Send and receive using custom jsonRabbitTemplate

        json = "{\"foo\" : \"convertAndReceive\"}";
        jsonMsg = MessageBuilder.withBody(json.getBytes(Charset.forName("utf-8")))
                .andProperties(MessagePropertiesBuilder.newInstance().setContentType("application/json").build())
                .build();
        jsonMsg.getMessageProperties().setHeader(DefaultClassMapper.DEFAULT_CLASSID_FIELD_NAME, "foo");
        this.jsonRabbitTemplate.send(RECEIVE_AND_CONVERT_QUEUE, jsonMsg);
        // receive返回map
        Foo foo = (Foo) this.jsonRabbitTemplate.receiveAndConvert(RECEIVE_AND_CONVERT_QUEUE, 10_000);
        System.out.println("convertAndReceive : Expected a Foo, got a " + foo);
        // 如果Jackson2JsonMessageConverter不设置ClassMapper，直接使用objectMapper转换对象，底层也是将json转为对象（convertBytesToObject方法）
        this.jsonRabbitTemplate.send(RECEIVE_AND_CONVERT_QUEUE, jsonMsg);
        Bar bar = objectMapper.readValue(objectMapper.writeValueAsString(this.jsonRabbitTemplate.receiveAndConvert(RECEIVE_AND_CONVERT_QUEUE, 10_000)), Bar.class);
        System.out.println("convertAndReceive : Expected a Bar, got a " + bar);

        //~ global exception handler

        // To mock some bad json message, when sending a ListenerExecutionFailedException will be thrown.
        this.rabbitTemplate.convertAndSend(INFERRED_FOO_QUEUE, new Foo("error json foo"),
                message -> new Message("bad json".getBytes(), message.getMessageProperties()));
    }

    // RabbitTemplate with default message converter SimpleMessageConverter

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    // RabbitTemplate with Jackson2JsonMessageConverter

    @Bean
    public RabbitTemplate jsonRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        // 申明json转换器，及其转换类型，默认为SimpleMessageConverter
        Jackson2JsonMessageConverter messageConverter = (Jackson2JsonMessageConverter) messageConverter();
        messageConverter.setClassMapper(classMapper());
        template.setMessageConverter(messageConverter);
        return template;
    }

    //~ RabbitListenerContainerFactory

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            SimpleRabbitListenerContainerFactoryConfigurer containerFactoryConfigurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        containerFactoryConfigurer.configure(factory, connectionFactory);
        factory.setErrorHandler(errorHandler());
        return factory;
    }

    @Bean
    public ErrorHandler errorHandler() {
        return new ConditionalRejectingErrorHandler(new CustomFatalExceptionStrategy());
    }

    /**
     * Custom fatal exception strategy
     */
    public class CustomFatalExceptionStrategy extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {
        @Override
        public boolean isFatal(Throwable t) {
            if (t instanceof ListenerExecutionFailedException) {
                ListenerExecutionFailedException exception = (ListenerExecutionFailedException) t;
                logger.error("Failed to process inbound message from queue "
                        + exception.getFailedMessage().getMessageProperties().getConsumerQueue()
                        + "; failed message : " + exception.getFailedMessage(), t);
            }
            return super.isFatal(t);
        }
    }

    // @Bean
    // public SimpleMessageListenerContainer legacyPojoListener(ConnectionFactory connectionFactory) {
    //     SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
    //     container.setQueueNames(MAPPED_QUEUE);
    //     MessageListenerAdapter messageListener = new MessageListenerAdapter(new Object() {
    //
    //         @SuppressWarnings("unused")
    //         public void handleMessage(Object object) {
    //             System.out.println("Got a " + object);
    //         }
    //
    //     });
    //     Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
    //     jsonConverter.setClassMapper(classMapper());
    //     messageListener.setMessageConverter(jsonConverter);
    //     container.setMessageListener(messageListener);
    //     return container;
    // }

    /**
     * Create instance of {@link Jackson2JsonMessageConverter}，replace of default
     * {@link org.springframework.amqp.support.converter.SimpleMessageConverter}.
     *
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("foo", Foo.class);
        idClassMapping.put("bar", Bar.class);
        classMapper.setIdClassMapping(idClassMapping);
        return classMapper;
    }

    @RabbitListener(queues = INFERRED_FOO_QUEUE)
    public void listenForAFoo(Foo foo) {
        System.out.println("listenForAFoo: Expected a Foo, got a " + foo);
    }

    @RabbitListener(queues = INFERRED_BAR_QUEUE)
    public void listenForABar(Bar bar) {
        System.out.println("listenForABar : Expected a Bar, got a " + bar);
    }

    @Bean
    public Queue inferredFoo() {
        return new AnonymousQueue(() -> INFERRED_FOO_QUEUE);
    }

    @Bean
    public Queue inferredBar() {
        return new AnonymousQueue(() -> INFERRED_BAR_QUEUE);
    }

    @Bean
    public Queue convertAndReceive() {
        return new Queue(RECEIVE_AND_CONVERT_QUEUE);
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

    public static class Foo {

        private String foo;

        public Foo() {
            super();
        }

        public Foo(String foo) {
            this.foo = foo;
        }

        public String getFoo() {
            return this.foo;
        }

        public void setFoo(String foo) {
            this.foo = foo;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + " [foo=" + this.foo + "]";
        }

    }

    public static class Bar extends Foo {

        public Bar() {
            super();
        }

        public Bar(String foo) {
            super(foo);
        }

    }
}