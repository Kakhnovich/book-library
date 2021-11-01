package com.itechart.studets_lab.book_library.model;

import java.time.LocalDate;
import java.util.Objects;

public class BorrowDto {
    private final int id;
    private final int bookId;
    private final Reader reader;
    private final LocalDate borrowDate;
    private final int duration;
    private final LocalDate returnDate;
    private final String comment;
    private final String status;

    public BorrowDto(int id, int bookId, Reader reader, LocalDate borrowDate, int duration, LocalDate returnDate, String comment, String status) {
        this.id = id;
        this.bookId = bookId;
        this.reader = reader;
        this.borrowDate = borrowDate;
        this.duration = duration;
        this.returnDate = returnDate;
        this.comment = comment;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getBookId() {
        return bookId;
    }

    public Reader getReader() {
        return reader;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public int getDuration() {
        return duration;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public String getComment() {
        return comment;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorrowDto borrow = (BorrowDto) o;
        return bookId == borrow.bookId && duration == borrow.duration && Objects.equals(reader, borrow.reader) && Objects.equals(borrowDate, borrow.borrowDate) && Objects.equals(returnDate, borrow.returnDate) && Objects.equals(comment, borrow.comment) && Objects.equals(status, borrow.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, reader, borrowDate, duration, returnDate, comment, status);
    }

    @Override
    public String toString() {
        return "{" +
                "id: " + id +
                ", bookId: " + bookId +
                ", reader: " + reader.toString() +
                ", borrowDate: " + borrowDate +
                ", duration: " + duration +
                ", returnDate: " + returnDate +
                ", comment: '" + comment + '\'' +
                ", status: '" + status + '\'' +
                '}';
    }
}
