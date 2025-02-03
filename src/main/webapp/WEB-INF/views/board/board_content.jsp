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
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
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

        <!-- 📌 버튼 그룹 -->
        <div class="button-group">
            <c:if test="${board.writer eq pageContext.request.userPrincipal.name}">
                <a href="/board/edit/${board.bnum}" class="edit-btn">수정</a>
                <button class="delete-btn" onclick="deleteBoard(${board.bnum})">삭제</button>
            </c:if>
            <a href="/" class="list-btn">목록보기</a>
        </div>
    </div>

    <!-- 📌 게시글 삭제 스크립트 -->
    <script>
        function deleteBoard(boardId) {
            if (confirm("정말로 삭제하시겠습니까?")) {
                $.ajax({
                    url: "/api/board/" + boardId,
                    type: "DELETE",
                    success: function(response) {
                        alert("게시글이 삭제되었습니다.");
                        window.location.href = "/";
                    },
                    error: function(xhr) {
                        alert("게시글 삭제에 실패했습니다.");
                        console.error(xhr.responseText);
                    }
                });
            }
        }
    </script>
</body>
</html>
