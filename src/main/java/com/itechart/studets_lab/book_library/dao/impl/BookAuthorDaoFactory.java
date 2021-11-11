package com.itechart.studets_lab.book_library.dao.impl;

public class BookAuthorDaoFactory {
    private static final BookAuthorDaoFactory INSTANCE = new BookAuthorDaoFactory();

    BookAuthorDaoFactory() {
    }

    public static BookAuthorDaoFactory getInstance() {
        return INSTANCE;
    }

    public BookAuthorDao getDao() {
        return new BookAuthorDao();
    }
}
