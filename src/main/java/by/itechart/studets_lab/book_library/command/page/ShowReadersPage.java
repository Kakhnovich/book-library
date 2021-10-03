package by.itechart.studets_lab.book_library.command.page;

import by.itechart.studets_lab.book_library.command.Command;
import by.itechart.studets_lab.book_library.command.RequestContext;
import by.itechart.studets_lab.book_library.command.ResponseContext;
import by.itechart.studets_lab.book_library.model.Reader;
import by.itechart.studets_lab.book_library.service.impl.ReaderService;

import java.util.List;
import java.util.Optional;

public enum ShowReadersPage implements Command {
    INSTANCE;

    private final ReaderService readerService = ReaderService.getInstance();
    private static final String READERS_ATTRIBUTE_NAME = "readers";

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

    public ShowReadersPage getInstance(){
        return INSTANCE;
    }

    @Override
    public ResponseContext execute(RequestContext request) {
        Optional<List<Reader>> readers = readerService.findAll();
        readers.ifPresent(readerList -> request.setAttribute(READERS_ATTRIBUTE_NAME, readerList));
        return READERS_PAGE_RESPONSE;
    }
}
