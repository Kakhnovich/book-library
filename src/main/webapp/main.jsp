<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Main Page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <script src="js/borrowModal.js"></script>
</head>
<body>
<jsp:include page="commands.jsp"/>
<div class="container py-4">
    <c:choose>
        <c:when test="${!sort.equals('accept')}">
            <form method="post" action="${pageContext.request.contextPath}/controller?sort=accept">
                <button type="submit">Show only available now books</button>
            </form>
        </c:when>
        <c:otherwise>
            <form method="post" action="${pageContext.request.contextPath}/controller?command=main">
                <button type="submit">Show all books</button>
            </form>
        </c:otherwise>
    </c:choose>
    <jsp:include page="bookList.jsp"/>
    <jsp:include page="pagination.jsp"/>
    <a href="${pageContext.request.contextPath}/controller?command=book">Add</a>
</div>
</body>
</html>