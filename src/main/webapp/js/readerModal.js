let readerModal;
let tableRef;
let readerId;
let firstName;
let lastName;
let email;
let maleButton;
let femaleButton;
let phoneNumber;

window.onload = function () {
    readerModal = document.getElementById("myModal");
    tableRef = document.getElementsByClassName("table")[0];
    readerId = document.getElementById("readerId");
    firstName = document.getElementById("firstName");
    lastName = document.getElementById("lastName");
    email = document.getElementById("email");
    maleButton = document.getElementById("male");
    femaleButton = document.getElementById("female");
    phoneNumber = document.getElementById("phoneNumber");
}

function showModal(rowNumber) {
    let row = tableRef.lastElementChild.rows[rowNumber];
    readerId.value = row.cells[0].innerText;
    firstName.value = row.cells[1].innerText;
    lastName.value = row.cells[2].innerText;
    email.value = row.cells[4].innerText;
    phoneNumber.value = row.cells[7].innerText;
    if (row.cells[6].innerText === 'male') {
        maleButton.checked = true;
    } else if (row.cells[6].innerText === 'female') {
        femaleButton.checked = true;
    }
    console.log("showModal()");
    readerModal.style.display = "block";
}

function showNewModal() {
    readerId.value = 0;
    firstName.value = "";
    lastName.value = "";
    email.value = "";
    phoneNumber.value = "";
    maleButton.checked = false;
    femaleButton.checked = false;
    console.log("showNewModal()");
    readerModal.style.display = "block";
}

function closeModal() {
    console.log("closeModal()");
    readerModal.style.display = "none";
}