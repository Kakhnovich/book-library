var modal;
var email;
var borrowDate;
var duration;
var status;
var comment;

window.onload = function () {
    modal = document.getElementById('myModal');
    email = document.getElementsByName('email')[0];
    borrowDate = document.getElementsByName('borrowDate')[0];
    duration = document.getElementsByName('duration')[0];
    status = document.getElementsByName('status')[0];
    comment = document.getElementsByName('comment')[0];
}

function showModal(borrow) {
    modal.style.display = "block";
    document.body.setAttribute("borrowInfo", borrow);
}

function showNewModal() {
    email.selectedIndex=0;
    duration.selectedIndex=0;
    comment.value=0;
    console.log("showNewModal()");
    modal.style.display = "block";
}

function closeModal() {
    console.log("closeModal()");
    modal.style.display = "none";
}

function addTableRow() {
    let tableRef = document.getElementById("table");
    let newRowId = tableRef.lastElementChild["rows"].length+1;
    let newRow = tableRef.insertRow(newRowId);
    let newCell1 = newRow.insertCell(0);
    let newCell2 = newRow.insertCell(1);
    let newCell3 = newRow.insertCell(2);
    let newCell4 = newRow.insertCell(3);
    let newCell5 = newRow.insertCell(4);
    newCell1.innerHTML=email.value;
    let date = new Date;
    let dueDate = new Date;
    dueDate.setMonth(dueDate.getMonth() + Number(duration.value));
    if(borrowDate === undefined){
       new Date()
    } else {
        date=borrowDate.value;
    }
    newCell3.innerHTML=convertDate(date);
    newCell4.innerHTML=convertDate(dueDate);
    closeModal();
}

function convertDate(date){
    function format(m) {
        let f = new Intl.DateTimeFormat('en', m);
        return f.format(date);
    }
    let a = [{year: 'numeric'}, {month: 'numeric'}, {day: 'numeric'}];
    return a.map(format).join('-')
}