<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>게시글 보기</title>
    <link rel="stylesheet" type="text/css" href="/css/main.css">
    <link rel="stylesheet" type="text/css" href="/css/board.css">
</head>
<body>
    <!-- ✅ 공통 헤더 포함 -->
    <%@ include file="../main_top.jsp" %>

    <!-- 📌 게시글 상세 내용 -->
    <div class="content-container">
        <h2 class="content-title">${board.title}</h2>

        <div class="content-info">
            <span class="content-writer">${board.writer}</span>
            <span class="content-date">
                <fmt:formatDate value="${board.formattedDate}" pattern="yyyy-MM-dd HH:mm"/>
            </span>
        </div>

        <div class="content-body">
            ${board.content}
        </div>
    </div>

    <!-- 📌 목록보기 버튼 -->
    <div class="list-button">
        <a href="/" class="list-btn">목록보기</a>
    </div>
</body>
</html>
