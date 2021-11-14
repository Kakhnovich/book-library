package com.itechart.studets_lab.book_library.command.edit_data;

import com.itechart.studets_lab.book_library.command.Command;
import com.itechart.studets_lab.book_library.command.RequestContext;
import com.itechart.studets_lab.book_library.command.ResponseContext;
import com.itechart.studets_lab.book_library.command.page.ShowReadersPage;
import com.itechart.studets_lab.book_library.model.Reader;
import com.itechart.studets_lab.book_library.model.ReaderFactory;
import com.itechart.studets_lab.book_library.service.ReaderService;
import com.itechart.studets_lab.book_library.service.impl.ReaderServiceImpl;

public enum SetReaderData implements Command {
    INSTANCE;

    private static final String ID_ATTRIBUTE_NAME = "id";
    private static final String FIRST_NAME_ATTRIBUTE_NAME = "firstName";
    private static final String LAST_NAME_ATTRIBUTE_NAME = "lastName";
    private static final String EMAIL_ATTRIBUTE_NAME = "email";
    private static final String GENDER_ATTRIBUTE_NAME = "gender";
    private static final String PHONE_NUMBER_ATTRIBUTE_NAME = "phone";
    private static final String ERROR_ATTRIBUTE_NAME = "errorMsg";
    private static final String ERROR_ATTRIBUTE_VALUE = "Reader hasn't been updated!";
    private final ReaderService readerService = ReaderServiceImpl.getInstance();

    @Override
    public ResponseContext execute(RequestContext request) {
        int id = Integer.parseInt(String.valueOf(request.getParameter(ID_ATTRIBUTE_NAME)));
        String firstName = String.valueOf(request.getParameter(FIRST_NAME_ATTRIBUTE_NAME)).trim();
        String lastName = String.valueOf(request.getParameter(LAST_NAME_ATTRIBUTE_NAME)).trim();
        String email = String.valueOf(request.getParameter(EMAIL_ATTRIBUTE_NAME)).trim();
        String gender = String.valueOf(request.getParameter(GENDER_ATTRIBUTE_NAME));
        String phone = String.valueOf(request.getParameter(PHONE_NUMBER_ATTRIBUTE_NAME));
        int phoneNumber = phone.equals("null") ? 0 : Integer.parseInt(phone);
        Reader reader = ReaderFactory.getInstance().create(id, email, firstName, lastName, gender, phoneNumber);
        if (readerService.update(reader) == null) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, ERROR_ATTRIBUTE_VALUE);
        }
        return ShowReadersPage.INSTANCE.execute(request);
    }
}
