package com.belonk;

import com.belonk.springamqp.domain.User;
import com.belonk.springamqp.helloworld.AmqpDemo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;

@SpringBootApplication
public class SpringAmqpApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(SpringAmqpApplication.class, args);

        // 运行demo

        AmqpDemo amqpDemo = ctx.getBean(AmqpDemo.class);
        amqpDemo.runDemo("this is a foo.");
        amqpDemo.runDemo(new ArrayList<>());
        // 异常，没有对应的转换器，无法接收
        // amqpDemo.runDemo(new User());

        // ctx.close();
    }
}
