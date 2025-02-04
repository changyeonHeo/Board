// ✅ 댓글 작성 AJAX 함수
function createComment(bnum, parentId, content) {
    const requestData = {
        bnum: bnum, // 게시글 번호
        parentId: parentId, // null이면 댓글, 값이 있으면 대댓글
        content: content // 작성된 댓글 내용
    };

    // AJAX 요청
    $.ajax({
        url: "/api/comments", // 서버 API 경로
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(requestData), // JSON 데이터로 변환
        success: function (response) {
            alert("댓글 작성 성공");
            // 댓글 목록 새로고침
            loadComments(bnum);
        },
        error: function (xhr) {
            console.error("댓글 작성 오류:", xhr.responseText);
            alert("댓글 작성 중 오류가 발생했습니다.");
        }
    });
}

// ✅ 댓글 목록 불러오기 AJAX 함수
function loadComments(bnum) {
    $.ajax({
        url: `/api/comments?bnum=${bnum}`, // 댓글 목록 API
        type: "GET",
        success: function (comments) {
            renderComments(comments); // 댓글 렌더링
        },
        error: function (xhr) {
            console.error("댓글 불러오기 오류:", xhr.responseText);
            alert("댓글을 불러오는 중 오류가 발생했습니다.");
        }
    });
}

// ✅ 댓글 렌더링 함수
function renderComments(comments) {
    const commentList = $("#comment-list"); // 댓글 목록 요소
    commentList.empty(); // 기존 댓글 목록 초기화

    comments.forEach(comment => {
        const commentHtml = `
            <div class="comment-item" data-id="${comment.id}">
                <p class="comment-content">${comment.content}</p>
                <p class="comment-meta">
                    작성자: ${comment.writer} / 작성일: ${comment.createdDate}
                </p>
                <button class="reply-btn" data-id="${comment.id}">답글</button>
            </div>
        `;
        commentList.append(commentHtml);
    });

    // 답글 버튼 이벤트 등록
    $(".reply-btn").on("click", function () {
        const parentId = $(this).data("id");
        const content = prompt("답글 내용을 입력하세요:");
        if (content) {
            const bnum = $("#bnum").val(); // 게시글 번호 가져오기
            createComment(bnum, parentId, content); // 대댓글 작성
        }
    });
}

// ✅ 댓글 작성 폼 제출 이벤트
$(document).ready(function () {
    $("#comment-form").on("submit", function (event) {
        event.preventDefault(); // 기본 폼 제출 막기

        const bnum = $("#bnum").val(); // 게시글 번호
        const content = $("#comment-content").val(); // 댓글 내용

        if (!content.trim()) {
            alert("댓글 내용을 입력하세요.");
            return;
        }

        createComment(bnum, null, content); // 댓글 작성
        $("#comment-content").val(""); // 댓글 입력 칸 초기화
    });

    // 초기 댓글 로드
    const bnum = $("#bnum").val(); // 현재 게시글 번호
    loadComments(bnum);
});
