package com.itechart.studets_lab.book_library.dao.impl;

public class BorrowPeriodDaoFactory {
    private static final BorrowPeriodDaoFactory INSTANCE = new BorrowPeriodDaoFactory();

    BorrowPeriodDaoFactory() {
    }

    public static BorrowPeriodDaoFactory getInstance() {
        return INSTANCE;
    }

    public BorrowPeriodDao getDao() {
        return new BorrowPeriodDao();
    }
}
