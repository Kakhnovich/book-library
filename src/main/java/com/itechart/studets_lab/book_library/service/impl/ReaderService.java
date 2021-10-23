package com.itechart.studets_lab.book_library.service.impl;

import com.itechart.studets_lab.book_library.dao.impl.ReaderDao;
import com.itechart.studets_lab.book_library.model.Reader;
import com.itechart.studets_lab.book_library.service.CommonService;

import java.util.List;
import java.util.Optional;

public class ReaderService implements CommonService<Reader> {
    private static final ReaderService INSTANCE = new ReaderService();
    private final ReaderDao readerDao;

    private ReaderService() {
        readerDao = new ReaderDao();
    }

    public static ReaderService getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<List<Reader>> findAll() {
        return readerDao.findAll();
    }

    @Override
    public Optional<List<Reader>> findByPage(int page) {
        return readerDao.findByPageNumber(page);
    }

    @Override
    public Optional<Reader> findByKey(int id) {
        return readerDao.findByKey(id);
    }

    @Override
    public Optional<Reader> create(Reader reader) {
        return readerDao.create(reader);
    }

    @Override
    public Optional<Reader> update(Reader reader) {
        return readerDao.update(reader);
    }

    @Override
    public int getCountOfPages() {
        return readerDao.getCountOfPages();
    }
}
