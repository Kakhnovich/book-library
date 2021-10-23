package com.itechart.studets_lab.book_library.command.page;

import com.itechart.studets_lab.book_library.command.Command;
import com.itechart.studets_lab.book_library.command.RequestContext;
import com.itechart.studets_lab.book_library.command.ResponseContext;
import com.itechart.studets_lab.book_library.command.UrlPatterns;

public enum ShowSearchPage implements Command {
    INSTANCE;

    private static final ResponseContext SEARCH_PAGE_RESPONSE = new ResponseContext(UrlPatterns.SEARCH, false);

    @Override
    public ResponseContext execute(RequestContext request) {
        return SEARCH_PAGE_RESPONSE;
    }
}
