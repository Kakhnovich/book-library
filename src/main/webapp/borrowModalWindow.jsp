<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Book Info</title>
    <script src="js/modal.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <link href="styles/style.css" rel="stylesheet">
</head>
<body>
<div id="myModal" class="modal">
    <div class="modal-content">
        <div class="modal-body">
            <p>Reader email address - <select required name="email">
                <c:forEach var="email" items="${emails}">
                    <option value="${email}">${email}</option>
                </c:forEach>
            </select></p>
            <%--                <p>Reader name - <input required name="name" disabled--%>
            <%--                                        value="${borrow.reader.firstName} ${borrow.reader.secondName}"></p>--%>
            <c:if test="${borrowInfo!=null}">
                <p>Borrow date - <input required name="borrowDate" type="date" value="${borrowInfo.borrowDate}"></p>
            </c:if>
            <p>Time period (months) - <select required name="duration">
                <c:forEach var="period" items="${periods}">
                    <option value="${period}">${period}</option>
                </c:forEach>
            </select></p>
            <c:if test="${borrowInfo.returnDate!=null}">
                <p>Status - <select required name="status">
                    <c:forEach var="status" items="${statuses}">
                        <option value="${status}">${status}</option>
                    </c:forEach>
                </select></p>
            </c:if>
            <p>Comment: <input name="comment" value="${borrowInfo.comment}"></p>
            <button onclick="addTableRow()">Save</button>
            <button onclick="closeModal()">Discard</button>
        </div>
    </div>
</div>
</body>
</html>
