let form;
let borrowModal;
let statusField;
let email;
let dataList;
let name;
let duration;
let status;
let comment;
let tableDiv;
let tableRef;
let borrows = [];
let bookId;
let borrowId;
let actualRow = 0;
let totalAmount;
let availableCount;
let addButton;
let img = document.getElementsByTagName('img')[0];
img.onerror = () => {
    img.src = '/img/default.jpg';
}

window.onload = function () {
    form = document.getElementById('form');
    borrowModal = document.getElementById('myModal');
    statusField = document.getElementById('status');
    email = document.getElementById('email');
    dataList = email.list;
    dataList.id = "anyId";
    email.addEventListener('keyup', () => setSearchList());
    name = document.getElementById('name');
    duration = document.getElementsByName('duration')[0];
    status = document.getElementsByName('status')[0];
    comment = document.getElementsByName('comment')[0];
    bookId = Number(document.getElementById('bookId').textContent);
    borrowId = document.getElementById("borrowId");
    totalAmount = document.getElementsByName("totalAmount")[0];
    availableCount = document.getElementById('availableCount');
    addButton = document.getElementById("addButton");
    tableDiv = document.getElementById("tableDiv");
    tableRef = document.getElementsByClassName("table")[0];
    for (let row of tableRef.lastElementChild.rows) {
        addBorrow(row);
    }
    setMaxPublishDate();
}

function addBorrow(row) {
    let borrowId = Number(row.cells[0].textContent);
    let readerEmail = row.cells[1].textContent;
    let readerName = row.cells[2].textContent;
    let borrowDate = new Date(row.cells[3].textContent);
    let dueDate = new Date(row.cells[4].textContent);
    let duration = (dueDate.getMonth() - borrowDate.getMonth()) + (dueDate.getFullYear() - borrowDate.getFullYear()) * 12;
    let returnDate = row.cells[5].textContent;
    if (returnDate !== '') {
        returnDate = convertDate(new Date(returnDate));
    }
    let comment = row.cells[6].textContent;
    let status = row.cells[7].textContent;
    let borrow = {
        id: borrowId,
        bookId: bookId,
        readerEmail: readerEmail,
        readerName: readerName,
        borrowDate: convertDate(borrowDate),
        duration: duration,
        returnDate: returnDate,
        comment: comment,
        status: status
    };
    borrows.push(borrow);
}

function showModal(rowNumber) {
    actualRow = rowNumber;
    borrowId.innerText = borrows[rowNumber].id;
    email.value = borrows[rowNumber].readerEmail;
    email.readOnly = true;
    name.value = borrows[rowNumber].readerName.trim();
    name.readOnly = true;
    status.value = borrows[rowNumber].status;
    duration.value = borrows[rowNumber].duration;
    duration.disabled = true;
    comment.value = borrows[rowNumber].comment;
    console.log("showModal()");
    statusField.style.display = 'block';
    borrowModal.style.display = "block";
}

function showNewModal() {
    borrowId.innerText = '';
    email.value = '';
    email.readOnly = false;
    name.value = '';
    name.readOnly = false;
    status.selectedIndex = 0;
    duration.selectedIndex = 0;
    duration.disabled = false;
    comment.value = '';
    console.log("showNewModal()");
    statusField.style.display = 'none';
    borrowModal.style.display = "block";
}

function closeModal() {
    console.log("closeModal()");
    borrowModal.style.display = "none";
}

function addTableRow() {
    tableDiv.style.display = "block";
    let newCell1;
    let newCell2;
    let newCell3;
    let newCell4;
    let newCell5;
    if (borrowId.innerText === '') {
        let newRowId = tableRef.lastElementChild["rows"].length + 1;
        actualRow = newRowId - 1;
        let newRow = tableRef.insertRow(newRowId);
        newCell1 = newRow.insertCell(0);
        let newHiddenCell = newRow.insertCell(1);
        newHiddenCell.style.display = 'none';
        newCell2 = newRow.insertCell(2);
        newCell3 = newRow.insertCell(3);
        newCell4 = newRow.insertCell(4);
        newCell5 = newRow.insertCell(5);
    } else {
        newCell1 = tableRef.lastElementChild["rows"][actualRow].cells[1];
        newCell2 = tableRef.lastElementChild["rows"][actualRow].cells[2];
        newCell3 = tableRef.lastElementChild["rows"][actualRow].cells[3];
        newCell4 = tableRef.lastElementChild["rows"][actualRow].cells[4];
        newCell5 = tableRef.lastElementChild["rows"][actualRow].cells[5];
    }
    newCell1.innerHTML = email.value;
    if (borrowId.innerText === '' || status.value === 'not returned') {
        newCell2.innerHTML = '<a href="#" onclick="showModal(' + actualRow + ')">' + name.value + '</a>'
    } else {
        newCell2.innerHTML = name.value;
    }
    let dueDate = new Date;
    dueDate.setMonth(dueDate.getMonth() + Number(duration.value));
    newCell3.innerHTML = convertDate(new Date);
    newCell4.innerHTML = convertDate(dueDate);
    if (borrowId.innerText !== '' && status.value !== 'not returned') {
        newCell5.innerText = convertDate(new Date());
    }
    let borrow = {
        id: Number(borrowId.innerText),
        bookId: bookId,
        readerEmail: newCell1.innerText,
        readerName: newCell2.innerText,
        borrowDate: newCell3.innerText,
        duration: Number(duration.value),
        status: borrowId.innerText === '' ? 'not returned' : status.value,
        returnDate: newCell5.innerText,
        comment: comment.value
    }
    if (borrowId.innerText !== '') {
        borrows[actualRow] = borrow;
        if (borrow.status !== 'not returned') {
        }
    } else {
        borrows.push(borrow);
    }
    let activeBorrowsCount = 0;
    for (let borrow of borrows) {
        if (borrow.status === 'not returned') {
            activeBorrowsCount++;
        }
    }
    if (totalAmount.value < activeBorrowsCount) {
        totalAmount.value = activeBorrowsCount;
    }
    availableCount.innerText = (Number(totalAmount.value) - activeBorrowsCount);
    totalAmount.min = activeBorrowsCount;
    if (activeBorrowsCount >= Number(totalAmount.value)) {
        addButton.style.display = 'none';
    } else {
        addButton.style.display = 'block';
    }
    closeModal();
}

function convertDate(date) {
    function format(m) {
        let f = new Intl.DateTimeFormat('en', m);
        return f.format(date);
    }

    let a = [{year: 'numeric'}, {month: '2-digit'}, {day: '2-digit'}];
    return a.map(format).join('-')
}

function selectReader() {
    let hasChanged = false;
    for (let i = 0; i < email.list.options.length; i++) {
        if (email.value === email.list.options[i].value) {
            name.value = email.list.options[i].innerText;
            hasChanged = true;
        }
    }
    if (!hasChanged) {
        name.value = '';
    }
}

function saveBookInfo() {
    let input = document.createElement('input');
    input.style.display = 'none';
    input.setAttribute('name', 'borrows');
    borrows.forEach(borrow => borrow.toString = function toStr() {
        return borrow.id + ';' + borrow.bookId + ';' + borrow.readerEmail + ';' + borrow.readerName + ';' +
            borrow.borrowDate + ';' + borrow.duration + ';' + borrow.returnDate + ';' + borrow.comment + ';' + borrow.status;
    });
    input.setAttribute('value', borrows.toString());
    form.appendChild(input);
    form.submit();
}

function setSearchList() {
    if (email.value.length < 3) {
        dataList.id = "anyId";
    } else {
        dataList.id = "emails";
    }
}