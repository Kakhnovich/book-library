<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Book Page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
</head>
<body>
<jsp:include page="commands.jsp"/>
<div class="container py-4">
    <c:if test="${book!=null}">
        <div class="hidden" id="bookId">${book.id}</div>
        <form method="post" action="${pageContext.request.contextPath}/upload?bookId=${book.id}"
              enctype="multipart/form-data">
            <div>
                <img src="${book.coverLink}" alt="${book.title} cover"/>
                <input type="file" name="cover">
                <input type="submit" value="update cover">
            </div>
        </form>
    </c:if>
    <form id="form" method="post"
          action="${pageContext.request.contextPath}/controller?command=update_book&bookId=${book.id}">
        <h4><input required value="${book.title}" name="title" accept=".jpg, .png"> Book information page</h4>
        <p>Author<c:if test="${book.authors.size()>1}">s</c:if>:
            <input required value="${authors}" name="authors">
        </p>
        <p>Book Publisher - <input required value="${book.publisher}" name="publisher"></p>
        <p>Publish Date - <input required type="date" value="${book.publishDate}" name="publishDate"></p>
        <p>Genre<c:if test="${book.genres.size()>1}">s</c:if>:
            <input required value="${genres}" name="genres">
        </p>
        <p>Page count - <input required type="number" min="1" max="1000" value="${book.pageCount}" name="pageCount"></p>
        <p>ISBN - <input required type="number" min="1" max="1_000_000_000_000_000" value="${book.isbn}" name="isbn">
        </p>
        <p>Description: <input value="${book.description}" name="description"></p>
        <c:choose>
        <c:when test="${book==null}">
        <p>Total amount - <input required type="number" min="1" max="100" name="totalAmount"></p>
        <div><input type="submit" value="Add">
            </c:when>
            <c:otherwise>

            <c:choose>
            <c:when test="${availableCount!=null && availableCount>0}">
            <p class="inline">Available (</p>
            <p class="inline" id="availableCount">${availableCount}</p>
            <p class="inline"> out of <input required type="number" min="${book.totalAmount - availableCount}" max="100"
                                             value="${book.totalAmount}" name="totalAmount">)</p>
            </c:when>
            <c:otherwise>
            <p>Total amount - <input required type="number" min="${book.totalAmount - availableCount}" max="100"
                                     value="${book.totalAmount}"
                                     name="totalAmount"></p>
            <p>Unavailable (expected to become available on ${availableDate})</p>
            </c:otherwise>
            </c:choose>
    </form>
    <div>
        <button onclick="saveBookInfo()">Save</button>
        </c:otherwise>
        </c:choose>
        <button onclick="window.location.href='/'">Discard</button>
    </div>
    <div>
        <c:if test="${book.id!=null}">
            <jsp:include page="borrowList.jsp"/>
            <jsp:include page="borrowModalWindow.jsp"/>
            <c:if test="${availableCount>0}">
                <button id="addButton" onclick="showNewModal()">Add borrow</button>
            </c:if>
        </c:if>
    </div>
</div>
</body>
</html>
