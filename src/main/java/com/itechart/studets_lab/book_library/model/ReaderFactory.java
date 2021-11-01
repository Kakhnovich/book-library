package com.itechart.studets_lab.book_library.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReaderFactory {
    private static final ReaderFactory INSTANCE = new ReaderFactory();
    private static final String ID_COLUMN_NAME = "id";
    private static final String FIRST_NAME_COLUMN_NAME = "first_name";
    private static final String LAST_NAME_COLUMN_NAME = "last_name";
    private static final String EMAIL_COLUMN_NAME = "email";
    private static final String GENDER_COLUMN_NAME = "gender";
    private static final String PHONE_NUMBER_COLUMN_NAME = "phone_number";
    private static final String DATE_OF_REGISTRATION_COLUMN_NAME = "registration_date";

    private ReaderFactory() {
    }

    public static ReaderFactory getInstance() {
        return INSTANCE;
    }

    public Reader create(ResultSet resultSet) throws SQLException {
        return Reader.builder()
                .id(resultSet.getInt(ID_COLUMN_NAME))
                .firstName(resultSet.getString(FIRST_NAME_COLUMN_NAME))
                .lastName(resultSet.getString(LAST_NAME_COLUMN_NAME))
                .email(resultSet.getString(EMAIL_COLUMN_NAME))
                .gender(resultSet.getString(GENDER_COLUMN_NAME))
                .phoneNumber(resultSet.getInt(PHONE_NUMBER_COLUMN_NAME))
                .dateOfRegistration(resultSet.getDate(DATE_OF_REGISTRATION_COLUMN_NAME).toLocalDate())
                .build();
    }
}
