package by.itechart.studets_lab.book_library.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Book {
    private final int isbn;
    private String coverLink;
    private final String title;
    private final List<String> authors;
    private final String publisher;
    private final LocalDate publishDate;
    private final List<String> genres;
    private final int pageCount;
    private final String description;
    private final int totalAmount;
    private String status;

    Book(int isbn, String coverLink, String title, List<String> authors, String publisher, LocalDate publishDate, List<String> genres, int pageCount, String description, int totalAmount, String status) {
        this.isbn = isbn;
        this.coverLink = coverLink;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.genres = genres;
        this.pageCount = pageCount;
        this.description = description;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    static class BookBuilder {
        private int isbn;
        private String coverLink;
        private String title;
        private List<String> authors;
        private String publisher;
        private LocalDate publishDate;
        private List<String> genres;
        private int pageCount;
        private String description;
        private int totalAmount;
        private String status;

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

        public BookBuilder authors(List<String> authors) {
            this.authors = authors;
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

        public BookBuilder genres(List<String> genres) {
            this.genres = genres;
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

        public BookBuilder status(String status) {
            this.status = status;
            return this;
        }

        public Book build() {
            return new Book(
                    this.isbn,
                    this.coverLink,
                    this.title,
                    this.authors, this.publisher,
                    this.publishDate,
                    this.genres, this.pageCount,
                    this.description,
                    this.totalAmount,
                    this.status);
        }
    }

    static BookBuilder builder() {
        return new BookBuilder();
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

    public List<String> getAuthors() {
        return authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public List<String> getGenres() {
        return genres;
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

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return isbn == book.isbn && pageCount == book.pageCount && totalAmount == book.totalAmount && Objects.equals(coverLink, book.coverLink) && Objects.equals(title, book.title) && Objects.equals(authors, book.authors) && Objects.equals(publisher, book.publisher) && Objects.equals(publishDate, book.publishDate) && Objects.equals(genres, book.genres) && Objects.equals(description, book.description) && Objects.equals(status, book.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, coverLink, title, authors, publisher, publishDate, genres, pageCount, description, totalAmount, status);
    }
}
