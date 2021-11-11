package com.itechart.studets_lab.book_library.service.impl;

import com.itechart.studets_lab.book_library.dao.impl.ReaderDao;
import com.itechart.studets_lab.book_library.dao.impl.ReaderDaoFactory;
import com.itechart.studets_lab.book_library.model.Reader;
import com.itechart.studets_lab.book_library.service.ReaderService;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ReaderServiceImpl implements ReaderService {
    private static final ReaderServiceImpl INSTANCE = new ReaderServiceImpl();
    private final ReaderDao readerDao;

    private ReaderServiceImpl() {
        ReaderDaoFactory readerDaoFactory = ReaderDaoFactory.getInstance();
        readerDao = readerDaoFactory.getDao();
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
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Reader create(Reader reader) {
        Optional<Reader> newReader = readerDao.create(reader);
        return newReader.map(value -> readerDao.findByEmail(value.getEmail()).get()).orElse(null);
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
    public HashMap<String, String> findEmailsWithNames() {
        HashMap<String, String> hashMap = new HashMap<>();
        List<Reader> readers = findAll();
        for (Reader reader : readers) {
            hashMap.put(reader.getEmail(), reader.getFirstName() + ' ' + reader.getLastName());
        }
        return hashMap;
    }

    @Override
    public Reader findReaderByEmail(String email) {
        Optional<Reader> reader = findAll().stream().filter(user -> user.getEmail().equals(email)).findFirst();
        return reader.orElse(null);
    }
}
