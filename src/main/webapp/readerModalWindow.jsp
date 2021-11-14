<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Reader</title>
    <script src="js/readerModal.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <link href="styles/style.css" rel="stylesheet">
</head>
<body>
<div id="myModal" class="modal">
    <div class="modal-content">
        <div class="modal-body">
            <form method="post" action="${pageContext.request.contextPath}/controller?command=save_reader">
            <input id="readerId" class="hidden" type="number" name="id">
            <p>First Name - <input id="firstName" required name="firstName"></p>
            <p>Last Name - <input id="lastName" required name="lastName"></p>
            <p>Email - <input id="email" type="email" required name="email"></p>
            <p>Gender: <input id="male" type="radio" name="gender" required value="male"> male <input id="female" type="radio" name="gender" required value="female"> female</p>
            <p>Phone number: <input id="phoneNumber" type="number" name="phone"></p>
            <input type="submit" value="Save">
            </form>
            <button onclick="closeModal()">Discard</button>
        </div>
    </div>
</div>
</body>
</html>
