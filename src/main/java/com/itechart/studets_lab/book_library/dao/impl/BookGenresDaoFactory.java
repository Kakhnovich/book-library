package com.itechart.studets_lab.book_library.dao.impl;

public class BookGenresDaoFactory {
    private static final BookGenresDaoFactory INSTANCE = new BookGenresDaoFactory();

    BookGenresDaoFactory() {
    }

    public static BookGenresDaoFactory getInstance() {
        return INSTANCE;
    }

    public BookGenresDao getDao() {
        return new BookGenresDao();
    }
}
