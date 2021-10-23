var modal;

window.onload = function () {
    modal = document.getElementById('myModal');
}

function showModal(borrow) {
    modal.style.display = "block";
    document.body.setAttribute("borrowInfo", borrow);
    window.opener.location.reload();
    window.close();
}

function showNewModal(){
    console.log("showNewModal()");
    modal.style.display = "block";
}

function closeModal() {
    console.log("closeModal()");
    modal.style.display = "none";
}