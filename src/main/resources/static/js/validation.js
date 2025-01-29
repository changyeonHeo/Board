$(document).ready(function () {
    const validationState = {
        memberId: false,
        memberName: false,
        memberPasswd: false,
        confirmPassword: false,
        memberEmail: false,
        memberTel: false,
    };

    const registerButton = $('#register-btn');

    // 버튼 상태 업데이트 함수
    function updateButtonState() {
        const allValid = Object.values(validationState).every((value) => value);

        if (allValid) {
            registerButton.prop('disabled', false).css({
                'background-color': '#007bff', // 활성화 색상 (파란색)
                'color': 'white',
                'cursor': 'pointer',
            });
        } else {
            registerButton.prop('disabled', true).css({
                'background-color': '#ccc', // 비활성화 색상 (회색)
                'color': '#666',
                'cursor': 'not-allowed',
            });
        }
    }

    // 초기 버튼 상태 설정
    updateButtonState();

    // 아이디 유효성 검사
    $('input[name="memberId"]').on('input', function () {
        const username = $(this).val();
        const regex = /^[a-zA-Z0-9]{4,12}$/;
        const messageElem = $(this).siblings('.message');

        if (!regex.test(username)) {
            messageElem.text('아이디는 영어와 숫자로 4~12자만 가능합니다.').addClass('error').removeClass('success');
            validationState.memberId = false;
        } else {
            messageElem.text('사용 가능한 아이디입니다.').addClass('success').removeClass('error');
            validationState.memberId = true;
        }
        updateButtonState();
    });

    // 이름 유효성 검사
    $('input[name="memberName"]').on('input', function () {
        const name = $(this).val();
        const regex = /^[가-힣a-zA-Z]{2,}$/;
        const messageElem = $(this).siblings('.name-message');

        if (!regex.test(name)) {
            messageElem.text('이름은 한글 또는 영문으로 2자 이상 입력해야 합니다.').addClass('error').removeClass('success');
            validationState.memberName = false;
        } else {
            messageElem.text('이름이 유효합니다.').addClass('success').removeClass('error');
            validationState.memberName = true;
        }
        updateButtonState();
    });

    // 비밀번호 유효성 검사
    $('input[name="memberPasswd"]').on('input', function () {
        const password = $(this).val();
        const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{10,}$/;
        const messageElem = $(this).siblings('.password-message');

        if (!regex.test(password)) {
            messageElem.text('비밀번호는 영어, 숫자, 특수문자를 포함하여 10자 이상이어야 합니다.').addClass('error').removeClass('success');
            validationState.memberPasswd = false;
        } else {
            messageElem.text('비밀번호가 유효합니다.').addClass('success').removeClass('error');
            validationState.memberPasswd = true;
        }
        checkPasswordMatch();
        updateButtonState();
    });

    // 비밀번호 확인 유효성 검사
    $('input[name="confirm_password"]').on('input', function () {
        checkPasswordMatch();
        updateButtonState();
    });

    function checkPasswordMatch() {
        const password = $('input[name="memberPasswd"]').val();
        const confirmPassword = $('input[name="confirm_password"]').val();
        const messageElem = $('input[name="confirm_password"]').siblings('.confirm-password-message');

        if (password !== confirmPassword || confirmPassword === '') {
            messageElem.text('비밀번호가 일치하지 않습니다.').addClass('error').removeClass('success');
            validationState.confirmPassword = false;
        } else {
            messageElem.text('비밀번호가 일치합니다.').addClass('success').removeClass('error');
            validationState.confirmPassword = true;
        }
    }

    // 이메일 유효성 검사
    $('input[name="memberEmail"]').on('input', function () {
        const email = $(this).val().trim();
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        const messageElem = $(this).siblings('.email-message');

        if (!regex.test(email)) {
            messageElem.text('유효한 이메일 주소를 입력해 주세요.').addClass('error').removeClass('success');
            validationState.memberEmail = false;
        } else {
            // AJAX 요청
            $.ajax({
                url: '/api/validateEmail',
                type: 'GET',
                data: { email: email },
                success: function (response) {
                    if (response.status === 'EXIST') {
                        messageElem.text(response.message).addClass('error').removeClass('success');
                        validationState.memberEmail = false;
                    } else {
                        messageElem.text('사용 가능한 이메일입니다.').addClass('success').removeClass('error');
                        validationState.memberEmail = true;
                    }
                    updateButtonState();
                },
                error: function () {
                    messageElem.text('이메일 확인 요청 실패').addClass('error').removeClass('success');
                    validationState.memberEmail = false;
                },
            });
        }
        updateButtonState();
    });

    // 전화번호 유효성 검사
    $('input[name="memberTel"]').on('input', function () {
        const phone = $(this).val().replace(/\D/g, '');
        const messageElem = $(this).siblings('.phone-message');

        $(this).val(phone);
        if (phone.length < 10 || phone.length > 11) {
            messageElem.text('전화번호는 10~11자리 숫자로 입력해야 합니다.').addClass('error').removeClass('success');
            validationState.memberTel = false;
        } else {
            messageElem.text('전화번호가 유효합니다.').addClass('success').removeClass('error');
            validationState.memberTel = true;
        }
        updateButtonState();
    });
});
