$(document).ready(function () {
    const validationState = {
        memberId: false,
        memberPasswd: false,
        confirmPassword: false,
        memberName: false,
        memberTel: false,
        memberEmail: false,
    };

    function enableSubmit() {
        const allValid = Object.values(validationState).every((value) => value);
        const registerButton = $("#register-btn");

        if (allValid) {
            registerButton.prop("disabled", false).css("background-color", "#007bff").css("cursor", "pointer");
        } else {
            registerButton.prop("disabled", true).css("background-color", "#ccc").css("cursor", "not-allowed");
        }
    }

    // ✅ 아이디 중복 확인 AJAX
    $("input[name='memberId']").on("input", function () {
        const memberId = $(this).val();
        const regex = /^[a-zA-Z0-9]{4,12}$/;
        const messageElem = $(this).siblings(".message");

        if (!regex.test(memberId)) {
            messageElem.text("아이디는 영어와 숫자로 4~12자만 가능합니다.").addClass("error").removeClass("success");
            validationState.memberId = false;
            enableSubmit();
            return;
        }

        $.ajax({
            url: "/api/validateId",
            type: "GET",
            data: { memberId },
            success: function (data) {
                if (data.status === "AVAILABLE") {
                    messageElem.text("사용 가능한 아이디입니다.").addClass("success").removeClass("error");
                    validationState.memberId = true;
                } else {
                    messageElem.text("이미 존재하는 아이디입니다.").addClass("error").removeClass("success");
                    validationState.memberId = false;
                }
                enableSubmit();
            },
            error: function () {
                messageElem.text("아이디 확인 중 오류가 발생했습니다.").addClass("error").removeClass("success");
                validationState.memberId = false;
                enableSubmit();
            },
        });
    });

    // ✅ 이름 유효성 검사 (중복 확인 없음)
    $("input[name='memberName']").on("input", function () {
        const memberName = $(this).val();
        const regex = /^[가-힣a-zA-Z]{2,}$/;
        const messageElem = $(this).siblings(".name-message");

        if (!regex.test(memberName)) {
            messageElem.text("이름은 한글 또는 영문으로 2자 이상 입력해야 합니다.").addClass("error").removeClass("success");
            validationState.memberName = false;
        } else {
            messageElem.text("이름이 유효합니다.").addClass("success").removeClass("error");
            validationState.memberName = true;
        }
        enableSubmit();
    });

    // ✅ 비밀번호 유효성 검사
    $("input[name='memberPasswd']").on("input", function () {
        const password = $(this).val();
        const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
        const messageElem = $(this).siblings(".password-message");

        if (!regex.test(password)) {
            messageElem.text("비밀번호는 8자리 이상, 영어, 숫자, 특수문자를 포함해야 합니다.").addClass("error").removeClass("success");
            validationState.memberPasswd = false;
        } else {
            messageElem.text("비밀번호가 유효합니다.").addClass("success").removeClass("error");
            validationState.memberPasswd = true;
        }
        checkPasswordMatch();
        enableSubmit();
    });

    // ✅ 비밀번호 확인 유효성 검사
    $("input[name='confirm_password']").on("input", function () {
        checkPasswordMatch();
        enableSubmit();
    });

    function checkPasswordMatch() {
        const password = $("input[name='memberPasswd']").val();
        const confirmPassword = $("input[name='confirm_password']").val();
        const messageElem = $("input[name='confirm_password']").siblings(".confirm-password-message");

        if (password !== confirmPassword || confirmPassword === "") {
            messageElem.text("비밀번호가 일치하지 않습니다.").addClass("error").removeClass("success");
            validationState.confirmPassword = false;
        } else {
            messageElem.text("비밀번호가 일치합니다.").addClass("success").removeClass("error");
            validationState.confirmPassword = true;
        }
    }

    // ✅ 이메일 중복 확인 AJAX
    $("input[name='memberEmail']").on("input", function () {
        const email = $(this).val().trim();
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        const messageElem = $(this).siblings(".email-message");

        if (!regex.test(email)) {
            messageElem.text("유효한 이메일 주소를 입력해 주세요.").addClass("error").removeClass("success");
            validationState.memberEmail = false;
            enableSubmit();
            return;
        }

        $.ajax({
            url: "/api/validateEmail",
            type: "GET",
            data: { email },
            success: function (data) {
                if (data.status === "AVAILABLE") {
                    messageElem.text("사용 가능한 이메일입니다.").addClass("success").removeClass("error");
                    validationState.memberEmail = true;
                } else {
                    messageElem.text("이미 존재하는 이메일입니다.").addClass("error").removeClass("success");
                    validationState.memberEmail = false;
                }
                enableSubmit();
            },
            error: function () {
                messageElem.text("이메일 확인 중 오류가 발생했습니다.").addClass("error").removeClass("success");
                validationState.memberEmail = false;
                enableSubmit();
            },
        });
    });

    // ✅ 전화번호 중복 확인 AJAX
    $("input[name='memberTel']").on("input", function () {
        let phone = $(this).val().replace(/\D/g, "");
        const messageElem = $(this).siblings(".phone-message");

        $(this).val(phone);
        if (phone.length < 10 || phone.length > 11) {
            messageElem.text("전화번호는 10~11자리 숫자로 입력해야 합니다.").addClass("error").removeClass("success");
            validationState.memberTel = false;
            enableSubmit();
            return;
        }

        $.ajax({
            url: "/api/validatePhone",
            type: "GET",
            data: { phone },
            success: function (data) {
                if (data.status === "AVAILABLE") {
                    messageElem.text("사용 가능한 전화번호입니다.").addClass("success").removeClass("error");
                    validationState.memberTel = true;
                } else {
                    messageElem.text("이미 존재하는 전화번호입니다.").addClass("error").removeClass("success");
                    validationState.memberTel = false;
                }
                enableSubmit();
            },
            error: function () {
                messageElem.text("전화번호 확인 중 오류가 발생했습니다.").addClass("error").removeClass("success");
                validationState.memberTel = false;
                enableSubmit();
            },
        });
    });

    // ✅ 회원가입 버튼 클릭 이벤트
    $("#registration-form").on("submit", function (event) {
        event.preventDefault();

        $.ajax({
            url: "/register",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({
                memberId: $("input[name='memberId']").val(),
                memberPasswd: $("input[name='memberPasswd']").val(),
                memberName: $("input[name='memberName']").val(),
                memberTel: $("input[name='memberTel']").val(),
                memberEmail: $("input[name='memberEmail']").val(),
            }),
            success: function (response) {
                alert(response.message);
                window.location.href = "/login";
            },
            error: function (xhr) {
                let response = JSON.parse(xhr.responseText);
                alert(response.message);
            },
        });
    });
});
