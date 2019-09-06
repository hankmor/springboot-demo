package com.belonk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebappViewStarter {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(WebappViewStarter.class, args);

        Logger logger = LoggerFactory.getLogger(WebappViewStarter.class);
        logger.debug("this is debug log.");
        logger.info("this is info log.");
        logger.warn("this is warn log.");
        logger.error("this is error log.");
    }
}