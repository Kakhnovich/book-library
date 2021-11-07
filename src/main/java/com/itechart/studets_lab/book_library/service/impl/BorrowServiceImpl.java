package com.itechart.studets_lab.book_library.service.impl;

import com.itechart.studets_lab.book_library.dao.impl.BookDao;
import com.itechart.studets_lab.book_library.dao.impl.BookDaoFactory;
import com.itechart.studets_lab.book_library.dao.impl.BorrowDao;
import com.itechart.studets_lab.book_library.dao.impl.BorrowDaoFactory;
import com.itechart.studets_lab.book_library.dao.impl.ReaderDao;
import com.itechart.studets_lab.book_library.dao.impl.ReaderDaoFactory;
import com.itechart.studets_lab.book_library.model.Book;
import com.itechart.studets_lab.book_library.model.Borrow;
import com.itechart.studets_lab.book_library.model.BorrowDto;
import com.itechart.studets_lab.book_library.service.BorrowService;
import com.itechart.studets_lab.book_library.service.CommonService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BorrowServiceImpl implements BorrowService {
    private static final BorrowServiceImpl INSTANCE = new BorrowServiceImpl();
    private final BorrowDao borrowDao;
    private final ReaderDao readerDao;
    private final BookDao bookDao;

    private BorrowServiceImpl() {
        BorrowDaoFactory borrowDaoFactory = BorrowDaoFactory.getInstance();
        borrowDao = borrowDaoFactory.getDao();
        ReaderDaoFactory readerDaoFactory = ReaderDaoFactory.getInstance();
        readerDao = readerDaoFactory.getDao();
        BookDaoFactory bookDaoFactory = BookDaoFactory.getInstance();
        bookDao = bookDaoFactory.getDao();
    }

    public static BorrowServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<BorrowDto> findAll() {
        return checkOptionalList(
                borrowDao.findAll()
                        .map(borrows -> borrows.stream()
                                .map(this::convertToDto)
                                .collect(Collectors.toList())));
    }

    @Override
    public List<BorrowDto> findByPage(int page) {
        return checkOptionalList(borrowDao.findByPageNumber(page)
                .map(borrows -> borrows.stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList())));
    }

    @Override
    public BorrowDto findByKey(int id) {
        Optional<Borrow> borrow = borrowDao.findByKey(id);
        return borrow.map(this::convertToDto).orElse(null);
    }

    @Override
    public BorrowDto create(Borrow borrow) {
        Optional<Book> book = bookDao.findByKey(borrow.getBookId());
        if (book.isPresent()) {
            Optional<Borrow> newBorrow = borrowDao.create(borrow, book.get().getTotalAmount());
            return newBorrow.map(this::convertToDto).orElse(null);
        }
        return null;
    }

    @Override
    public BorrowDto update(Borrow borrow) {
        Optional<Borrow> newBorrow = borrowDao.update(borrow);
        return newBorrow.map(this::convertToDto).orElse(null);
    }

    @Override
    public int getCountOfPages() {
        return borrowDao.getCountOfPages();
    }

    @Override
    public List<BorrowDto> findBorrowsOfBook(int id) {
        return findAll().stream().filter(borrow -> borrow.getBookId() == id).collect(Collectors.toList());
    }

    @Override
    public List<BorrowDto> findReaderBorrows(String email) {
        return findAll().stream().filter(borrow -> borrow.getReader().getEmail().equals(email)).collect(Collectors.toList());
    }

    @Override
    public List<Integer> findAllPeriods() {
        return borrowDao.findAllPeriods().orElse(new ArrayList<>());
    }

    @Override
    public List<String> findAllStatuses() {
        return borrowDao.findAllStatuses().orElse(new ArrayList<>());
    }

    private BorrowDto convertToDto(Borrow borrow) {
        return new BorrowDto(
                borrow.getId(),
                borrow.getBookId(),
                readerDao.findByKey(borrow.getReaderId()).get(),
                borrow.getBorrowDate(),
                borrow.getDuration(),
                borrow.getReturnDate(),
                borrow.getComment(),
                borrow.getStatus());
    }

    @Override
    public void deleteBorrowOfBook(int bookId) {
        borrowDao.deleteByBookId(bookId);
    }
}
