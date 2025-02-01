$(document).ready(function () {
    $("#content").summernote({
        height: 500,
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
        lang: "ko-KR",
        disableResizeEditor: true,
        callbacks: {
            onImageUpload: function (files) {
                uploadImage(files[0]);
            },
        },
    });
});

    $(document).ready(function () {
    $("#board-form").on("submit", function (event) {
        event.preventDefault();

        // ✅ 데이터 수집
        const formData = {
            title: $("#title").val(),
            content: $("#content").val(),
            writer: $("#writer").val() // hidden 필드에서 작성자 정보 가져오기
        };

        console.log("보내는 데이터:", formData); // 데이터가 제대로 만들어졌는지 확인 (F12 콘솔)

        // ✅ AJAX 요청
        $.ajax({
            url: "/api/board",
            type: "POST",
            contentType: "application/json", // JSON 형식으로 전송
            data: JSON.stringify(formData), // 데이터를 JSON으로 변환
            success: function (response) {
                alert("글이 성공적으로 저장되었습니다.");
                window.location.href = "/";
            },
            error: function (xhr) {
                console.error("Error: ", xhr.responseText);
                alert("글 저장 중 오류가 발생했습니다.");
            }
        });
    });
});


    // ✅ 이미지 업로드 함수 (서버에 이미지 저장)
    function uploadImage(file) {
    const formData = new FormData();
    formData.append("file", file);

    $.ajax({
                    url: "/api/uploadImage", // 이미지 업로드 API 경로
                    type: "POST",
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (data) {
                        console.log("Uploaded Image URL:", data.url); // ✅ 서버 응답 확인
                        $("#content").summernote("insertImage", data.url);
                    },
        error: function () {
            alert("이미지 업로드 중 오류가 발생했습니다.");
        },
    });
}

