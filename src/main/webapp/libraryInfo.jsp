<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Library Information Page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
</head>
<body>
<jsp:include page="commands.jsp"/>
<div class="container py-4">
    <form method="post" action="${pageContext.request.contextPath}/controller?command=save_info">
        Address: <input type="text" name="address" required value="${address}">
        Name: <input type="text" name="name" required value="${name}">
        Signature: <input type="text" name="signature" required value="${signature}">
        <input type="submit" value="Update Information">
    </form>
</div>
</body>
</html>
