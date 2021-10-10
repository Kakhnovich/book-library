package com.itechart.studets_lab.book_library.command.page;

import com.itechart.studets_lab.book_library.command.Command;
import com.itechart.studets_lab.book_library.command.RequestContext;
import com.itechart.studets_lab.book_library.command.ResponseContext;
import com.itechart.studets_lab.book_library.model.Book;
import com.itechart.studets_lab.book_library.model.Borrow;
import com.itechart.studets_lab.book_library.service.impl.BookService;
import com.itechart.studets_lab.book_library.service.impl.BorrowService;

import java.util.List;
import java.util.Optional;

public enum ShowBookPage implements Command {
    INSTANCE;

    private final BookService bookService = BookService.getInstance();
    private final BorrowService borrowService = BorrowService.getInstance();
    private static final String ISBN_PARAMETER_NAME = "bookISBN";
    private static final String BOOK_ATTRIBUTE_NAME = "book";
    private static final String BOOK_AUTHORS_ATTRIBUTE_NAME = "authors";
    private static final String BOOK_GENRES_ATTRIBUTE_NAME = "genres";
    private static final String BOOK_AVAILABLE_COUNT_ATTRIBUTE_NAME = "availableCount";

    private static final ResponseContext BOOK_PAGE_RESPONSE = new ResponseContext() {
        @Override
        public String getPage() {
            return "/bookPage.jsp";
        }

        @Override
        public boolean isRedirect() {
            return false;
        }
    };


    @Override
    public ResponseContext execute(RequestContext request) {
        String isbn = String.valueOf(request.getParameter(ISBN_PARAMETER_NAME));
        final int bookISBN = (isbn.equals("null")) ? 0 : Integer.parseInt(isbn);
        Optional<Book> book = bookService.findByKey(bookISBN);
        if (book.isPresent()) {
            request.setAttribute(BOOK_ATTRIBUTE_NAME, book.get());
            request.setAttribute(BOOK_AUTHORS_ATTRIBUTE_NAME, parseList(book.get().getAuthors()));
            request.setAttribute(BOOK_GENRES_ATTRIBUTE_NAME, parseList(book.get().getGenres()));
            Optional<List<Borrow>> borrows = borrowService.findBorrowsOfBook(book.get().getIsbn());
            borrows.ifPresent(borrowsList -> request.setAttribute(BOOK_AVAILABLE_COUNT_ATTRIBUTE_NAME, book.get().getTotalAmount() - borrows.get().size()));
        }
        return BOOK_PAGE_RESPONSE;
    }

    private String parseList(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String element : list) {
            stringBuilder.append(element).append(' ');
        }
        return stringBuilder.toString().trim();
    }
}
