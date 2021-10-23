<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <div class="collapse navbar-collapse">
            <div class="navbar-nav me-auto mb-2 mb-lg-0">
                <div class="nav-item">
                    <c:choose>
                        <c:when test="${command!='default' && command!='update_book'}">
                            <a class="nav-link"
                               href=${pageContext.request.contextPath}/controller?command=main>Main
                                Page</a>
                        </c:when>
                        <c:otherwise>
                            <div class="selected">Main Page</div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="nav-item">
                    <c:choose>
                        <c:when test="${command!='readers'}">
                            <div class="nav-item"><a class="nav-link"
                                                     href=${pageContext.request.contextPath}/controller?command=readers>Readers
                                Page</a></div>
                        </c:when>
                        <c:otherwise>
                            <div class="selected">Readers Page</div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="nav-item">
                    <c:choose>
                        <c:when test="${command!='search' && command!='find_book'}">
                            <div class="nav-item"><a class="nav-link"
                                                     href=${pageContext.request.contextPath}/controller?command=search>Search
                                Page</a></div>
                        </c:when>
                        <c:otherwise>
                            <div class="selected">Search Page</div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <%--    <c:if test="${command!='info'}">--%>
                <%--        <div class="nav-item"><a class="nav-link" href=${pageContext.request.contextPath}/controller?command=info>Information Page</a></div>--%>
                <%--    </c:if>--%>
            </div>
        </div>
    </div>
</nav>
</body>
</html>
