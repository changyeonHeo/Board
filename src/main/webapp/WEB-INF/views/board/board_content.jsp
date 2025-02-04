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
    <script src="/js/comment.js"></script>
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
<div class="comment-section">
    <h3>댓글</h3>
    <form id="comment-form">
        <input type="hidden" id="bnum" value="${board.bnum}" />
        <textarea id="comment-content" placeholder="댓글을 입력하세요"></textarea>
        <button type="submit">댓글 작성</button>
    </form>
    <div id="comment-list"></div> <!-- 댓글 목록 -->
</div>
        <!-- 📌 버튼 그룹 (목록, 수정, 삭제) -->
<div class="content-button-group">
    <a href="/" class="list-btn">목록보기</a>
    <a href="/board/edit/${board.bnum}" class="edit-btn">수정하기</a>
    <button type="button" class="delete-btn" onclick="deleteBoard(${board.bnum})">삭제하기</button>
</div>

    </div>
 <table class="prev-next-table">
    <tbody>
        <c:if test="${prevPost != null}">
            <tr>
                <td class="arrow-cell">⬆</td>
                <td class="label-cell">이전글</td>
                <td class="title-cell">
                    <a href="/board/${prevPost.bnum}" class="prev-next-link">${prevPost.title}</a>
                </td>
            </tr>
        </c:if>
        <c:if test="${nextPost != null}">
            <tr>
                <td class="arrow-cell">⬇</td>
                <td class="label-cell">다음글</td>
                <td class="title-cell">
                    <a href="/board/${nextPost.bnum}" class="prev-next-link">${nextPost.title}</a>
                </td>
            </tr>
        </c:if>
    </tbody>
</table>

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
