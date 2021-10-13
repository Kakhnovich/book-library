package com.itechart.studets_lab.book_library.command.page;


import com.itechart.studets_lab.book_library.command.Command;
import com.itechart.studets_lab.book_library.command.RequestContext;
import com.itechart.studets_lab.book_library.command.ResponseContext;
import com.itechart.studets_lab.book_library.model.Book;
import com.itechart.studets_lab.book_library.service.impl.BookService;

import java.util.List;
import java.util.Optional;


public enum ShowMainPage implements Command {
    INSTANCE;

    /* TODO по конвенции статические переменные должны идти перед переменными объекта. Поменяй плиз это в проекте и соблюдай конвенции. */
    private final BookService bookService = BookService.getInstance();
    private static final String BOOKS_PARAMETER_NAME = "books";
    private static final String PAGE_PARAMETER_NAME = "page";
    private static final String COUNT_OF_PAGES_ATTRIBUTE_NAME = "count";

    /*
     TODO ты во всех командах делаешь одно и то же с таким анонимным объектом, единственное что меняется страница и редирект или нет.
     Попробуй лучше создать какой нибудь класс, объект которого будет имьютабл после создания, в котором будут уже необходимые тебе методы getPage и isRedirect
     Tебе останется только передать в него при создании необходимые параметры страницы и редиректа.
    */
    private static final ResponseContext MAIN_PAGE_RESPONSE = new ResponseContext() {
        @Override
        public String getPage() {
            /*
             TODO не оставляй стринги в коде. Повыноси их либо в переменные класса либо еще лучше в какой нибудь отдельный класс, в которому будут все такие премененные, обозначающие страницы.
             Если потом придется подредактировать эти страницы, то это будет сделать гораздо проще в этом одном классе, а не скакать по разным классам.
             Да и в целом будет сразу видно, какие страницы у тебя есть в целом
            */
            return "/main.jsp";
        }

        @Override
        public boolean isRedirect() {
            return false;
        }
    };

    @Override
    public ResponseContext execute(RequestContext request) {
        String page = String.valueOf(request.getParameter(PAGE_PARAMETER_NAME));
        final int pageNumber = (page.equals("null")) ? 1 : Integer.parseInt(page);
        request.setAttribute(PAGE_PARAMETER_NAME, pageNumber);
        request.setAttribute(COUNT_OF_PAGES_ATTRIBUTE_NAME, bookService.getCountOfPages());
        Optional<List<Book>> books = bookService.findByPage(pageNumber);
        books.ifPresent(bookList -> request.setAttribute(BOOKS_PARAMETER_NAME, bookList));
        return MAIN_PAGE_RESPONSE;
    }
}
