package com.itechart.studets_lab.book_library.controller.filter;

import com.itechart.studets_lab.book_library.command.UrlPatterns;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

@WebFilter(urlPatterns = {"/controller", UrlPatterns.SEARCH}, initParams = {
        @WebInitParam(name = "page", value = UrlPatterns.SEARCH)})
public class CriteriaSearchFilter implements Filter {
    private static final String INIT_PARAMETER_NAME = "page";
    private static final String COMMAND_PARAMETER_NAME = "command";
    private static final String FIND_COMMAND_VALUE = "find_book";
    private static final String TITLE_PARAMETER_NAME = "title";
    private static final String AUTHORS_PARAMETER_NAME = "authors";
    private static final String GENRES_PARAMETER_NAME = "genres";
    private static final String DESCRIPTION_PARAMETER_NAME = "description";
    private static final String ERROR_ATTRIBUTE_NAME = "errorMsg";
    private static final String ERROR_ATTRIBUTE_VALUE = "Please enter one of the criteria!";
    private String page;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        page = filterConfig.getInitParameter(INIT_PARAMETER_NAME);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final String command = String.valueOf(servletRequest.getParameter(COMMAND_PARAMETER_NAME));
        if (command.equals(FIND_COMMAND_VALUE)) {
            if (servletRequest.getParameter(TITLE_PARAMETER_NAME).isEmpty() &&
                    servletRequest.getParameter(AUTHORS_PARAMETER_NAME).isEmpty() &&
                    servletRequest.getParameter(GENRES_PARAMETER_NAME).isEmpty() &&
                    servletRequest.getParameter(DESCRIPTION_PARAMETER_NAME).isEmpty()) {
                final RequestDispatcher dispatcher = servletRequest.getRequestDispatcher(page);
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
