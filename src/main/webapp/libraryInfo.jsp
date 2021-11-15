<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Library Information Page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <link href="styles/style.css" rel="stylesheet">
</head>
<body>
<jsp:include page="commands.jsp"/>
<div class="container py-4">
    <form class="card p-2" method="post" action="${pageContext.request.contextPath}/controller?command=save_info">
        <p>Address: <input  class="form-control" type="text" name="address" required value="${address}"></p>
        <p>Name: <input  class="form-control" type="text" name="name" required value="${name}"></p>
        <p>Signature: <input  class="form-control" type="text" name="signature" required value="${signature}"></p>
        <p><input class="btn btn-secondary" type="submit" value="Update Information"></p>
    </form>
</div>
</body>
</html>
