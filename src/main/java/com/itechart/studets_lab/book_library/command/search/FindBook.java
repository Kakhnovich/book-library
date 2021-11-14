package com.itechart.studets_lab.book_library.command.search;

import com.itechart.studets_lab.book_library.command.Command;
import com.itechart.studets_lab.book_library.command.RequestContext;
import com.itechart.studets_lab.book_library.command.ResponseContext;
import com.itechart.studets_lab.book_library.command.page.ShowSearchPage;
import com.itechart.studets_lab.book_library.model.BookCriteria;
import com.itechart.studets_lab.book_library.model.BookDto;
import com.itechart.studets_lab.book_library.model.BorrowDto;
import com.itechart.studets_lab.book_library.service.BookService;
import com.itechart.studets_lab.book_library.service.BorrowService;
import com.itechart.studets_lab.book_library.service.impl.BookServiceImpl;
import com.itechart.studets_lab.book_library.service.impl.BorrowServiceImpl;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public enum FindBook implements Command {
    INSTANCE;

    private static final String BOOKS_PARAMETER_NAME = "books";
    private static final String TITLE_PARAMETER_NAME = "title";
    private static final String AUTHORS_PARAMETER_NAME = "authors";
    private static final String GENRES_PARAMETER_NAME = "genres";
    private static final String DESCRIPTION_PARAMETER_NAME = "description";
    private static final String ERROR_ATTRIBUTE_NAME = "errorMsg";
    private static final String ERROR_ATTRIBUTE_VALUE = "No any books for this criteria!";
    private final BookService bookService = BookServiceImpl.getInstance();
    private final BorrowService borrowService = BorrowServiceImpl.getInstance();

    @Override
    public ResponseContext execute(RequestContext request) {
        final String title = String.valueOf(request.getParameter(TITLE_PARAMETER_NAME)).trim();
        final String authors = String.valueOf(request.getParameter(AUTHORS_PARAMETER_NAME)).trim();
        final String genres = String.valueOf(request.getParameter(GENRES_PARAMETER_NAME)).trim();
        final String description = String.valueOf(request.getParameter(DESCRIPTION_PARAMETER_NAME)).trim();
        final BookCriteria bookCriteria = createCriteria(title, authors, genres, description);
        List<BookDto> books = bookService.findByCriteria(bookCriteria);

        if (books.isEmpty()) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, ERROR_ATTRIBUTE_VALUE);
        } else {
            request.setAttribute(BOOKS_PARAMETER_NAME, createListOfAvailableBooks(books));
        }
        return ShowSearchPage.INSTANCE.execute(request);
    }

    private BookCriteria createCriteria(String title, String authors, String genres, String description) {
        BookCriteria.CriteriaBuilder bookCriteria = BookCriteria.builder();
        if (StringUtils.isNotBlank(title)) {
            bookCriteria.title(title);
        }
        if (StringUtils.isNotBlank(authors)) {
            bookCriteria.authors(parseStringIntoStringList(authors));
        }
        if (StringUtils.isNotBlank(genres)) {
            bookCriteria.genres(parseStringIntoStringList(genres));
        }
        if (StringUtils.isNotBlank(description)) {
            bookCriteria.description(description);
        }
        return bookCriteria.build();
    }

    private List<BookDto> createListOfAvailableBooks(List<BookDto> books) {
        for (BookDto book : books) {
            List<BorrowDto> borrows = borrowService.findBorrowsOfBook(book.getId()).stream()
                    .filter(borrow -> borrow.getReturnDate() == null)
                    .collect(Collectors.toList());
            book.setTotalAmount(book.getTotalAmount() - borrows.size());
        }
        return books;
    }

    private List<String> parseStringIntoStringList(String set) {
        return Arrays.stream(set.split(" ")).distinct().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
    }
}
