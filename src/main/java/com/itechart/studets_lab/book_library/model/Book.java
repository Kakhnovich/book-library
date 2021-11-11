package com.itechart.studets_lab.book_library.model;

import java.time.LocalDate;
import java.util.Objects;

public class Book {
    private final int id;
    private final int isbn;
    private final String coverLink;
    private final String title;
    private final String publisher;
    private final LocalDate publishDate;
    private final int pageCount;
    private final String description;
    private final int totalAmount;

    Book(int id, int isbn, String coverLink, String title, String publisher, LocalDate publishDate, int pageCount, String description, int totalAmount) {
        this.id = id;
        this.isbn = isbn;
        this.coverLink = coverLink;
        this.title = title;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.pageCount = pageCount;
        this.description = description;
        this.totalAmount = totalAmount;
    }

    static class BookBuilder {
        private int id;
        private int isbn;
        private String coverLink;
        private String title;
        private String publisher;
        private LocalDate publishDate;
        private int pageCount;
        private String description;
        private int totalAmount;

        public BookBuilder id(int id) {
            this.id = id;
            return this;
        }

        public BookBuilder isbn(int isbn) {
            this.isbn = isbn;
            return this;
        }

        public BookBuilder coverLink(String coverLink) {
            this.coverLink = coverLink;
            return this;
        }

        public BookBuilder title(String title) {
            this.title = title;
            return this;
        }

        public BookBuilder publisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        public BookBuilder publishDate(LocalDate publishDate) {
            this.publishDate = publishDate;
            return this;
        }

        public BookBuilder pageCount(int pageCount) {
            this.pageCount = pageCount;
            return this;
        }

        public BookBuilder description(String description) {
            this.description = description;
            return this;
        }

        public BookBuilder totalAmount(int totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Book build() {
            return new Book(
                    this.id,
                    this.isbn,
                    this.coverLink,
                    this.title,
                    this.publisher,
                    this.publishDate,
                    this.pageCount,
                    this.description,
                    this.totalAmount);
        }
    }

    static BookBuilder builder() {
        return new BookBuilder();
    }

    public int getId() {
        return id;
    }

    public int getIsbn() {
        return isbn;
    }

    public String getCoverLink() {
        return coverLink;
    }

    public String getTitle() {
        return title;
    }


    public String getPublisher() {
        return publisher;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public int getPageCount() {
        return pageCount;
    }

    public String getDescription() {
        return description;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && isbn == book.isbn && pageCount == book.pageCount && totalAmount == book.totalAmount && Objects.equals(coverLink, book.coverLink) && Objects.equals(title, book.title) && Objects.equals(publisher, book.publisher) && Objects.equals(publishDate, book.publishDate) && Objects.equals(description, book.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isbn, coverLink, title, publisher, publishDate, pageCount, description, totalAmount);
    }
}
