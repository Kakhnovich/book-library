package com.itechart.studets_lab.book_library.command.edit_data;

import com.itechart.studets_lab.book_library.command.Command;
import com.itechart.studets_lab.book_library.command.RequestContext;
import com.itechart.studets_lab.book_library.command.ResponseContext;
import com.itechart.studets_lab.book_library.command.page.RedirectIndexPage;
import com.itechart.studets_lab.book_library.service.BookService;
import com.itechart.studets_lab.book_library.service.impl.BookServiceImpl;

public enum RemoveBook implements Command {
    INSTANCE;

    private static final String BOOK_ID_PARAMETER_NAME = "bookId";
    private final BookService bookService = BookServiceImpl.getInstance();

    @Override
    public ResponseContext execute(RequestContext request) {
        String idValue = String.valueOf(request.getParameter(BOOK_ID_PARAMETER_NAME));
        if (!idValue.equals("")) {
            bookService.deleteBookWithId(Integer.parseInt(idValue));
        }
        return RedirectIndexPage.INSTANCE.execute(request);
    }
}
