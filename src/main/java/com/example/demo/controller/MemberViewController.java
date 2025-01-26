package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberViewController {

	@GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String signup(){
        return "register";
    }
}
