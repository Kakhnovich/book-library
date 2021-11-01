package com.itechart.studets_lab.book_library.model;

import java.time.LocalDate;
import java.util.Objects;

public class Borrow {
    private final int id;
    private final int bookId;
    private final int readerId;
    private final LocalDate borrowDate;
    private final int duration;
    private final LocalDate returnDate;
    private final String comment;
    private final String status;

    Borrow(int id, int bookId, int readerId, LocalDate borrowDate, int duration, LocalDate returnDate, String comment, String status) {
        this.id = id;
        this.bookId = bookId;
        this.readerId = readerId;
        this.borrowDate = borrowDate;
        this.duration = duration;
        this.returnDate = returnDate;
        this.comment = comment;
        this.status = status;
    }

    static class BorrowBuilder {
        private int id;
        private int bookId;
        private int readerId;
        private LocalDate borrowDate;
        private int duration;
        private LocalDate returnDate;
        private String comment;
        private String status;

        public BorrowBuilder id(int id) {
            this.id = id;
            return this;
        }

        public BorrowBuilder bookId(int bookId) {
            this.bookId = bookId;
            return this;
        }

        public BorrowBuilder readerId(int readerId) {
            this.readerId = readerId;
            return this;
        }

        public BorrowBuilder borrowDate(LocalDate borrowDate) {
            this.borrowDate = borrowDate;
            return this;
        }

        public BorrowBuilder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public BorrowBuilder returnDate(LocalDate returnDate) {
            this.returnDate = returnDate;
            return this;
        }

        public BorrowBuilder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public BorrowBuilder status(String status) {
            this.status = status;
            return this;
        }

        public Borrow build() {
            return new Borrow(
                    this.id,
                    this.bookId,
                    this.readerId,
                    this.borrowDate,
                    this.duration,
                    this.returnDate,
                    this.comment,
                    this.status);
        }
    }

    static BorrowBuilder builder() {
        return new BorrowBuilder();
    }

    public int getId() {
        return id;
    }

    public int getBookId() {
        return bookId;
    }

    public int getReaderId() {
        return readerId;
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
        Borrow borrow = (Borrow) o;
        return bookId == borrow.bookId && duration == borrow.duration && readerId == borrow.readerId && Objects.equals(borrowDate, borrow.borrowDate) && Objects.equals(returnDate, borrow.returnDate) && Objects.equals(comment, borrow.comment) && Objects.equals(status, borrow.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, readerId, borrowDate, duration, returnDate, comment, status);
    }

    @Override
    public String toString() {
        return "{" +
                "id: " + id +
                ", bookId: " + bookId +
                ", reader: " + readerId +
                ", borrowDate: " + borrowDate +
                ", duration: " + duration +
                ", returnDate: " + returnDate +
                ", comment: '" + comment + '\'' +
                ", status: '" + status + '\'' +
                '}';
    }
}
