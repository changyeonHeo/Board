<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>게시글 보기</title>
    <link rel="stylesheet" type="text/css" href="/css/main.css">
    <link rel="stylesheet" type="text/css" href="/css/board.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="/js/comment.js"></script>
</head>
<body>
    <!-- ✅ 공통 헤더 포함 -->
    <%@ include file="../main_top.jsp"%>

    <div class="content-container">
        <h2 class="content-title">${board.title}</h2>

        <div class="content-info">
            <span class="content-writer">${board.writer}</span> 
            <span class="content-date">
                <fmt:formatDate value="${board.formattedDate}" pattern="yyyy-MM-dd HH:mm" />
            </span>
        </div>

        <div class="content-body">${board.content}</div>

        <!-- ✅ 게시글 번호 -->
        <input type="hidden" id="bnum" value="${board.bnum}">

        <!-- 📌 댓글 영역 -->
        <div id="comments-container">
            <div class="comment-input">
                <input type="text" id="comment-content" placeholder="댓글을 입력하세요">
                <button id="submit-comment">등록</button>
            </div>

            <c:forEach var="comment" items="${comments}">
                <div class="comment" id="comment-${comment.id}">
                    <p>
                        <strong>${comment.writer}</strong> - 
                        <fmt:formatDate value="${comment.formattedDate}" pattern="yyyy-MM-dd HH:mm" />
                    </p>

                    <c:choose>
                        <c:when test="${comment.isDeleted}">
                            <p style="color: gray;">삭제된 댓글입니다.</p>
                        </c:when>
                        <c:otherwise>
                            <p>${comment.content}</p>
                            <c:if test="${sessionScope.username == comment.writer}">
                                <button class="delete-comment-btn" data-comment-id="${comment.id}">삭제</button>
                            </c:if>
                        </c:otherwise>
                    </c:choose>

                    <button class="reply-toggle" data-comment-id="${comment.id}">답글</button>

                    <div class="reply-section" id="reply-section-${comment.id}" style="display: none;">
                        <input type="text" id="reply-content-${comment.id}" class="reply-input" placeholder="답글을 입력하세요">
                        <button class="reply-btn" data-parent-id="${comment.id}">등록</button>
                    </div>

                    <div class="replies" id="replies-${comment.id}">
                        <c:forEach var="reply" items="${comment.replies}">
                            <div class="reply" id="reply-${reply.id}">
                                <p>
                                    <strong>${reply.writer}</strong> - 
                                    <fmt:formatDate value="${reply.formattedDate}" pattern="yyyy-MM-dd HH:mm" />
                                </p>
                                <c:choose>
                                    <c:when test="${reply.isDeleted}">
                                        <p style="color: gray;">삭제된 댓글입니다.</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p>${reply.content}</p>
                                        <c:if test="${sessionScope.username == reply.writer}">
                                            <button class="delete-reply-btn" data-reply-id="${reply.id}">삭제</button>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- 📌 버튼 그룹 -->
        <div class="content-button-group">
            <a href="/" class="list-btn">목록보기</a>

            <c:if test="${sessionScope.username == board.writer}">
                <a href="/board/edit/${board.bnum}" class="edit-btn">수정하기</a>
                <button type="button" class="delete-btn" onclick="deleteBoard(${board.bnum})">삭제하기</button>
            </c:if>
        </div>

        <!-- 📌 이전글 / 다음글 -->
        <table class="prev-next-table">
            <tbody>
                <c:if test="${not empty prevPost}">
                    <tr>
                        <td class="arrow-cell">⬆</td>
                        <td class="label-cell">이전글</td>
                        <td class="title-cell">
                            <a href="/board/${prevPost.bnum}" class="prev-next-link">${prevPost.title}</a>
                        </td>
                    </tr>
                </c:if>

                <c:if test="${not empty nextPost}">
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
    </div>

    <script>
        function deleteBoard(boardId) {
            if (confirm("정말로 삭제하시겠습니까?")) {
                $.ajax({
                    url: "/api/board/" + boardId,
                    type: "DELETE",
                    success: function() {
                        alert("게시글이 삭제되었습니다.");
                        window.location.href = "/";
                    },
                    error: function(xhr) {
                        alert("게시글 삭제에 실패했습니다.");
                    }
                });
            }
        }

        $(document).on("click", ".delete-comment-btn", function () {
            var commentId = $(this).data("comment-id");
            if (confirm("댓글을 삭제하시겠습니까?")) {
                $.ajax({
                    url: "/api/comments/" + commentId,
                    type: "DELETE",
                    success: function() {
                        alert("댓글이 삭제되었습니다.");
                        location.reload();
                    },
                    error: function(xhr) {
                        alert("삭제 실패: " + xhr.responseText);
                    }
                });
            }
        });

        $(document).on("click", ".delete-reply-btn", function () {
            var replyId = $(this).data("reply-id");
            if (confirm("대댓글을 삭제하시겠습니까?")) {
                $.ajax({
                    url: "/api/comments/" + replyId,
                    type: "DELETE",
                    success: function() {
                        alert("대댓글이 삭제되었습니다.");
                        location.reload();
                    },
                    error: function(xhr) {
                        alert("삭제 실패: " + xhr.responseText);
                    }
                });
            }
        });
    </script>
</body>
</html>
