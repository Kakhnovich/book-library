package com.itechart.studets_lab.book_library.controller.filter;

import com.itechart.studets_lab.book_library.command.UrlPatterns;
import com.itechart.studets_lab.book_library.command.WrappingRequestContext;
import com.itechart.studets_lab.book_library.command.page.ShowBookPage;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebFilter(urlPatterns = {"/controller", UrlPatterns.BOOK})
public class CreationBookFilter implements Filter {
    private static final String COMMAND_PARAMETER_NAME = "command";
    private static final String UPDATE_BOOK_COMMAND_VALUE = "update_book";
    private static final String BOOK_ID_PARAMETER_NAME = "bookId";
    private static final String TITLE_PARAMETER_NAME = "title";
    private static final String AUTHORS_PARAMETER_NAME = "authors";
    private static final String GENRES_PARAMETER_NAME = "genres";
    private static final String DESCRIPTION_PARAMETER_NAME = "description";
    private static final String PUBLISH_DATE_PARAMETER_NAVE = "publishDate";
    private static final String PAGE_COUNT_PARAMETER_NAME = "pageCount";
    private static final String ISBN_PARAMETER_NAME = "isbn";
    private static final String TOTAL_AMOUNT_PARAMETER_NAME = "totalAmount";
    private static final String ERROR_ATTRIBUTE_NAME = "errorMsg";
    private static final String ERROR_ATTRIBUTE_VALUE = "Check our input data!";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final String command = String.valueOf(servletRequest.getParameter(COMMAND_PARAMETER_NAME));
        if (command.equals(UPDATE_BOOK_COMMAND_VALUE)) {
            final String title = String.valueOf(servletRequest.getParameter(TITLE_PARAMETER_NAME)).trim();
            final String authors = String.valueOf(servletRequest.getParameter(AUTHORS_PARAMETER_NAME)).trim();
            final String genres = String.valueOf(servletRequest.getParameter(GENRES_PARAMETER_NAME)).trim();
            final String description = String.valueOf(servletRequest.getParameter(DESCRIPTION_PARAMETER_NAME)).trim();
            final String publishDate = String.valueOf(servletRequest.getParameter(PUBLISH_DATE_PARAMETER_NAVE)).trim();
            final String pageCount = String.valueOf(servletRequest.getParameter(PAGE_COUNT_PARAMETER_NAME)).trim();
            final String isbn = String.valueOf(servletRequest.getParameter(ISBN_PARAMETER_NAME)).trim();
            final String totalAmount = String.valueOf(servletRequest.getParameter(TOTAL_AMOUNT_PARAMETER_NAME)).trim();
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (title.isEmpty() || title.equals("null") ||
                    authors.isEmpty() || authors.equals("null") ||
                    genres.isEmpty() || genres.equals("null") ||
                    description.isEmpty() || description.equals("null") ||
                    publishDate.isEmpty() || publishDate.equals("null") || LocalDate.parse(publishDate, formatter).isAfter(LocalDate.now()) ||
                    pageCount.equals("null") || Integer.parseInt(pageCount) < 1 || Integer.parseInt(pageCount) > 1000 ||
                    isbn.equals("null") || Integer.parseInt(isbn) < 1 || Integer.parseInt(isbn) > 1_000_000_000 ||
                    totalAmount.equals("null") || Integer.parseInt(totalAmount) < 1 || Integer.parseInt(totalAmount) > 100) {
                final RequestDispatcher dispatcher = servletRequest.getRequestDispatcher(ShowBookPage.INSTANCE.execute(WrappingRequestContext.of((HttpServletRequest) servletRequest)).getPage());
                servletRequest.setAttribute(BOOK_ID_PARAMETER_NAME, servletRequest.getAttribute(BOOK_ID_PARAMETER_NAME));
                servletRequest.setAttribute(ERROR_ATTRIBUTE_NAME, ERROR_ATTRIBUTE_VALUE);
                servletRequest.setAttribute(COMMAND_PARAMETER_NAME, command);
                dispatcher.forward(servletRequest, servletResponse);
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}