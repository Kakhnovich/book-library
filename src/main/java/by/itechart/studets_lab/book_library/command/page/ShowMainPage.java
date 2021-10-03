package by.itechart.studets_lab.book_library.command.page;



import by.itechart.studets_lab.book_library.command.Command;
import by.itechart.studets_lab.book_library.command.RequestContext;
import by.itechart.studets_lab.book_library.command.ResponseContext;
import by.itechart.studets_lab.book_library.model.Book;
import by.itechart.studets_lab.book_library.service.impl.BookService;

import java.util.List;
import java.util.Optional;


public enum ShowMainPage implements Command {
    INSTANCE;

    private final BookService bookService = BookService.getInstance();
    private static final String BOOKS_PARAMETER_NAME = "books";

    private static final ResponseContext MAIN_PAGE_RESPONSE = new ResponseContext() {
        @Override
        public String getPage() {
            return "/main.jsp";
        }

        @Override
        public boolean isRedirect() {
            return false;
        }
    };

    @Override
    public ResponseContext execute(RequestContext request) {
        Optional<List<Book>> books = bookService.findAll();
        books.ifPresent(bookList -> request.setAttribute(BOOKS_PARAMETER_NAME, bookList));
        return MAIN_PAGE_RESPONSE;
    }
}
