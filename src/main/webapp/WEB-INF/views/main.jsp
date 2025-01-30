<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chang's Board</title>
    <link rel="stylesheet" type="text/css" href="css/main.css">
    <link rel="stylesheet" type="text/css" href="css/board.css">
</head>
<body>
    <!-- 헤더 -->
    <div class="header">
        <h1 class="logo">
            <a href="/">Chang's Board</a>
        </h1>
        <div class="util_box">
            <c:choose>
                <c:when test="${pageContext.request.userPrincipal != null}">
                    <p>${pageContext.request.userPrincipal.name}님 어서오세요!</p>
                    <a href="/logout" class="main-btn">로그아웃</a>
                </c:when>
                <c:otherwise>
                    <a href="/login" class="main-btn">로그인</a> 
                    <a href="/register" class="main-btn">회원가입</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <table class="content-table">
        <thead>
            <tr>
                <th>번호</th>
                <th>제목</th>
                <th>작성자</th>
                <th>작성일</th>
            </tr>
        </thead>
        <tbody>
        </tbody>
    </table>
    <div class="write-button">
    	<a href="/board/write">글쓰기</a>
	</div>
</body>
</html>
