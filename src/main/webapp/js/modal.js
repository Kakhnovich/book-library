var modal;

window.onload = function () {
    modal = document.getElementById('myModal');
}

function setBook(book) {
    modal.style.display = "block";
    document.body.setAttribute("selectedBook", book);
}

function closeModal() {
    modal.style.display = "none";
}