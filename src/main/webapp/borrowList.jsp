<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Borrow List</title>
    <script src="js/modal.js"></script>
</head>
<body>
<c:if test="${not empty borrows}">
    <c:set var="rowNumber" value="0"/>
    <div class="table-responsive">
        <table id="table" class="table table-striped table-sm" border="2">
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
                    <td class="hidden">${borrow.id}</td>
                    <td>${borrow.reader.email}</td>
                    <td><c:choose>
                        <c:when test="${borrow.status=='not returned'}"><a href="#" onclick="showModal(${rowNumber})">
                                ${borrow.reader.firstName} ${borrow.reader.lastName}</a></c:when>
                        <c:otherwise>${borrow.reader.firstName} ${borrow.reader.lastName}</c:otherwise>
                    </c:choose></td>
                    <td>${borrow.borrowDate}</td>
                    <td>${borrow.borrowDate.plusMonths(borrow.duration)}</td>
                    <td>${borrow.returnDate}</td>
                    <td class="hidden">${borrow.comment}</td>
                    <td class="hidden">${borrow.status}</td>
                </tr>
                <c:set var="rowNumber" value="${rowNumber + 1}"/>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>
</body>
</html>
