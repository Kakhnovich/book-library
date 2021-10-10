<jsp:useBean id="selectedBook" scope="request" type="com.itechart.studets_lab.book_library.model.Book"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Book Info</title>
</head>
<body>
<div id="myModal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <span onclick="closeModal()">&times;</span>
        </div>
        <div class="modal-body">
            <p>${selectedBook.title}</p>
        </div>
    </div>
</div>
<%--<form method="post" action="${pageContext.request.contextPath}/controller?command=update_book?title=${book.title}">--%>
<%--    <input name="">--%>
<%--</form>--%>
</body>
</html>
