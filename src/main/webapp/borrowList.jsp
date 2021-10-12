<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Borrow List</title>
</head>
<body>
<div class="table-responsive">
    <table class="table table-striped table-sm" border="2">
        <thead>
        <tr>
            <th>Reader Email</th>
            <th>Reader Name</th>
            <th>Borrow Date</th>
            <th>Due Date</th>
            <th>Return Date</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="borrow" items="${requestScope.borrows}">
            <tr>
                <td>#{borrow.email}</td>
                <td></td>
                <td>#{borrow.borrowDate}</td>
                <td>${borrow.getDueDate()}</td>
                <td>${borrow.returnDate}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<jsp:include page="pagination.jsp"/>
</body>
</html>
