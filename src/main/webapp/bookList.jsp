<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Book List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <script src="js/modal.js"></script>
</head>
<body>
<div class="table-responsive">
    <table class="table table-striped table-sm" border="2">
        <thead>
        <tr>
            <th>Title</th>
            <th>Author(-s)</th>
            <th>Publish Date</th>
            <th>Available Copies</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="book" items="${requestScope.books}">
            <tr>
                <td>
                    <a href="${pageContext.request.contextPath}/controller?command=book&bookISBN=${book.isbn}">${book.title}</a>
                </td>
                <td>
                    <c:forEach var="author" items="${book.authors}">
                        "${author}";
                    </c:forEach>
                </td>
                <td>${book.publishDate}</td>
                <td>${book.totalAmount}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<jsp:include page="pagination.jsp"/>
</body>
</html>
