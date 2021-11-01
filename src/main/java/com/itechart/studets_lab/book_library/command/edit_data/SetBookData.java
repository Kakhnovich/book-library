package com.itechart.studets_lab.book_library.command.edit_data;

import com.itechart.studets_lab.book_library.command.Command;
import com.itechart.studets_lab.book_library.command.RequestContext;
import com.itechart.studets_lab.book_library.command.ResponseContext;
import com.itechart.studets_lab.book_library.command.page.RedirectIndexPage;
import com.itechart.studets_lab.book_library.model.Book;
import com.itechart.studets_lab.book_library.model.BookFactory;
import com.itechart.studets_lab.book_library.service.BookService;
import com.itechart.studets_lab.book_library.service.impl.BookServiceImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum SetBookData implements Command {
    INSTANCE;

    private static final String BOOK_ID_PARAMETER_NAME = "bookId";
    private static final String ISBN_PARAMETER_NAME = "isbn";
    private static final String COVER_PARAMETER_NAME = "cover";
    private static final String TITLE_PARAMETER_NAME = "title";
    private static final String AUTHORS_PARAMETER_NAME = "authors";
    private static final String PUBLISHER_PARAMETER_NAME = "publisher";
    private static final String PUBLISH_DATE_PARAMETER_NAME = "publishDate";
    private static final String GENRES_PARAMETER_NAME = "genres";
    private static final String PAGE_COUNT_PARAMETER_NAME = "pageCount";
    private static final String DESCRIPTION_PARAMETER_NAME = "description";
    private static final String TOTAL_AMOUNT_PARAMETER_NAME = "totalAmount";
    private final BookService bookService = BookServiceImpl.getInstance();

    @Override
    public ResponseContext execute(RequestContext request) {
        String bookId = String.valueOf(request.getParameter(BOOK_ID_PARAMETER_NAME));
        Book book = createBook(request);
        if (bookId.equals("")) {
            bookService.create(book);
        } else {
            bookService.update(book);
        }
        return RedirectIndexPage.INSTANCE.execute(request);
    }

    private Book createBook(RequestContext request) {
        String idValue = String.valueOf(request.getParameter(BOOK_ID_PARAMETER_NAME));
        int id = idValue.equals("") ? 0 : Integer.parseInt(idValue);
        int isbn = Integer.parseInt(String.valueOf(request.getParameter(ISBN_PARAMETER_NAME)));
        String cover = String.valueOf(request.getParameter(COVER_PARAMETER_NAME));
        String title = String.valueOf(request.getParameter(TITLE_PARAMETER_NAME));
        String authors = String.valueOf(request.getParameter(AUTHORS_PARAMETER_NAME));
        String publisher = String.valueOf(request.getParameter(PUBLISHER_PARAMETER_NAME));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate publishDate = LocalDate.parse(String.valueOf(request.getParameter(PUBLISH_DATE_PARAMETER_NAME)), formatter);
        String genres = String.valueOf(request.getParameter(GENRES_PARAMETER_NAME));
        int pageCount = Integer.parseInt(String.valueOf(request.getParameter(PAGE_COUNT_PARAMETER_NAME)));
        String description = String.valueOf(request.getParameter(DESCRIPTION_PARAMETER_NAME));
        int totalAmount = Integer.parseInt(String.valueOf(request.getParameter(TOTAL_AMOUNT_PARAMETER_NAME)));
        return BookFactory.getInstance().create(id, isbn, cover, title, parseString(authors), publisher, publishDate, parseString(genres), pageCount, description, totalAmount);
    }

    private List<String> parseString(String data) {
        return Arrays.stream(data.trim().split(" ")).filter(word -> word.length() > 0).distinct().collect(Collectors.toList());
    }
}
