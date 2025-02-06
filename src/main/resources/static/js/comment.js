$(document).ready(function () {
    // ✅ bnum 값 확인
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

        console.log("댓글 저장:", { bnum, content });

        // ✅ AJAX 요청 (댓글 저장)
        $.ajax({
            url: "/api/comments",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({ bnum: parseInt(bnum), content: content }), // ✅ bnum을 정수형으로 변환
            success: function () {
                $("#comment-content").val(""); // ✅ 입력창 초기화
                loadComments(); // ✅ 댓글 목록 새로고침
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

                comments.forEach(comment => {
                    let commentHtml = `
                        <div class="comment" id="comment-${comment.id}">
                            <p><strong>${comment.writer}</strong> - ${formatDate(comment.createdDate)}</p>
                            <p>${comment.content}</p>
                            <button class="reply-toggle" data-comment-id="${comment.id}">답글</button>
                            <div class="reply-section" id="reply-section-${comment.id}" style="display: none;">
                                <input type="text" id="reply-content-${comment.id}" class="reply-input" placeholder="답글을 입력하세요">
                                <button class="reply-btn" data-parent-id="${comment.id}">등록</button>
                            </div>
                            <div class="replies" id="replies-${comment.id}"></div>
                        </div>
                    `;

                    $("#comments-container").append(commentHtml);

                    // ✅ 대댓글 불러오기 (부모 댓글 내부에 추가)
                    if (comment.replies && comment.replies.length > 0) {
                        comment.replies.forEach(reply => {
                            let replyHtml = `
                                <div class="reply" id="reply-${reply.id}" style="margin-left: 20px; padding-left: 10px; border-left: 1px solid #ddd;">
                                    <p><strong>${reply.writer}</strong> - ${formatDate(reply.createdDate)}</p>
                                    <p>${reply.content}</p>
                                </div>
                            `;
                            $("#replies-" + comment.id).append(replyHtml);
                        });
                    }
                });
            },
            error: function (xhr) {
                console.error("댓글 불러오기 오류: ", xhr.responseText);
            }
        });
    }

    // ✅ 대댓글 입력창 토글 기능
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

        console.log("대댓글 저장:", { parentId, bnum, replyContent });

        // ✅ AJAX 요청 (대댓글 저장)
        $.ajax({
            url: "/api/comments/" + parentId + "/reply",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({ bnum: parseInt(bnum), content: replyContent }), // ✅ bnum을 정수형으로 변환
            success: function () {
                $("#reply-content-" + parentId).val(""); // ✅ 입력창 초기화
                loadComments(); // ✅ 댓글 목록 새로고침
            },
            error: function (xhr) {
                console.error("대댓글 작성 오류: ", xhr.responseText);
                alert("대댓글 작성에 실패했습니다.");
            }
        });
    });

    // ✅ 날짜 포맷 함수
    function formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleString("ko-KR", {
            year: "numeric", month: "2-digit", day: "2-digit",
            hour: "2-digit", minute: "2-digit"
        });
    }

    // ✅ 페이지 로드 시 댓글 불러오기
    loadComments();
});
