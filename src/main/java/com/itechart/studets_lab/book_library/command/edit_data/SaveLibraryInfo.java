package com.itechart.studets_lab.book_library.command.edit_data;

import com.itechart.studets_lab.book_library.command.Command;
import com.itechart.studets_lab.book_library.command.RequestContext;
import com.itechart.studets_lab.book_library.command.ResponseContext;
import com.itechart.studets_lab.book_library.command.page.RedirectIndexPage;
import com.itechart.studets_lab.book_library.service.email.GmailSender;

public enum SaveLibraryInfo implements Command {
    INSTANCE;

    private static final String LIBRARY_NAME_PARAMETER_NAME = "name";
    private static final String LIBRARY_SIGNATURE_PARAMETER_NAME = "signature";
    private static final String LIBRARY_ADDRESS_PARAMETER_NAME = "address";

    @Override
    public ResponseContext execute(RequestContext request) {
        String libraryName = String.valueOf(request.getParameter(LIBRARY_NAME_PARAMETER_NAME));
        String librarySignature = String.valueOf(request.getParameter(LIBRARY_SIGNATURE_PARAMETER_NAME));
        String libraryAddress = String.valueOf(request.getParameter(LIBRARY_ADDRESS_PARAMETER_NAME));
        new GmailSender().changeLibraryInfo(libraryName, librarySignature, libraryAddress);
        return RedirectIndexPage.INSTANCE.execute(request);
    }
}
