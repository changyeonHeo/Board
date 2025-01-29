<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>로그인</title>
    <link rel="stylesheet" type="text/css" href="/css/login.css">
</head>
<body>
    <div class="login-container">
        <h1 class="login-title">로그인</h1>
        <form action="/doLogin" method="post">
            <div class="input-group">
                <label for="memberId">아이디</label>
                <input type="text" id="memberId" name="memberId" placeholder="아이디를 입력하세요">
            </div>
            <div class="input-group">
                <label for="memberPasswd">비밀번호</label>
                <input type="password" id="memberPasswd" name="memberPasswd" placeholder="비밀번호를 입력하세요">
            </div>
            <button type="submit" class="btn login-btn">로그인</button>
        </form>
        <div class="options">
            <a href="/register" class="btn signup-btn">회원가입</a>
            <div class="kakao-login">
                <a href="/kakao-login" class="btn kakao-btn">카카오 로그인</a>
            </div>
        </div>
    </div>
</body>
</html>
