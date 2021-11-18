<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="styles/style.css" rel="stylesheet" type="text/css">
    <title>Error page</title>
</head>
<body>
<jsp:include page="/commands.jsp"/>
<div class="container py-4">
    <h2>Error code: 500</h2>
    <br/>
    Exception: ${pageContext.exception}
    <br/>
    <br/>
    Message from exception: ${pageContext.exception.message}
</div>
</body>
</html>
