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
    public ResponseEntity<Map<String, String>> signup(@RequestBody AddUserRequest request) {
        Map<String, String> response = new HashMap<>();
        
        try {
            if (memberService.isIdExist(request.getMemberId())) {
                response.put("message", "이미 존재하는 아이디입니다.");
                return ResponseEntity.status(409).body(response);  // 409 Conflict
            }
            
            if (memberService.isEmailExist(request.getMemberEmail())) {
                response.put("message", "이미 존재하는 이메일입니다.");
                return ResponseEntity.status(409).body(response);  // 409 Conflict
            }
            
            memberService.save(request);  // 회원가입 수행
            response.put("message", "회원가입이 완료되었습니다.");
            return ResponseEntity.ok(response);  // 200 OK
        } catch (Exception e) {
            response.put("message", "회원가입 중 오류가 발생했습니다.");
            return ResponseEntity.status(500).body(response);  // 500 Internal Server Error
        }
    }


    // ✅ 아이디 중복 확인 API
    @GetMapping("/api/validateId")
    public ResponseEntity<Map<String, String>> validateId(@RequestParam String memberId) {
        Map<String, String> response = new HashMap<>();

        if (memberService.isIdExist(memberId)) {
            response.put("status", "EXIST");
            response.put("message", "이미 존재하는 아이디입니다.");
            return ResponseEntity.ok(response);  // ✅ 409 대신 200 OK로 변경했는지 확인!!
        }

        response.put("status", "AVAILABLE");
        response.put("message", "사용 가능한 아이디입니다.");
        return ResponseEntity.ok(response);
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