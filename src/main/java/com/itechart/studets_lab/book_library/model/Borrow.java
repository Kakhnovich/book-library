package com.itechart.studets_lab.book_library.model;

import java.time.LocalDate;
import java.util.Objects;

public class Borrow {
    private final int id;
    private final int bookId;
    private final String email;
    private final LocalDate borrowDate;
    private final int duration;
    private final LocalDate returnDate;
    private final String comment;
    private final String status;

    Borrow(int id, int bookId, String email, LocalDate borrowDate, int duration, LocalDate returnDate, String comment, String status) {
        this.id = id;
        this.bookId = bookId;
        this.email = email;
        this.borrowDate = borrowDate;
        this.duration = duration;
        this.returnDate = returnDate;
        this.comment = comment;
        this.status = status;
    }

    static class BorrowBuilder {
        private int id;
        private int bookId;
        private String email;
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

        public BorrowBuilder email(String email) {
            this.email = email;
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
                    id, this.bookId,
                    this.email,
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

    public String getEmail() {
        return email;
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
        return bookId == borrow.bookId && duration == borrow.duration && Objects.equals(email, borrow.email) && Objects.equals(borrowDate, borrow.borrowDate) && Objects.equals(returnDate, borrow.returnDate) && Objects.equals(comment, borrow.comment) && Objects.equals(status, borrow.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, email, borrowDate, duration, returnDate, comment, status);
    }
}
