package com.itechart.studets_lab.book_library.dao.impl;

public class ReaderDaoFactory {
    private static final ReaderDaoFactory INSTANCE = new ReaderDaoFactory();

    ReaderDaoFactory() {
    }

    public static ReaderDaoFactory getInstance() {
        return INSTANCE;
    }

    public ReaderDao getDao() {
        return new ReaderDao();
    }
}
