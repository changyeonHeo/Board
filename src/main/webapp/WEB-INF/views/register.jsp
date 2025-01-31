<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- register.jsp -->
<html>
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>
    <link rel="stylesheet" type="text/css" href="/css/register.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/validation.js"></script>
</head>
<body>
    <div class="register-container">
        <h1 class="register-title">회원가입</h1>
        <form action="/register" method="post" id="registration-form">
            <div class="input-group">
                <label for="memberId">아이디</label>
                <input type="text" id="memberId" name="memberId" placeholder="아이디를 입력하세요" required>
                <div class="message"></div>
            </div>
            <div class="input-group">
                <label for="memberName">이름</label>
                <input type="text" id="memberName" name="memberName" placeholder="이름을 입력하세요" required>
                <div class="name-message"></div>
            </div>
            <div class="input-group">
                <label for="memberPasswd">비밀번호</label>
                <input type="password" id="memberPasswd" name="memberPasswd" placeholder="비밀번호를 입력하세요" required>
                <div class="password-message"></div>
            </div>
            <div class="input-group">
                <label for="confirm_password">비밀번호 확인</label>
                <input type="password" id="confirm_password" name="confirm_password" placeholder="비밀번호를 다시 입력하세요" required>
                <div class="confirm-password-message"></div>
            </div>
            <div class="input-group">
                <label for="memberEmail">이메일</label>
                <input type="email" id="memberEmail" name="memberEmail" placeholder="이메일 주소를 입력하세요" required>
                <div class="email-message"></div>
            </div>
            <div class="input-group">
                <label for="memberTel">전화번호</label>
                <input type="tel" id="memberTel" name="memberTel" placeholder="전화번호를 입력하세요" required>
                <div class="phone-message"></div>
            </div>
            <button type="submit" class="btn register-btn" id="register-btn" disabled>회원가입</button>
        </form>
    </div>
</body>
</html>
