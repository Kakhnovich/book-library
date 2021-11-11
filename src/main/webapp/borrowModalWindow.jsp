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
            <p class="hidden" id="borrowId"></p>
            <p>Reader email address - <input type="search" onchange="selectReader()" id="email" required name="email"
                                             list="emails" autocomplete="off">
                <datalist id="emails">
                    <c:forEach var="reader" items="${emailsMap}">
                        <option value="${reader.key}">${reader.value}</option>
                    </c:forEach>
                </datalist>
            </p>
            <p>Reader name - <input id="name" required name="name"></p>
            <p>Time period (months) - <select required name="duration">
                <c:forEach var="period" items="${periods}">
                    <option value="${period}">${period}</option>
                </c:forEach>
            </select></p>
            <p id="status">Status - <select required name="status">
                <c:forEach var="status" items="${statuses}">
                    <option value="${status}">${status}</option>
                </c:forEach>
            </select></p>
            <p>Comment: <input name="comment"></p>
            <button onclick="addTableRow()">Save</button>
            <button onclick="closeModal()">Discard</button>
        </div>
    </div>
</div>
</body>
</html>
