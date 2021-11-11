package com.itechart.studets_lab.book_library.service.impl;

import com.itechart.studets_lab.book_library.dao.impl.BookDao;
import com.itechart.studets_lab.book_library.dao.impl.BookDaoFactory;
import com.itechart.studets_lab.book_library.dao.impl.BorrowDao;
import com.itechart.studets_lab.book_library.dao.impl.BorrowDaoFactory;
import com.itechart.studets_lab.book_library.dao.impl.BorrowPeriodDao;
import com.itechart.studets_lab.book_library.dao.impl.BorrowPeriodDaoFactory;
import com.itechart.studets_lab.book_library.dao.impl.BorrowStatusDao;
import com.itechart.studets_lab.book_library.dao.impl.BorrowStatusDaoFactory;
import com.itechart.studets_lab.book_library.dao.impl.ReaderDao;
import com.itechart.studets_lab.book_library.dao.impl.ReaderDaoFactory;
import com.itechart.studets_lab.book_library.model.Book;
import com.itechart.studets_lab.book_library.model.Borrow;
import com.itechart.studets_lab.book_library.model.BorrowDto;
import com.itechart.studets_lab.book_library.model.BorrowFactory;
import com.itechart.studets_lab.book_library.service.BorrowService;
import com.itechart.studets_lab.book_library.service.ReaderService;
import com.itechart.studets_lab.book_library.service.parser.BorrowParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BorrowServiceImpl implements BorrowService {
    private static final BorrowServiceImpl INSTANCE = new BorrowServiceImpl();
    private final ReaderService readerService = ReaderServiceImpl.getInstance();
    private final BorrowParser borrowParser = BorrowParser.getInstance();
    private final BorrowDao borrowDao;
    private final BorrowPeriodDao borrowPeriodDao;
    private final BorrowStatusDao borrowStatusDao;
    private final ReaderDao readerDao;
    private final BookDao bookDao;

    private BorrowServiceImpl() {
        BorrowDaoFactory borrowDaoFactory = BorrowDaoFactory.getInstance();
        borrowDao = borrowDaoFactory.getDao();
        BorrowPeriodDaoFactory borrowPeriodDaoFactory = BorrowPeriodDaoFactory.getInstance();
        borrowPeriodDao = borrowPeriodDaoFactory.getDao();
        BorrowStatusDaoFactory borrowStatusDaoFactory = BorrowStatusDaoFactory.getInstance();
        borrowStatusDao = borrowStatusDaoFactory.getDao();
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
    public BorrowDto create(BorrowDto borrowDto) {
        Optional<Book> book = bookDao.findByKey(borrowDto.getBookId());
        if (book.isPresent()) {
            int periodId = borrowPeriodDao.findTimePeriodId(borrowDto.getDuration());
            int statusId = borrowStatusDao.findStatusId(borrowDto.getStatus());
            Borrow newBorrow = BorrowFactory.getInstance().create(borrowDto, periodId, statusId);
            if(!borrowDao.create(newBorrow, book.get().getTotalAmount()).isPresent()){
                return null;
            }
            return borrowDto;
        }
        return null;
    }

    @Override
    public BorrowDto update(BorrowDto borrowDto) {
        if (borrowDto.getId() == 0) {
            return create(borrowDto);
        }
        int periodId = borrowPeriodDao.findTimePeriodId(borrowDto.getDuration());
        int statusId = borrowStatusDao.findStatusId(borrowDto.getStatus());
        Borrow newBorrow = BorrowFactory.getInstance().create(borrowDto, periodId, statusId);
        if(!borrowDao.update(newBorrow).isPresent()){
            return null;
        }
        return borrowDto;
    }

    @Override
    public List<BorrowDto> updateBorrowList(String borrowsData) {
        boolean wasError = false;
        if (!borrowsData.equals("null") && !borrowsData.equals("")) {
            List<BorrowDto> borrows = borrowParser.parseString(borrowsData);
            for (BorrowDto borrowDto : borrows) {
                if (update(borrowDto) == null) {
                    wasError = true;
                }
                readerService.update(borrowDto.getReader());
            }
            if (wasError) {
                return new ArrayList<>();
            }
            return borrows;
        }
        return new ArrayList<>();
    }

    @Override
    public int getCountOfPages() {
        return borrowDao.getCountOfPages();
    }

    @Override
    public List<BorrowDto> findBorrowsOfBook(int id) {
        return findAll().stream()
                .filter(borrow -> borrow.getBookId() == id)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowDto> findReaderBorrows(String email) {
        return findAll().stream().filter(borrow -> borrow.getReader().getEmail().equals(email)).collect(Collectors.toList());
    }

    @Override
    public List<Integer> findAllPeriods() {
        return borrowPeriodDao.findAllPeriods().orElse(new ArrayList<>());
    }

    @Override
    public List<String> findAllStatuses() {
        return borrowStatusDao.findAllStatuses().orElse(new ArrayList<>());
    }

    private BorrowDto convertToDto(Borrow borrow) {
        return new BorrowDto(
                borrow.getId(),
                borrow.getBookId(),
                readerDao.findByKey(borrow.getReaderId()).get(),
                borrow.getBorrowDate(),
                borrowPeriodDao.findPeriod(borrow.getDurationId()),
                borrow.getReturnDate(),
                borrow.getComment(),
                borrowStatusDao.findStatus(borrow.getStatusId()));
    }
}
