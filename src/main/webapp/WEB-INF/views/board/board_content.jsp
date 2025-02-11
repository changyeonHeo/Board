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

    <script>console.log("📌 JSP에서 받은 comments 개수: ${fn:length(comments)}");</script>
</head>
<body>
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

        <input type="hidden" id="bnum" value="${board.bnum}">

        <!-- 📌 댓글 영역 -->
        <div id="comments-container">
            <div class="comment-input">
                <input type="text" id="comment-content" placeholder="댓글을 입력하세요">
                <button id="submit-comment">등록</button>
            </div>

            <!-- ✅ 댓글 목록 -->
            <c:forEach var="comment" items="${comments}">
                <div class="comment" id="comment-${comment.id}">
                    <p><strong>${comment.writer}</strong> - <fmt:formatDate value="${comment.formattedDate}" pattern="yyyy-MM-dd HH:mm" /></p>
                    <c:choose>
                        <c:when test="${comment.isDeleted == true}">
                            <p style="color: gray;">삭제된 댓글입니다.</p>
                        </c:when>
                        <c:otherwise>
                            <p>${comment.content}</p>
                            <button class="delete-comment-btn" data-comment-id="${comment.id}">삭제</button>
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
                                <p><strong>${reply.writer}</strong> - <fmt:formatDate value="${reply.formattedDate}" pattern="yyyy-MM-dd HH:mm" /></p>
                                <c:choose>
                                    <c:when test="${reply.isDeleted == true}">
                                        <p style="color: gray;">삭제된 댓글입니다.</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p>${reply.content}</p>
                                        <button class="delete-reply-btn" data-reply-id="${reply.id}">삭제</button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:forEach>
        </div>

        <div class="content-button-group">
            <a href="/" class="list-btn">목록보기</a>
            <a href="/board/edit/${board.bnum}" class="edit-btn">수정하기</a>
            <button type="button" class="delete-btn" onclick="deleteBoard(${board.bnum})">삭제하기</button>
        </div>
    </div>

    <!-- 📌 댓글 & 대댓글 삭제 기능 -->
    <script>
        $(document).ready(function () {
            console.log("✅ 페이지 로드 완료 - 댓글 삭제 이벤트 등록");

            // ✅ 대댓글 삭제 버튼 이벤트 바인딩 (이벤트 위임 방식)
            $(document).on("click", ".delete-reply-btn", function () {
                let replyId = $(this).data("reply-id");
                console.log("🚀 대댓글 삭제 요청 - ID:", replyId);

                if (confirm("대댓글을 삭제하시겠습니까?")) {
                    $.ajax({
                        url: "/api/comments/" + replyId,
                        type: "DELETE",
                        success: function () {
                            console.log("✅ 대댓글 삭제 성공 - ID:", replyId);

                            // ✅ 삭제된 댓글 표시
                            let replyElement = $("#reply-" + replyId);
                            replyElement.find("p").remove();
                            replyElement.find(".delete-reply-btn").remove();
                            replyElement.append('<p style="color: gray;">삭제된 댓글입니다.</p>');
                        },
                        error: function (xhr) {
                            console.error("❌ 대댓글 삭제 실패:", xhr.responseText);
                            alert("삭제 실패: " + xhr.responseText);
                        }
                    });
                }
            });

            // ✅ 게시글 삭제
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

            // ✅ 댓글 삭제 (대댓글 포함)
            $(document).on("click", ".delete-comment-btn", function () {
                let commentId = $(this).data("comment-id");
                console.log("🚀 댓글 삭제 요청 - ID:", commentId);

                if (confirm("댓글을 삭제하시겠습니까?")) {
                    $.ajax({
                        url: "/api/comments/" + commentId,
                        type: "DELETE",
                        success: function () {
                            console.log("✅ 댓글 삭제 성공 - ID:", commentId);
                            $("#comment-" + commentId).remove();
                        },
                        error: function (xhr) {
                            alert("삭제 실패: " + xhr.responseText);
                        }
                    });
                }
            });
        });
    </script>
</body>
</html>
