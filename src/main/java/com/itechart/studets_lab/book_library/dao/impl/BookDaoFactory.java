package com.itechart.studets_lab.book_library.dao.impl;

import com.itechart.studets_lab.book_library.dao.CommonDao;
import com.itechart.studets_lab.book_library.model.Book;

public class BookDaoFactory {
    private static final BookDaoFactory INSTANCE = new BookDaoFactory();

    BookDaoFactory() {
    }

    public static BookDaoFactory getInstance() {
        return INSTANCE;
    }

    public BookDao getDao() {
        return new BookDao();
    }
}
