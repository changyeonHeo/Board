package com.example.demo.controller;

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
    public String signup(AddUserRequest request) {
        memberService.save(request); // 회원 가입 메소드 호출
        return "redirect:/login"; // 회원 가입이 완료된 후 로그인 페이지로 이동
    }

    @PostMapping("/validate-field")
    @ResponseBody
    public Map<String, Object> validateField(@RequestBody Map<String, String> request) {
        String fieldName = request.get("fieldName");
        String fieldValue = request.get("fieldValue");
        Map<String, Object> response = new HashMap<>();

        switch (fieldName) {
            case "name":
                if (!validateName(fieldValue)) {
                    response.put("status", "error");
                    response.put("message", "이름은 한글 또는 영문으로 2자 이상 입력해주세요.");
                } else {
                    response.put("status", "success");
                    response.put("message", "이름이 유효합니다.");
                }
                break;

            case "password":
                if (!validatePassword(fieldValue)) {
                    response.put("status", "error");
                    response.put("message", "비밀번호는 영어, 숫자, 특수문자를 포함해 10자 이상이어야 합니다.");
                } else {
                    response.put("status", "success");
                    response.put("message", "비밀번호가 유효합니다.");
                }
                break;

            case "confirmPassword":
                String password = request.get("password");
                if (!fieldValue.equals(password)) {
                    response.put("status", "error");
                    response.put("message", "비밀번호가 일치하지 않습니다.");
                } else {
                    response.put("status", "success");
                    response.put("message", "비밀번호가 일치합니다.");
                }
                break;

            case "email":
                response = validateEmail(fieldValue);
                break;

            case "phone":
                if (!validatePhone(fieldValue)) {
                    response.put("status", "error");
                    response.put("message", "전화번호는 10~11자리 숫자로 입력해주세요.");
                } else {
                    response.put("status", "success");
                    response.put("message", "전화번호가 유효합니다.");
                }
                break;

            default:
                response.put("status", "error");
                response.put("message", "유효하지 않은 필드입니다.");
                break;
        }

        return response;
    }

    @GetMapping("/api/validateEmail")
    @ResponseBody
    public Map<String, Object> validateEmail(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();

        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            response.put("status", "ERROR");
            response.put("message", "유효한 이메일 주소를 입력해 주세요.");
        } else if (memberService.isEmailExist(email)) {
            response.put("status", "EXIST");
            response.put("message", "이미 존재하는 이메일입니다.");
        } else {
            response.put("status", "SUCCESS");
            response.put("message", "사용 가능한 이메일입니다.");
        }

        return response;
    }


    // 이름 검증
    private boolean validateName(String name) {
        return name.matches("^[가-힣a-zA-Z]{2,}$");
    }

    // 비밀번호 검증
    private boolean validatePassword(String password) {
        return password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{10,}$");
    }

    // 이메일 포맷 검증
    private boolean validateEmailFormat(String email) {
        return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    // 전화번호 검증
    private boolean validatePhone(String phone) {
        return phone.matches("^\\d{10,11}$");
    }
}
