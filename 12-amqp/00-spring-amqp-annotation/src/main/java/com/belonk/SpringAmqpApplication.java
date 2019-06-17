package com.belonk;

import com.belonk.anno.*;
import com.belonk.domain.Dept;
import com.belonk.domain.User;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

@SpringBootApplication
@EnableRabbit
public class SpringAmqpApplication {

    public static void main(String[] args) throws UnsupportedEncodingException {
        ApplicationContext context = SpringApplication.run(SpringAmqpApplication.class, args);


        /*
        // demo1
        HelloWorldDemo helloWorldDemo = context.getBean(HelloWorldDemo.class);
        helloWorldDemo.send("hello world!");
        helloWorldDemo.send("hi, belonk!");
        helloWorldDemo.send("张三");
        // demo2
        ReceiveWithHeaderDemo receiveWithHeaderDemo = context.getBean(ReceiveWithHeaderDemo.class);
        receiveWithHeaderDemo.send("receive with arguments.");
        // demo3
        ReceiveWithPayloadDemo receiveWithPayloadDemo = context.getBean(ReceiveWithPayloadDemo.class);
        receiveWithPayloadDemo.send(new User("belonk"));
        // demo4
        MultiHandlerDemo multiHandlerDemo = context.getBean(MultiHandlerDemo.class);
        multiHandlerDemo.runDemo("send a string.");
        multiHandlerDemo.runDemo(new ArrayList<>());
        multiHandlerDemo.runDemo(new User("李四"));
        // 错误，无法（反）序列化，必须实现Serializable接口
        // multiHandlerDemo.runDemo(new Object());
        // demo5
        ConfirmAndReturnDemo confirmAndReturnDemo = context.getBean(ConfirmAndReturnDemo.class);
        confirmAndReturnDemo.send(new User("王五"));
        */

        // demo6，运行时需要打开RabbitConfiguration的RabbitListenerContainerFactory，使其为消息监听设置JSON转换器
        JsonMessageDemo jsonMessageDemo = context.getBean(JsonMessageDemo.class);
        User user = new User("赵六");
        jsonMessageDemo.send(user);
        jsonMessageDemo.send(new Dept("技术部", user));
        jsonMessageDemo.send("this is not json");
    }
}
