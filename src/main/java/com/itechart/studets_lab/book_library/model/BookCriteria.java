package com.itechart.studets_lab.book_library.model;

import java.util.ArrayList;
import java.util.List;

public class BookCriteria {
    private final String title;
    private final List<String> authors;
    private final List<String> genres;
    private final String description;

    private BookCriteria(String title, List<String> authors, List<String> genres, String description) {
        this.title = title;
        this.authors = authors;
        this.genres = genres;
        this.description = description;
    }

    public static class CriteriaBuilder {
        private String title = "";
        private List<String> authors = new ArrayList<>();
        private List<String> genres = new ArrayList<>();
        private String description = "";

        public CriteriaBuilder title(String title) {
            this.title = title;
            return this;
        }

        public CriteriaBuilder authors(List<String> authors) {
            this.authors = authors;
            return this;
        }

        public CriteriaBuilder genres(List<String> genres) {
            this.genres = genres;
            return this;
        }

        public CriteriaBuilder description(String description) {
            this.description = description;
            return this;
        }

        public BookCriteria build() {
            return new BookCriteria(
                    this.title,
                    this.authors,
                    this.genres,
                    this.description);
        }
    }

    public static CriteriaBuilder builder() {
        return new CriteriaBuilder();
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public List<String> getGenres() {
        return genres;
    }

    public String getDescription() {
        return description;
    }
}
