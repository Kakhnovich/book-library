<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Search Page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
</head>
<body>
<jsp:include page="commands.jsp"/>
<div class="container py-4">
    <form class="card p-2" method="post" action="${pageContext.request.contextPath}/controller?command=find_book">
        <input class="form-control" name="title" placeholder="title">
        <input class="form-control" name="authors" placeholder="author(-s)">
        <input class="form-control" name="genres" placeholder="genre(-s)">
        <input class="form-control" name="description" placeholder="title">
        <input class="btn btn-secondary" type="submit" value="search">
    </form>
    <c:if test="${not empty requestScope.books}">
        <jsp:include page="bookList.jsp"/>
    </c:if>
</div>
</body>
</html>
