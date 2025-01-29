package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {
	private String memberId;
    private String memberPasswd;
    private String memberName;
    private String memberTel;
    private String memberEmail;
}
