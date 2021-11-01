package com.itechart.studets_lab.book_library.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BorrowFactory {
    private static final BorrowFactory INSTANCE = new BorrowFactory();
    private static final String ID_COLUMN_NAME = "id";
    private static final String BOOK_ID_COLUMN_NAME = "book_id";
    private static final String BORROW_DATE_COLUMN_NAME = "borrow_date";
    private static final String DURATION_COLUMN_NAME = "time_period";
    private static final String RETURN_DATE_COLUMN_NAME = "return_date";
    private static final String COMMENT_COLUMN_NAME = "comment";
    private static final String STATUS_COLUMN_NAME = "status";
    private static final String READER_ID_COLUMN_NAME = "reader_id";

    private BorrowFactory() {
    }

    public static BorrowFactory getInstance() {
        return INSTANCE;
    }

    public Borrow create(ResultSet resultSet) throws SQLException {
        Date returnDate = resultSet.getDate(RETURN_DATE_COLUMN_NAME);
        return Borrow.builder()
                .id(resultSet.getInt(ID_COLUMN_NAME))
                .bookId(resultSet.getInt(BOOK_ID_COLUMN_NAME))
                .readerId(resultSet.getInt(READER_ID_COLUMN_NAME))
                .borrowDate(resultSet.getDate(BORROW_DATE_COLUMN_NAME).toLocalDate())
                .duration(resultSet.getInt(DURATION_COLUMN_NAME))
                .returnDate(returnDate == null ? null : returnDate.toLocalDate())
                .comment(resultSet.getString(COMMENT_COLUMN_NAME))
                .status(resultSet.getString(STATUS_COLUMN_NAME))
                .build();
    }
}
