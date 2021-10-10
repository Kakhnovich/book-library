package com.itechart.studets_lab.book_library.command.page;

import com.itechart.studets_lab.book_library.command.Command;
import com.itechart.studets_lab.book_library.command.RequestContext;
import com.itechart.studets_lab.book_library.command.ResponseContext;
import com.itechart.studets_lab.book_library.model.Reader;
import com.itechart.studets_lab.book_library.service.impl.ReaderService;

import java.util.List;
import java.util.Optional;

public enum ShowReadersPage implements Command {
    INSTANCE;

    private final ReaderService readerService = ReaderService.getInstance();
    private static final String READERS_ATTRIBUTE_NAME = "readers";
    private static final String PAGE_PARAMETER_NAME = "page";
    private static final String COUNT_OF_PAGES_ATTRIBUTE_NAME = "count";

    private static final ResponseContext READERS_PAGE_RESPONSE = new ResponseContext() {
        @Override
        public String getPage() {
            return "/readers.jsp";
        }

        @Override
        public boolean isRedirect() {
            return false;
        }
    };

    public ShowReadersPage getInstance() {
        return INSTANCE;
    }

    @Override
    public ResponseContext execute(RequestContext request) {
        String page = String.valueOf(request.getParameter(PAGE_PARAMETER_NAME));
        final int pageNumber = (page.equals("null")) ? 1 : Integer.parseInt(page);
        request.setAttribute(PAGE_PARAMETER_NAME, pageNumber);
        request.setAttribute(COUNT_OF_PAGES_ATTRIBUTE_NAME, readerService.getCountOfPages());
        Optional<List<Reader>> readers = readerService.findByPage(pageNumber);
        readers.ifPresent(readerList -> request.setAttribute(READERS_ATTRIBUTE_NAME, readerList));
        return READERS_PAGE_RESPONSE;
    }
}
