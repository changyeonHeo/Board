package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // ✅ JPA Auditing 활성화
public class PersonalBoardApplication {
    public static void main(String[] args) {
        SpringApplication.run(PersonalBoardApplication.class, args);
    }
}
