<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="main_top.jsp"%>

<!-- 게시글 목록 -->
<div class="board-container">
    <table class="board-table">
        <thead>
            <tr>
                <th>번호</th>
                <th>제목</th>
                <th>작성자</th>
                <th>작성일</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="board" items="${boards}">
                <tr>
                    <td>${board.bnum}</td>
                    <td>
                        <a href="/board/${board.bnum}" class="board-title">${board.title}</a>
                    </td>
                    <td>${board.writer}</td>
                    <td>
                        <fmt:formatDate value="${board.formattedDate}" pattern="yyyy-MM-dd HH:mm"/>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
<!-- 📌 페이지네이션 -->
<div class="pagination">
    <c:if test="${totalPages > 1}">
        <c:if test="${currentPage > 1}">
            <a href="/?page=${currentPage - 1}">&laquo; 이전</a>
        </c:if>
    </c:if>

    <!-- ✅ 항상 1페이지가 표시되도록 설정 -->
    <c:forEach var="i" begin="1" end="${totalPages}">
        <a href="/?page=${i}" class="${currentPage == i ? 'active' : ''}">${i}</a>
    </c:forEach>

    <c:if test="${totalPages > 1}">
        <c:if test="${currentPage < totalPages}">
            <a href="/?page=${currentPage + 1}">다음 &raquo;</a>
        </c:if>
    </c:if>
</div>


<!-- ✅ 글쓰기 버튼 (테이블 아래 위치) -->
<div class="write-button">
    <a href="/board/write" class="write-btn">글쓰기</a>
</div>  
</body>
</html>
