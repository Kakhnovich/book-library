package com.itechart.studets_lab.book_library.dao.impl;

public class BorrowDaoFactory {
    private static final BorrowDaoFactory INSTANCE = new BorrowDaoFactory();

    BorrowDaoFactory() {
    }

    public static BorrowDaoFactory getInstance() {
        return INSTANCE;
    }

    public BorrowDao getDao() {
        return new BorrowDao();
    }
}
