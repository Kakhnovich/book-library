package com.itechart.studets_lab.book_library.service.impl;

import com.itechart.studets_lab.book_library.dao.impl.ReaderDao;
import com.itechart.studets_lab.book_library.model.Reader;
import com.itechart.studets_lab.book_library.service.CommonService;
import com.itechart.studets_lab.book_library.service.ReaderService;

import java.util.ArrayList;
import java.util.List;

public class ReaderServiceImpl implements ReaderService {
    private static final ReaderServiceImpl INSTANCE = new ReaderServiceImpl();
    private final ReaderDao readerDao;

    private ReaderServiceImpl() {
        readerDao = new ReaderDao();
    }

    public static ReaderServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Reader> findAll() {
        return checkOptionalList(readerDao.findAll());
    }

    @Override
    public List<Reader> findByPage(int page) {
        return checkOptionalList(readerDao.findByPageNumber(page));
    }

    @Override
    public Reader findByKey(int id) {
        return readerDao.findByKey(id).orElse(null);
    }

    @Override
    public Reader create(Reader reader) {
        return readerDao.create(reader).orElse(null);
    }

    @Override
    public Reader update(Reader reader) {
        return readerDao.update(reader).orElse(null);
    }

    @Override
    public int getCountOfPages() {
        return readerDao.getCountOfPages();
    }

    @Override
    public List<String> findAllEmails() {
        return readerDao.findAllEmails().orElse(new ArrayList<>());
    }
}
