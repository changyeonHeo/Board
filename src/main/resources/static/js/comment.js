$(document).ready(function () {
    console.log("🔹 페이지 로드됨 - 이벤트 등록 확인");

    const bnum = $("#bnum").val();
    if (!bnum) {
        console.error("bnum 값이 없습니다.");
        return;
    }

    // ✅ 댓글 작성 이벤트
    $(document).on("click", "#submit-comment", function () {
        const content = $("#comment-content").val().trim();
        if (content === "") {
            alert("댓글을 입력하세요.");
            return;
        }

        $.ajax({
            url: "/api/comments",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({ bnum: parseInt(bnum), content: content }),
            success: function (newComment) {
                $("#comment-content").val("");
                addCommentToUI(newComment);
            },
            error: function (xhr) {
                console.error("댓글 작성 오류: ", xhr.responseText);
                alert("댓글 작성에 실패했습니다.");
            }
        });
    });

    // ✅ 댓글 목록 불러오기
    function loadComments() {
        $.ajax({
            url: "/api/comments?bnum=" + bnum,
            type: "GET",
            success: function (comments) {
                $("#comments-container").html(`
                    <div class="comment-input">
                        <input type="text" id="comment-content" placeholder="댓글을 입력하세요">
                        <button id="submit-comment">등록</button>
                    </div>
                `);

                comments.forEach(comment => addCommentToUI(comment));
            },
            error: function (xhr) {
                console.error("댓글 불러오기 오류: ", xhr.responseText);
            }
        });
    }

    // ✅ 댓글을 UI에 추가하는 함수
    function addCommentToUI(comment) {
        let commentHtml = `
            <div class="comment" id="comment-${comment.id}">
                <p><strong>${comment.writer}</strong> - ${formatDate(comment.createdDate)}</p>
                <p>${comment.content}</p>
                <button class="reply-toggle" data-comment-id="${comment.id}">답글</button>
                <button class="delete-comment-btn" data-comment-id="${comment.id}">삭제</button>
                <div class="reply-section" id="reply-section-${comment.id}" style="display: none;">
                    <input type="text" id="reply-content-${comment.id}" class="reply-input" placeholder="답글을 입력하세요">
                    <button class="reply-btn" data-parent-id="${comment.id}">등록</button>
                </div>
                <div class="replies" id="replies-${comment.id}"></div>
            </div>
        `;

        $("#comments-container").append(commentHtml);

        if (comment.replies && comment.replies.length > 0) {
            comment.replies.forEach(reply => addReplyToUI(reply, comment.id));
        }
    }

    // ✅ 대댓글을 UI에 추가하는 함수
    function addReplyToUI(reply, parentId) {
        let replyHtml = `
            <div class="reply" id="reply-${reply.id}" style="margin-left: 20px; padding-left: 10px; border-left: 1px solid #ddd;">
                <p><strong>${reply.writer}</strong> - ${formatDate(reply.createdDate)}</p>
                <p>${reply.isDeleted ? "삭제된 댓글입니다." : reply.content}</p>
                <button class="delete-reply-btn" data-reply-id="${reply.id}">삭제</button>
            </div>
        `;

        $("#replies-" + parentId).append(replyHtml);
    }

    $(document).on("click", ".reply-toggle", function () {
        var commentId = $(this).data("comment-id");
        $("#reply-section-" + commentId).toggle();
    });

    // ✅ 대댓글 작성 이벤트
    $(document).on("click", ".reply-btn", function () {
        const parentId = $(this).data("parent-id");
        const replyContent = $("#reply-content-" + parentId).val().trim();

        if (replyContent === "") {
            alert("답글을 입력하세요.");
            return;
        }

        $.ajax({
            url: "/api/comments/" + parentId + "/reply",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({ bnum: parseInt(bnum), content: replyContent }),
            success: function (newReply) {
                $("#reply-content-" + parentId).val("");
                addReplyToUI(newReply, parentId);
            },
            error: function (xhr) {
                console.error("대댓글 작성 오류:", xhr.responseText);
                alert("대댓글 작성 실패: " + xhr.responseText);
            }
        });
    });

    // ✅ 댓글 삭제 이벤트
    $(document).on("click", ".delete-comment-btn", function () {
        var commentId = $(this).data("comment-id");
        deleteComment(commentId);
    });

    // ✅ 댓글 & 대댓글 삭제 함수
function deleteComment(commentId, isReply = false) {
    if (confirm("댓글을 삭제하시겠습니까?")) {
        $.ajax({
            url: "/api/comments/" + commentId,
            type: "DELETE",
            success: function () {
                if (isReply) {
                    console.log("✅ 대댓글 삭제 완료: ", commentId);

                    // ✅ 대댓글 삭제 후 UI 업데이트
                    const replyElement = $("#reply-" + commentId);
                    replyElement.find(".reply-content").remove(); // 기존 내용 삭제
                    replyElement.append('<p class="deleted-text" style="color: gray;">삭제된 댓글입니다.</p>');

                    // ✅ 삭제 버튼 제거
                    replyElement.find(".delete-reply-btn").remove();
                } else {
                    console.log("✅ 부모 댓글 삭제 완료: ", commentId);
                    $("#comment-" + commentId).remove();
                }
            },
            error: function (xhr) {
                alert("삭제 실패: " + xhr.responseText);
                console.error("❌ 삭제 오류:", xhr);
            }
        });
    }
}




    function formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleString("ko-KR", {
            year: "numeric", month: "2-digit", day: "2-digit",
            hour: "2-digit", minute: "2-digit"
        });
    }

    loadComments();
});
