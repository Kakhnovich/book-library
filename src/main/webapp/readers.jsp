<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Readers Page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <link href="styles/style.css" rel="stylesheet">
</head>
<body>
<jsp:include page="commands.jsp"/>
<c:set var="rowNumber" value="0"/>
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
                <tr onclick="showModal(${rowNumber})">
                    <td class="hidden">${reader.id}</td>
                    <td class="hidden">${reader.firstName}</td>
                    <td class="hidden">${reader.lastName}</td>
                    <td>${reader.firstName} ${reader.lastName}</td>
                    <td>${reader.email}</td>
                    <td>${reader.dateOfRegistration}</td>
                    <td class="hidden">${reader.gender}</td>
                    <td><c:if test="${reader.phoneNumber!=0}">${reader.phoneNumber}</c:if></td>
                </tr>
                <c:set var="rowNumber" value="${rowNumber + 1}"/>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <jsp:include page="pagination.jsp"/>
    <jsp:include page="readerModalWindow.jsp"/>
    <button onclick="showNewModal()">Add reader</button>
</div>
</body>
</html>
