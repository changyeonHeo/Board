package com.example.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@Controller
public class MainController {
	@GetMapping("/")
	public String hello() {
		return "/main";
	}
}
