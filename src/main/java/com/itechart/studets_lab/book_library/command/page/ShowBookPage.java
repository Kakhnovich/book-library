package com.itechart.studets_lab.book_library.command.page;

import com.itechart.studets_lab.book_library.command.Command;
import com.itechart.studets_lab.book_library.command.RequestContext;
import com.itechart.studets_lab.book_library.command.ResponseContext;
import com.itechart.studets_lab.book_library.command.UrlPatterns;
import com.itechart.studets_lab.book_library.model.Book;
import com.itechart.studets_lab.book_library.model.Borrow;
import com.itechart.studets_lab.book_library.service.impl.BookService;
import com.itechart.studets_lab.book_library.service.impl.BorrowService;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public enum ShowBookPage implements Command {
    INSTANCE;

    private static final String ID_PARAMETER_NAME = "bookId";
    private static final String BOOK_ATTRIBUTE_NAME = "book";
    private static final String BOOK_AUTHORS_ATTRIBUTE_NAME = "authors";
    private static final String BOOK_GENRES_ATTRIBUTE_NAME = "genres";
    private static final String BOOK_AVAILABLE_COUNT_ATTRIBUTE_NAME = "availableCount";
    private static final String AVAILABLE_DATE_ATTRIBUTE_NAME = "availableDate";
    private static final String BOOK_BORROWS_ATTRIBUTE_NAME = "borrows";
    private static final String STATUS_ATTRIBUTE_NAME = "statuses";
    private static final String PERIODS_ATTRIBUTE_NAME = "periods";
    private final BookService bookService = BookService.getInstance();
    private final BorrowService borrowService = BorrowService.getInstance();

    private static final ResponseContext BOOK_PAGE_RESPONSE = new ResponseContext(UrlPatterns.BOOK, false);

    @Override
    public ResponseContext execute(RequestContext request) {
        String id = String.valueOf(request.getParameter(ID_PARAMETER_NAME));
        final int bookId = (id.equals("null")) ? 0 : Integer.parseInt(id);
        Optional<Book> book = bookService.findByKey(bookId);
        book.ifPresent(value -> setRequestAttributes(request, value));
        return BOOK_PAGE_RESPONSE;
    }

    private void setRequestAttributes(RequestContext request, Book book) {
        request.setAttribute(BOOK_ATTRIBUTE_NAME, book);
        request.setAttribute(BOOK_AUTHORS_ATTRIBUTE_NAME, parseList(book.getAuthors()));
        request.setAttribute(BOOK_GENRES_ATTRIBUTE_NAME, parseList(book.getGenres()));
        Optional<List<Borrow>> borrows = borrowService.findBorrowsOfBook(book.getId());
        if (borrows.isPresent() && borrows.get().size() > 0) {
            request.setAttribute(BOOK_AVAILABLE_COUNT_ATTRIBUTE_NAME, book.getTotalAmount() - borrows.get().size());
            request.setAttribute(BOOK_BORROWS_ATTRIBUTE_NAME, borrows.get());
            request.setAttribute(AVAILABLE_DATE_ATTRIBUTE_NAME, getAvailableDate(borrows.get()));
        } else {
            request.setAttribute(BOOK_AVAILABLE_COUNT_ATTRIBUTE_NAME, book.getTotalAmount());
        }
        Optional<List<Integer>> periods  = borrowService.findAllPeriods();
        periods.ifPresent(periodList -> request.setAttribute(PERIODS_ATTRIBUTE_NAME, periodList));
        Optional<List<String>> statuses  = borrowService.findAllStatuses();
        statuses.ifPresent(statusList -> request.setAttribute(STATUS_ATTRIBUTE_NAME, statusList));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private LocalDate getAvailableDate(List<Borrow> borrows) {
        Comparator<Borrow> comparator = Comparator.comparing(o -> o.getReturnDate().plusMonths(o.getDuration()));
        Borrow borrow = borrows.stream().min(comparator).get();
        return borrow.getBorrowDate().plusMonths(borrow.getDuration());
    }

    private String parseList(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String element : list) {
            stringBuilder.append(element).append(' ');
        }
        return stringBuilder.toString().trim();
    }
}
