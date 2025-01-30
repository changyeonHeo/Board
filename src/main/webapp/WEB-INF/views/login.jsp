<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix ="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>로그인</title>
    <link rel="stylesheet" type="text/css" href="/css/login.css">
</head>
<body>

    <!-- 로고를 로그인 컨테이너 밖으로 이동 -->
    <div class="header">
        <h1 class="logo">
            <a href="/">Chang's Board</a>
        </h1>
    </div>

    <div class="login-container">
        <h1 class="login-title">로그인</h1>
        <c:if test="${param.error eq 'true'}">
            <p class="error-message">아이디 또는 비밀번호가 일치하지 않습니다.</p>
        </c:if>
        <c:if test="${param.logout eq 'true'}">
            <p class="success-message">로그아웃 되었습니다.</p>
        </c:if>
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
                <a href="/oauth2/authorization/kakao" class="btn kakao-btn">카카오 로그인</a>
            </div>
        </div>
    </div>

</body>
</html>

