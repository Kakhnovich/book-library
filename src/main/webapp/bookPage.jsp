<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Book Page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <link href="styles/style.css" rel="stylesheet">
    <script src="js/createBook.js"></script>
</head>
<body>
<jsp:include page="commands.jsp"/>
<div class="container py-4">
    <c:if test="${book!=null}">
        <div class="hidden" id="bookId">${book.id}</div>
        <div class="line">
            <div class="imgColumn">
                <img src="img/${book.coverLink}" alt="${book.title} cover" accept="image/png, image/jpg"/>
                <div class="imgButtons">
                    <button class="btn btn-outline-primary" id="previousButton" onclick="previousImage()"><</button>
                    <button class="btn btn-outline-primary" id="nextButton" onclick="nextImage()">></button>
                </div>
            </div>
            <div class="column">
                <div class="bordered">
                    <p>Change Book Cover:</p>
                    <form method="post"
                          action="${pageContext.request.contextPath}/upload?command=cover&bookId=${book.id}"
                          enctype="multipart/form-data">
                        <div>
                            <input type="file" name="photo">
                            <input type="submit" value="update cover">
                        </div>
                    </form>
                </div>
                <div class="bordered">
                    <p>Add new Book Photo in gallery:</p>
                    <form method="post"
                          action="${pageContext.request.contextPath}/upload?command=gallery&bookId=${book.id}"
                          enctype="multipart/form-data">
                        <div>
                            <input type="file" name="photo">
                            <input id="addPhotoButton" type="submit" value="add photo">
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <datalist id="photos">
            <c:forEach var="photo" items="${photos}">
                <option value="${photo}">${photo}</option>
            </c:forEach>
        </datalist>
    </c:if>
    <form id="form" method="post"
          action="${pageContext.request.contextPath}/controller?command=update_book&bookId=${book.id}">
        <h4><input required value="${book.title}" name="title" accept=".jpg, .png"> Book information page</h4>
        <p>Author<c:if test="${book.authors.size()>1}">s</c:if>:
            <input class="form-control" required value="${authors}" name="authors">
        </p>
        <p>Book Publisher - <input class="form-control" required value="${book.publisher}" name="publisher"></p>
        <p>Publish Date - <input class="form-control" required type="date" value="${book.publishDate}"
                                 name="publishDate"></p>
        <p>Genre<c:if test="${book.genres.size()>1}">s</c:if>:
            <input class="form-control" required value="${genres}" name="genres">
        </p>
        <p>Page count - <input class="form-control" required type="number" min="1" max="1000" value="${book.pageCount}"
                               name="pageCount"></p>
        <p>ISBN - <input class="form-control" required type="number" min="1" max="1_000_000_000" value="${book.isbn}"
                         name="isbn">
        </p>
        <p>Description: <input class="form-control" value="${book.description}" name="description"></p>
        <c:choose>
        <c:when test="${book==null}">
        <p>Total amount - <input class="form-control" required type="number" min="1" max="100" name="totalAmount"></p>
        <div><input class="btn btn-secondary" type="submit" value="Add">
            </c:when>
            <c:otherwise>

            <c:choose>
            <c:when test="${availableCount!=null && availableCount>0}">
            <p class="inline">Available (</p>
            <p class="inline" id="availableCount">${availableCount}</p>
            <p class="inline"> out of <input class="form-control" required type="number"
                                             min="${book.totalAmount - availableCount}" max="100"
                                             value="${book.totalAmount}" name="totalAmount">)</p>
            </c:when>
            <c:otherwise>
            <p class="hidden" id="availableCount">${availableCount}</p>
            <p>Total amount - <input class="form-control" required type="number"
                                     min="${book.totalAmount - availableCount}" max="100"
                                     value="${book.totalAmount}"
                                     name="totalAmount"></p>
            <p>Unavailable (expected to become available on ${availableDate})</p>
            </c:otherwise>
            </c:choose>
    </form>
    <div>
        <button class="btn btn-secondary" onclick="saveBookInfo()">Save</button>
        </c:otherwise>
        </c:choose>
        <button class="btn btn-secondary" onclick="window.location.href='/'">Discard</button>
    </div>
    <div class="error"><p>${errorMsg}</p></div>
    <div class="error"><p>${bookErrorMsg}</p></div>
    <div>
        <c:if test="${book.id!=null}">
            <jsp:include page="borrowList.jsp"/>
            <jsp:include page="borrowModalWindow.jsp"/>
            <div <c:if test="${availableCount==0}"> class="hidden" </c:if>>
                <button class="btn btn-secondary" id="addButton" onclick="showNewModal()">Add borrow</button>
            </div>
        </c:if>
    </div>
    <div class="error"><p>${borrowsErrorMsg}</p></div>
</div>
</body>
</html>
