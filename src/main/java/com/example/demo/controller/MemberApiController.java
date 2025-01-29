package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.AddUserRequest;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String signup() {
        return "register";
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody AddUserRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean isRegistered = memberService.save(request);
            if (isRegistered) {
                response.put("status", "success");
                response.put("message", "회원가입이 완료되었습니다.");
                return ResponseEntity.ok(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        response.put("status", "error");
        response.put("message", "회원가입 중 오류가 발생했습니다.");
        return ResponseEntity.badRequest().body(response);
    }

    // ✅ 아이디 중복 확인 API
    @GetMapping("/api/validateId")
    @ResponseBody
    public Map<String, String> validateId(@RequestParam String memberId) {
        Map<String, String> response = new HashMap<>();
        
        if (memberService.isIdExist(memberId)) {
            response.put("status", "EXIST");  // 이미 존재하는 아이디
            response.put("message", "이미 존재하는 아이디입니다.");
        } else {
            response.put("status", "AVAILABLE");  // 사용 가능한 아이디
            response.put("message", "사용 가능한 아이디입니다.");
        }
        
        return response;
    }

    // ✅ 이메일 중복 확인 API
    @GetMapping("/api/validateEmail")
    @ResponseBody
    public Map<String, String> validateEmail(@RequestParam String email) {
        Map<String, String> response = new HashMap<>();
        
        if (memberService.isEmailExist(email)) {
            response.put("status", "EXIST");
            response.put("message", "이미 존재하는 이메일입니다.");
        } else {
            response.put("status", "AVAILABLE");
            response.put("message", "사용 가능한 이메일입니다.");
        }
        
        return response;
    }

    // ✅ 전화번호 중복 확인 API
    @GetMapping("/api/validatePhone")
    @ResponseBody
    public Map<String, String> validatePhone(@RequestParam String phone) {
        Map<String, String> response = new HashMap<>();

        if (memberService.isPhoneExist(phone)) {
            response.put("status", "EXIST");
            response.put("message", "이미 존재하는 전화번호입니다.");
        } else {
            response.put("status", "AVAILABLE");
            response.put("message", "사용 가능한 전화번호입니다.");
        }
        
        return response;
    }
}
