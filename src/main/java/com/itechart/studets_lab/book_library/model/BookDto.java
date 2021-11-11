package com.itechart.studets_lab.book_library.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class BookDto {
    private final int id;
    private final int isbn;
    private String coverLink;
    private final String title;
    private final List<String> authors;
    private final String publisher;
    private final LocalDate publishDate;
    private final List<String> genres;
    private final int pageCount;
    private final String description;
    private int totalAmount;

    public BookDto(int id, int isbn, String coverLink, String title, List<String> authors, String publisher, LocalDate publishDate, List<String> genres, int pageCount, String description, int totalAmount) {
        this.id = id;
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

    public void setCoverLink(String coverLink) {
        this.coverLink = coverLink;
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

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDto bookDto = (BookDto) o;
        return id == bookDto.id && isbn == bookDto.isbn && pageCount == bookDto.pageCount && totalAmount == bookDto.totalAmount && Objects.equals(coverLink, bookDto.coverLink) && Objects.equals(title, bookDto.title) && Objects.equals(authors, bookDto.authors) && Objects.equals(publisher, bookDto.publisher) && Objects.equals(publishDate, bookDto.publishDate) && Objects.equals(genres, bookDto.genres) && Objects.equals(description, bookDto.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isbn, coverLink, title, authors, publisher, publishDate, genres, pageCount, description, totalAmount);
    }
}
