<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

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

	<!-- 📌 게시글 상세 내용 -->
	<div class="content-container">
		<h2 class="content-title">${board.title}</h2>

		<div class="content-info">
			<span class="content-writer">${board.writer}</span> <span
				class="content-date"> <fmt:formatDate
					value="${board.formattedDate}" pattern="yyyy-MM-dd HH:mm" />
			</span>
		</div>

		<div class="content-body">${board.content}</div>

		<!-- ✅ 게시글 번호 -->
		<input type="hidden" id="bnum" value="${board.bnum}">

		<!-- 📌 댓글 영역 -->
		<div id="comments-container">
			<!-- ✅ 댓글 입력창 -->
			<div class="comment-input">
				<input type="text" id="comment-content" placeholder="댓글을 입력하세요">
				<button id="submit-comment">등록</button>
			</div>

			<!-- ✅ 댓글 목록 -->
			<c:forEach var="comment" items="${comments}">
				<script>console.log("Comment ID: ${comment.id}, isDeleted: ${comment.isDeleted}");</script>
				<div class="comment" id="comment-${comment.id}">
					<p>
						<strong>${comment.writer}</strong> -
						<fmt:formatDate value="${comment.createdDate}"
							pattern="yyyy-MM-dd HH:mm" />
					</p>

					<!-- 🔥 isDeleted 값을 콘솔에서 확인 -->
					<script>console.log("Comment ID: ${comment.id}, isDeleted: ${comment.isDeleted}");</script>

					<c:choose>
						<c:when test="${comment.isDeleted}">
							<p style="color: gray;">삭제된 댓글입니다.</p>
						</c:when>
						<c:otherwise>
							<p>${comment.content}</p>
							<button class="delete-comment-btn"
								data-comment-id="${comment.id}">삭제</button>
						</c:otherwise>
					</c:choose>

					<!-- ✅ 대댓글 작성 버튼 -->
					<button class="reply-toggle" data-comment-id="${comment.id}">답글</button>

					<!-- ✅ 대댓글 입력창 -->
					<div class="reply-section" id="reply-section-${comment.id}"
						style="display: none;">
						<input type="text" id="reply-content-${comment.id}"
							class="reply-input" placeholder="답글을 입력하세요">
						<button class="reply-btn" data-parent-id="${comment.id}">등록</button>
					</div>

					<!-- ✅ 대댓글 리스트 -->
					<div class="replies" id="replies-${comment.id}">
						<c:forEach var="reply" items="${comment.replies}">
							<div class="reply" id="reply-${reply.id}">
								<p>
									<strong>${reply.writer}</strong> -
									<fmt:formatDate value="${reply.createdDate}"
										pattern="yyyy-MM-dd HH:mm" />
								</p>

								<!-- 🔥 isDeleted 값 확인 -->
								<script>console.log("Reply ID: ${reply.id}, isDeleted: ${reply.isDeleted}");</script>

								<c:choose>
									<c:when test="${reply.isDeleted}">
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

		<!-- 📌 버튼 그룹 (목록, 수정, 삭제) -->
		<div class="content-button-group">
			<a href="/" class="list-btn">목록보기</a> <a
				href="/board/edit/${board.bnum}" class="edit-btn">수정하기</a>
			<button type="button" class="delete-btn"
				onclick="deleteBoard(${board.bnum})">삭제하기</button>
		</div>

	</div>

	<!-- 📌 이전글 / 다음글 -->
	<table class="prev-next-table">
		<tbody>
			<c:if test="${prevPost != null}">
				<tr>
					<td class="arrow-cell">⬆</td>
					<td class="label-cell">이전글</td>
					<td class="title-cell"><a href="/board/${prevPost.bnum}"
						class="prev-next-link">${prevPost.title}</a></td>
				</tr>
			</c:if>
			<c:if test="${nextPost != null}">
				<tr>
					<td class="arrow-cell">⬇</td>
					<td class="label-cell">다음글</td>
					<td class="title-cell"><a href="/board/${nextPost.bnum}"
						class="prev-next-link">${nextPost.title}</a></td>
				</tr>
			</c:if>
		</tbody>
	</table>

	<!-- 📌 게시글 & 댓글 삭제 스크립트 -->
	<script>
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
        function deleteComment(commentId) {
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
        }
    </script>
</body>
</html>
