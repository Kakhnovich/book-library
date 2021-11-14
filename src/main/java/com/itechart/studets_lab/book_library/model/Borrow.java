package com.itechart.studets_lab.book_library.model;

import java.time.LocalDate;
import java.util.Objects;

public class Borrow {
    private final int id;
    private final int bookId;
    private final int readerId;
    private final LocalDate borrowDate;
    private final int durationId;
    private final LocalDate returnDate;
    private final String comment;
    private final int statusId;

    Borrow(int id, int bookId, int readerId, LocalDate borrowDate, int durationId, LocalDate returnDate, String comment, int statusId) {
        this.id = id;
        this.bookId = bookId;
        this.readerId = readerId;
        this.borrowDate = borrowDate;
        this.durationId = durationId;
        this.returnDate = returnDate;
        this.comment = comment;
        this.statusId = statusId;
    }

    static class BorrowBuilder {
        private int id;
        private int bookId;
        private int readerId;
        private LocalDate borrowDate;
        private int durationId;
        private LocalDate returnDate;
        private String comment;
        private int statusId;

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

        public BorrowBuilder durationId(int durationId) {
            this.durationId = durationId;
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

        public BorrowBuilder statusId(int statusId) {
            this.statusId = statusId;
            return this;
        }

        public Borrow build() {
            return new Borrow(
                    this.id,
                    this.bookId,
                    this.readerId,
                    this.borrowDate,
                    this.durationId,
                    this.returnDate,
                    this.comment,
                    this.statusId);
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

    public int getDurationId() {
        return durationId;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public String getComment() {
        return comment;
    }

    public int getStatusId() {
        return statusId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Borrow borrow = (Borrow) o;
        return bookId == borrow.bookId && readerId == borrow.readerId && durationId == borrow.durationId && Objects.equals(borrowDate, borrow.borrowDate) && Objects.equals(returnDate, borrow.returnDate) && Objects.equals(comment, borrow.comment) && statusId == borrow.statusId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, readerId, borrowDate, durationId, returnDate, comment, statusId);
    }

    @Override
    public String toString() {
        return "{" +
                "id: " + id +
                ", bookId: " + bookId +
                ", reader: " + readerId +
                ", borrowDate: " + borrowDate +
                ", durationId: " + durationId +
                ", returnDate: " + returnDate +
                ", comment: '" + comment + '\'' +
                ", statusId: " + statusId +
                '}';
    }
}
