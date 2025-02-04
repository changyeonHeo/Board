<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>글쓰기</title>
    <link rel="stylesheet" type="text/css" href="/css/board.css">
    <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-lite.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.20/dist/summernote-lite.min.js"></script>
    <script src="/js/board.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.20/lang/summernote-ko-KR.min.js"></script>
</head>
<body>
    <div class="write-page-container">
        <h1 class="page-title">글쓰기</h1>
        <form id="board-form" class="write-form">
    <!-- 게시글 수정 시 기존 bnum을 hidden input으로 전달 -->
    <c:if test="${not empty board}">
        <input type="hidden" id="bnum" name="bnum" value="${board.bnum}">
    </c:if>

    <div class="form-group">
        <label for="title">제목</label>
        <input type="text" id="title" name="title" placeholder="제목을 입력하세요" required 
               value="${board.title}">
    </div>

    <div class="form-group">
        <label for="content">내용</label>
        <textarea id="content" name="content" required>${board.content}</textarea>
    </div>

    <input type="hidden" id="writer" name="writer" value="${writer}">

    <!-- ✅ 버튼 그룹 (등록 & 취소) -->
<div class="write-button-group">
    <button type="submit" class="save-btn">등록</button>
    <a href="/" class="cancel-btn">취소</a>
</div>

</form>


    </div>
</body>
</html>
