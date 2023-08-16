package com.trojan.chat;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class ChatgptRobotBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatgptRobotBackApplication.class, args);
    }

}
