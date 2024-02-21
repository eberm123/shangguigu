package com.ebchinatech.kylincloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class KylinCloudDemo {
    public static void main(String[] args) {
        SpringApplication.run(KylinCloudDemo.class, args);
    }

}
