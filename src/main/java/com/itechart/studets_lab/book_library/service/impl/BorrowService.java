package com.itechart.studets_lab.book_library.service.impl;

import com.itechart.studets_lab.book_library.dao.impl.BorrowDao;
import com.itechart.studets_lab.book_library.model.Borrow;
import com.itechart.studets_lab.book_library.service.CommonService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BorrowService implements CommonService<Borrow> {
    private static final BorrowService INSTANCE = new BorrowService();
    private final BorrowDao borrowDao;

    private BorrowService() {
        borrowDao = new BorrowDao();
    }

    public static BorrowService getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<List<Borrow>> findAll() {
        return borrowDao.findAll();
    }

    @Override
    public Optional<List<Borrow>> findByPage(int page) {
        return borrowDao.findByPageNumber(page);
    }

    @Override
    public Optional<Borrow> findByKey(int id) {
        return borrowDao.findByKey(id);
    }

    @Override
    public Optional<Borrow> create(Borrow borrow) {
        return borrowDao.create(borrow);
    }

    @Override
    public Optional<Borrow> update(Borrow borrow) {
        return borrowDao.update(borrow);
    }

    @Override
    public int getCountOfPages() {
        return borrowDao.getCountOfPages();
    }

    public Optional<List<Borrow>> findBorrowsOfBook(int id) {
        return findAll().map(borrows -> borrows.stream().filter(borrow -> borrow.getBookId() == id).collect(Collectors.toList()));
    }

    public Optional<List<Borrow>> findReaderBorrows(String email) {
        return findAll().map(borrows -> borrows.stream().filter(borrow -> borrow.getReader().getEmail().equals(email)).collect(Collectors.toList()));
    }

    public Optional<List<Integer>> findAllPeriods(){
        return borrowDao.findAllPeriods();
    }

    public Optional<List<String>> findAllStatuses(){
        return borrowDao.findAllStatuses();
    }
}
