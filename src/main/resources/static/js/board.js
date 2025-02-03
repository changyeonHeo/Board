$(document).ready(function () {
    // ✅ Summernote 초기화
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

    // ✅ 게시글 저장 및 수정
    $("#board-form").on("submit", function (event) {
        event.preventDefault(); // 기본 폼 제출 막기

        const bnum = $("#bnum").val(); // ✅ 기존 글인지 확인
        const formData = {
            bnum: bnum ? bnum : null, // bnum이 존재하면 수정, 없으면 새 글
            title: $("#title").val(),
            content: $("#content").val(),
            writer: $("#writer").val()
        };

        // ✅ AJAX 요청
        $.ajax({
            url: "/api/board" + (bnum ? "/" + bnum : ""),
            type: bnum ? "PUT" : "POST", // ✅ 수정이면 PUT, 새 글이면 POST
            contentType: "application/json",
            data: JSON.stringify(formData),
            success: function (response) {
                alert(response);
                window.location.href = "/";
            },
            error: function (xhr) {
                console.error("Error: ", xhr.responseText);
                alert("저장 중 오류가 발생했습니다.");
            }
        });
    });

    // ✅ 이미지 업로드 함수
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
                console.log("Uploaded Image URL:", data.imageUrl); // ✅ 서버 응답 확인
                $("#content").summernote("insertImage", data.imageUrl);
            },
            error: function () {
                alert("이미지 업로드 중 오류가 발생했습니다.");
            },
        });
    }

    // ✅ 게시글 수정 처리
    $("#edit-form").on("submit", function (event) {
        event.preventDefault();

        const bnum = $("#bnum").val(); // ✅ 기존 글의 bnum 가져오기
        const formData = {
            bnum: bnum,
            title: $("#title").val(),
            content: $("#content").val(),
            bimage: $("#bimage").val()
        };

        $.ajax({
            url: "/api/board/" + bnum, // ✅ 수정할 글의 ID 포함
            type: "PUT",
            contentType: "application/json",
            data: JSON.stringify(formData),
            success: function (response) {
                alert("게시글이 수정되었습니다.");
                window.location.href = "/"; // 수정 후 메인 페이지 이동
            },
            error: function () {
                alert("수정 중 오류가 발생했습니다.");
            }
        });
    });
});
