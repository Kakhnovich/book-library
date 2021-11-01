package com.itechart.studets_lab.book_library.command.search;

import com.itechart.studets_lab.book_library.command.Command;
import com.itechart.studets_lab.book_library.command.RequestContext;
import com.itechart.studets_lab.book_library.command.ResponseContext;
import com.itechart.studets_lab.book_library.command.page.ShowSearchPage;
import com.itechart.studets_lab.book_library.model.Book;
import com.itechart.studets_lab.book_library.model.BookCriteria;
import com.itechart.studets_lab.book_library.service.BookService;
import com.itechart.studets_lab.book_library.service.impl.BookServiceImpl;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum FindBook implements Command {
    INSTANCE;

    private static final String BOOKS_PARAMETER_NAME = "books";
    private static final String TITLE_PARAMETER_NAME = "title";
    private static final String AUTHORS_PARAMETER_NAME = "authors";
    private static final String GENRES_PARAMETER_NAME = "genres";
    private static final String DESCRIPTION_PARAMETER_NAME = "description";
    private static final String PAGE_PARAMETER_NAME = "page";
    private static final String COUNT_OF_PAGES_ATTRIBUTE_NAME = "count";
    private final BookService bookService = BookServiceImpl.getInstance();

    @Override
    public ResponseContext execute(RequestContext request) {
        final String title = String.valueOf(request.getParameter(TITLE_PARAMETER_NAME));
        final String authors = String.valueOf(request.getParameter(AUTHORS_PARAMETER_NAME));
        final String genres = String.valueOf(request.getParameter(GENRES_PARAMETER_NAME));
        final String description = String.valueOf(request.getParameter(DESCRIPTION_PARAMETER_NAME));
        final BookCriteria bookCriteria = createCriteria(title, authors, genres, description);
        List<Book> books = bookService.findByCriteria(bookCriteria);
        String page = String.valueOf(request.getParameter(PAGE_PARAMETER_NAME));
        final int pageNumber = (page.equals("null")) ? 1 : Integer.parseInt(page);
        request.setAttribute(PAGE_PARAMETER_NAME, pageNumber);
        if (!books.isEmpty()) {
            request.setAttribute(COUNT_OF_PAGES_ATTRIBUTE_NAME, books.size() % 10 + 1);
            List<Book> rezList = new ArrayList<>();
            for (int i = 10 * (pageNumber - 1); i < 10 * pageNumber && i < books.size(); i++) {
                rezList.add(books.get(i));
            }
            request.setAttribute(BOOKS_PARAMETER_NAME, rezList);
        }
        return ShowSearchPage.INSTANCE.execute(request);
    }

    private BookCriteria createCriteria(String title, String authors, String genres, String description) {
        ;
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

    private List<String> parseStringIntoStringList(String set) {
        return Collections.singletonList(set);
    }
}
