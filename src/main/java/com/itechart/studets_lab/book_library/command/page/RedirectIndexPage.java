package com.itechart.studets_lab.book_library.command.page;

import com.itechart.studets_lab.book_library.command.Command;
import com.itechart.studets_lab.book_library.command.RequestContext;
import com.itechart.studets_lab.book_library.command.ResponseContext;
import com.itechart.studets_lab.book_library.command.UrlPatterns;

public enum RedirectIndexPage implements Command {
    INSTANCE;

    private static final ResponseContext INDEX_PAGE_RESPONSE = new ResponseContext(UrlPatterns.INDEX, true);

    @Override
    public ResponseContext execute(RequestContext request) {
        return INDEX_PAGE_RESPONSE;
    }
}
