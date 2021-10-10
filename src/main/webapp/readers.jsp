<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Readers Page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
</head>
<body>
<jsp:include page="commands.jsp"/>
<div class="container py-4">
    <div class="table-responsive">
        <table class="table table-striped table-sm" border="2">
            <thead>
            <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Date of registration</th>
                <th>Phone number</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="reader" items="${requestScope.readers}">
                <tr>
                    <td>${reader.name}</td>
                    <td>${reader.email}</td>
                    <td>${reader.dateOfRegistration}</td>
                    <td>${reader.phoneNumber}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <jsp:include page="pagination.jsp"/>
</div>
</body>
</html>
