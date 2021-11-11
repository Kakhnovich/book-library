package com.itechart.studets_lab.book_library.dao.impl;

public class BorrowStatusDaoFactory {
    private static final BorrowStatusDaoFactory INSTANCE = new BorrowStatusDaoFactory();

    BorrowStatusDaoFactory() {
    }

    public static BorrowStatusDaoFactory getInstance() {
        return INSTANCE;
    }

    public BorrowStatusDao getDao() {
        return new BorrowStatusDao();
    }
}
