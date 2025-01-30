// Summernote와 글쓰기 데이터 전송 처리
$(document).ready(function () {
    // Summernote 초기화
    $("#content").summernote({
        height: 500, // 에디터 높이
        placeholder: "내용을 입력하세요",
        toolbar: [
            ["style", ["style"]],
            ["font", ["bold", "italic", "underline", "clear"]],
            ["fontname", ["fontname"]],
            ["color", ["color"]],
            ["para", ["ul", "ol", "paragraph"]],
            ["table", ["table"]],
            ["insert", ["link", "picture", "video"]],
            ["view", ["fullscreen", "codeview", "help"]],
        ],
        lang: "ko-KR", // Summernote 언어를 한국어로 설정
        disableResizeEditor: true, // 크기 조정 비활성화
    });

    // 글쓰기 데이터 전송 (AJAX)
    $("#board-form").on("submit", function (event) {
        event.preventDefault();

        const data = {
            title: $("#title").val(),
            content: $("#content").val(),
        };

        $.ajax({
            url: "/api/board",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(data),
            success: function (response) {
                alert(response);
                window.location.href = "/";
            },
            error: function () {
                alert("글 저장 중 오류가 발생했습니다.");
            }
        });
    });
});
