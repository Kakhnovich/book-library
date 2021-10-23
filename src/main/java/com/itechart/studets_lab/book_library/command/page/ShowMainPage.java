package com.itechart.studets_lab.book_library.command.page;

import com.itechart.studets_lab.book_library.command.Command;
import com.itechart.studets_lab.book_library.command.RequestContext;
import com.itechart.studets_lab.book_library.command.ResponseContext;
import com.itechart.studets_lab.book_library.command.UrlPatterns;
import com.itechart.studets_lab.book_library.model.Book;
import com.itechart.studets_lab.book_library.model.Borrow;
import com.itechart.studets_lab.book_library.service.email.GmailSender;
import com.itechart.studets_lab.book_library.service.impl.BookService;
import com.itechart.studets_lab.book_library.service.impl.BorrowService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum ShowMainPage implements Command {
    INSTANCE;

    private static final String BOOKS_PARAMETER_NAME = "books";
    private static final String PAGE_PARAMETER_NAME = "page";
    private static final String COUNT_OF_PAGES_ATTRIBUTE_NAME = "count";
    private static final String SORT_PARAMETER_NAME = "sort";
    private static final String SORT_PARAMETER_ACCEPT_VALUE = "accept";
    private final BookService bookService = BookService.getInstance();
    private final BorrowService borrowService = BorrowService.getInstance();

    private static final ResponseContext MAIN_PAGE_RESPONSE = new ResponseContext(UrlPatterns.MAIN, false);

    @Override
    public ResponseContext execute(RequestContext request) {
        String page = String.valueOf(request.getParameter(PAGE_PARAMETER_NAME));
        final int pageNumber = (page.equals("null")) ? 1 : Integer.parseInt(page);
        request.setAttribute(PAGE_PARAMETER_NAME, pageNumber);
        request.setAttribute(COUNT_OF_PAGES_ATTRIBUTE_NAME, bookService.getCountOfPages());
        Optional<List<Book>> books = bookService.findByPage(pageNumber);
        if (books.isPresent()) {
            List<Book> bookList;
            String sortValue = String.valueOf(request.getParameter(SORT_PARAMETER_NAME));
            request.setAttribute(SORT_PARAMETER_NAME, sortValue);
            if (sortValue.equals(SORT_PARAMETER_ACCEPT_VALUE)) {
                bookList = createListOfAvailableBooks(books.get()).stream().filter(book -> book.getTotalAmount() > 0).collect(Collectors.toList());
            } else {
                bookList = createListOfAvailableBooks(books.get());
            }
            request.setAttribute(BOOKS_PARAMETER_NAME, bookList);
        }
        return MAIN_PAGE_RESPONSE;
    }

    private List<Book> createListOfAvailableBooks(List<Book> books) {
        for (Book book : books) {
            Optional<List<Borrow>> borrows = borrowService.findBorrowsOfBook(book.getIsbn());
            borrows.ifPresent(borrowList -> book.setTotalAmount(book.getTotalAmount() - borrowList.size()));
        }
        return books;
    }
}
