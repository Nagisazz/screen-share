package com.nagisazz.screenshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @auther zhushengzhe
 * @date 2022/07/04 23:44
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableScheduling
public class ScreenShareApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScreenShareApplication.class, args);
    }
}
