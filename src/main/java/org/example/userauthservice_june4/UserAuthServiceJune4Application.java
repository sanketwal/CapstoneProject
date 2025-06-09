package org.example.userauthservice_june4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//@EnableJpaAuditing
public class UserAuthServiceJune4Application {

    public static void main(String[] args) {
        SpringApplication.run(UserAuthServiceJune4Application.class, args);
    }

}
