package com.itechart.studets_lab.book_library.dao.impl;

public class BookGalleryDaoFactory {
    private static final BookGalleryDaoFactory INSTANCE = new BookGalleryDaoFactory();

    BookGalleryDaoFactory() {
    }

    public static BookGalleryDaoFactory getInstance() {
        return INSTANCE;
    }

    public BookGalleryDao getDao() {
        return new BookGalleryDao();
    }
}