package com.itechart.studets_lab.book_library.command;

public class ResponseContext {
    private final String page;
    private final boolean isRedirect;

    public ResponseContext(String page, boolean isRedirect) {
        this.page = page;
        this.isRedirect = isRedirect;
    }

    public String getPage() {
        return page;
    }

    public boolean isRedirect() {
        return isRedirect;
    }
}
