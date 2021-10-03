create database book_library;
use book_library;

create table book
(
    isbn int not null,
    cover varchar(255) null,
    title varchar(50) not null,
    publisher varchar(50) not null,
    publishDate date not null,
    pageCount int not null,
    description varchar(150) not null,
    totalAmount int not null,
    statusId int not null
);

create unique index book_isbn_uindex
    on book (isbn);

create unique index book_title_uindex
    on book (title);

alter table book
    add constraint book_pk
        primary key (isbn);

create table borrow_record
(
    bookId int not null,
    email varchar(50) not null,
    borrowDate date not null,
    timePeriodId int not null,
    returnDate date not null,
    comment varchar(150) null,
    statusId int not null,
    id int auto_increment,
    constraint borrow_record_pk
        primary key (id)
);

create table book_status
(
    id int auto_increment,
    status varchar(15) not null,
    constraint book_status_pk
        primary key (id)
);

create unique index book_status_status_uindex
    on book_status (status);

insert into book_status(status) values ('Unavailable'),('Available');

create table borrow_status
(
    id int auto_increment,
    status varchar(25) not null,
    constraint book_status_pk
        primary key (id)
);

create unique index borrow_status_status_uindex
    on borrow_status (status);

insert into borrow_status(status) values ('returned'),('returned and damaged'), ('lost');

create table borrow_period
(
    id int auto_increment,
    months int not null,
    constraint book_status_pk
        primary key (id)
);

create unique index borrow_period_months_uindex
    on borrow_period (months);

insert into borrow_period(months) values (1),(2),(3),(6),(12);

alter table book
    add constraint book_book_status_id_fk
        foreign key (statusId) references book_status (id);

alter table borrow_record
    add constraint borrow_record_borrow_status_id_fk
        foreign key (statusId) references borrow_status (id);

alter table borrow_record
    add constraint borrow_record_time_period_id_fk
        foreign key (timePeriodId) references time_period (id);

alter table borrow_record
    add constraint borrow_record_book_id_fk
        foreign key (bookId) references book (isbn);

create table reader
(
    firstName varchar(15) not null,
    lastName varchar(20) not null,
    email varchar(50) not null,
    gender varchar(15) not null,
    phoneNumber int null,
    registrationDate date not null,
    constraint reader_pk
        primary key (email)
);

alter table borrow_record
    add constraint borrow_record_reader_email_fk
        foreign key (email) references reader (email);

create table book_authors
(
    id int auto_increment,
    book_isbn int not null,
    author varchar(50) not null,
    constraint book_authors_pk
        primary key (id),
    constraint book_authors_book_isbn_fk
        foreign key (book_isbn) references book (isbn)
);

create table book_genres
(
    id int auto_increment,
    book_isbn int not null,
    genre varchar(30) not null,
    constraint book_genres_pk
        primary key (id),
    constraint book_genres_book_isbn_fk
        foreign key (book_isbn) references book (isbn)
);