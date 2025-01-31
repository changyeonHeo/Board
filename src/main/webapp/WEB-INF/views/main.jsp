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
    <!-- 글쓰기 버튼 -->
    <div class="write-button">
        <a href="/board/write">글쓰기</a>
    </div>
    
</body>
</html>
