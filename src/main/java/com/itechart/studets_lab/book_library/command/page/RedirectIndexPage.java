package com.itechart.studets_lab.book_library.command.page;

import com.itechart.studets_lab.book_library.command.Command;
import com.itechart.studets_lab.book_library.command.RequestContext;
import com.itechart.studets_lab.book_library.command.ResponseContext;

public enum RedirectIndexPage implements Command {
    INSTANCE;

    private static final ResponseContext INDEX_PAGE_RESPONSE = new ResponseContext() {
        @Override
        public String getPage() {
            return "/index.jsp";
        }

        @Override
        public boolean isRedirect() {
            return true;
        }
    };

    @Override
    public ResponseContext execute(RequestContext request) {
        return INDEX_PAGE_RESPONSE;
    }
}
