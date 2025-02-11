![image](https://github.com/user-attachments/assets/f8a5fa71-fa1e-4a80-81a8-04ea1deb247b)
# 들어가며
### 1. 프로젝트 소개
프로젝트를 시작하게 된 계기는 게시판을 직접 만들어보며 배우고자 시작하게 되었습니다.
독학으로 관련 기술들을 학습한 이후 제작한 개인 프로젝트이기 때문에 개인적인 만족감을 가지고 있는 프로젝트입니다.

### 2.프로젝트 기능
+ 게시판 - CRUD기능, 페이징 처리
+ 사용자 - ecurity 회원가입 및 로그인, OAuth 2.0 카카오 로그인,회원가입시 유효성 검사 및 중복 검사
+ 댓글 - 댓글달기, 삭제 및 대댓글 달기

### 3.사용 기술
+ Java 17
+ SpringBoot 3.4.2
+ JPA(Spring Data JPA)
+ Spring Security
+ OAuth 2.0
+ Oracle Database 18.0.0
+ HTML/CSS
+ JavaScript

### 4.실행 화면

<details>
<summary>로그인 페이지</summary>
<img src = "https://github.com/user-attachments/assets/bd1cd510-14ff-46ae-b770-9fd8d78a6087">

</details>

<details>
<summary>회원가입 페이지</summary>
<img src = "https://github.com/user-attachments/assets/046ea401-e61c-4952-ac62-9171e14458b2">
<img src = "https://github.com/user-attachments/assets/9b37dac4-e5a9-46df-980d-6725d5ace6b0">
</details>

<details>
<summary>게시글 페이지</summary>
<img src = "https://github.com/user-attachments/assets/102e2533-941d-4130-b753-fc163c121304" alt ="게시글 목록을 페이징처리로 조회가능">
        게시글 목록을 페이징처리로 조회가능
<img src = "https://github.com/user-attachments/assets/3c211a0e-2e08-47fd-a3bc-4e58ca8ece8a" alt ="로그인한 사용자만 글쓰기 가능">
        로그인한 사용자만 글쓰기 가능
<img src = "https://github.com/user-attachments/assets/9b6938a7-9fe2-46ac-8bc6-c7dda8969aa8" alt ="게시글에서 목록보기를 클릭하면 다시 글 목록 페이지로 이동한다.">
        게시글에서 목록보기를 클릭하면 다시 글 목록 페이지로 이동한다.
</details>
<details>
<summary>댓글</summary>
<img src = "https://github.com/user-attachments/assets/d20f1e0c-73c2-4e27-b440-6a3f51a5bee8">
<img src = "https://github.com/user-attachments/assets/51e27cd4-91f1-4e5c-ba50-d1c725ce032b" alt ="댓글이 삭제되는 경우 대댓글과 함께 삭제되며 대댓글이 삭제되는 경우 삭제된 댓글입니다. 라고 바뀐다. 본인이 쓴 댓글과 대댓글만 삭제가능합니다.">
        댓글이 삭제되는 경우 대댓글과 함께 삭제되며 대댓글이 삭제되는 경우 삭제된 댓글입니다. 라고 바뀐다. 본인이 쓴 댓글과 대댓글만 삭제가능합니다.
</details>

### 5.DB설계표
📌 DB 설계표
<img src ="https://github.com/user-attachments/assets/13f0430a-ffc3-4e5b-9e58-1df6007a5a6a">
<img src ="https://github.com/user-attachments/assets/0c40c52e-d8aa-4a63-bbbe-2fe739b41573">
<img src = "https://github.com/user-attachments/assets/5e954ac6-5331-473e-a1a4-03b0575defaf">

### 6. api 설계표
<img src ="https://github.com/user-attachments/assets/7a08c8e8-ed0e-456f-a073-78654b4b546d">
<img src ="https://github.com/user-attachments/assets/09912bbb-ffd8-4cd3-b87c-fbcddab31d3e">
<img src ="https://github.com/user-attachments/assets/11bc3504-b244-4fae-a153-ca63f4753031">

### 7. 마치며
처음에는 그냥 간단하게 CRUD기능이 가능한 게시판을 만들어보려고 했었습니다.
하지만 초기에 진행하면서 다른 기술도 넣고 싶다는 욕심이 나서 계속해서 기능들을 조금씩 추가했습니다.
혼자 독학하면서 공부하기 위해 만든 프로젝트여서 구글링도 많이해보고 또 챗GPT의 도움도 많이 받았습니다.
여러 글을 보고 혼자서 코드를 짜보고 거기에 대한 에러도 많이 만났습니다. 하지만 그 에러를 인터넷에 찾아보고
GPT에 물어보면서 하나씩 문제를 해결해 나갔고 내가 내코드를 다른사람에게 잘 설명할 수 있을까하는 생각도 많이 했습니다.

또한, 이번 프로젝트를 진행하면서 저한테 부족했던 점이 어떤게 있었는지 알 수 있었습니다.
스프링시큐리티에서 인증 사용자 정보 가져오는 방법, AJAX 요청, JPA 연관 관계 매핑 그리고 HTTP 메서드 적절하게 매칭하는방법에 대해서
조금은 알게 된 계기가 된거 같습니다. 부족한 부분에 대해서 스스로 인지하고 발전할 수 있게 되었습니다.
이를 통해 더 나은 웹 어플리케이션을 만들 수 있을것 같다는 자신감도 생겼습니다.







