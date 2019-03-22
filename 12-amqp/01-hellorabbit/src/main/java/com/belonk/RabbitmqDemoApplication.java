package com.belonk;

import com.belonk.rabbit.SimpleMessageDemo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RabbitmqDemoApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext ctx = SpringApplication.run(RabbitmqDemoApplication.class, args);

        // simple message demo

        SimpleMessageDemo simpleMessageDemo = ctx.getBean(SimpleMessageDemo.class);
        simpleMessageDemo.send("张三");
        simpleMessageDemo.send("lisi");

        // ctx.close();
    }
}
