package com.itechart.studets_lab.book_library.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookFactory {
    private static final BookFactory INSTANCE = new BookFactory();
    private static final String ID_COLUMN_NAME = "id";
    private static final String ISBN_COLUMN_NAME = "isbn";
    private static final String COVER_LINK_COLUMN_NAME = "cover";
    private static final String TITLE_COLUMN_NAME = "title";
    private static final String PUBLISHER_COLUMN_NAME = "publisher";
    private static final String PUBLISH_DATE_COLUMN_NAME = "publish_date";
    private static final String PAGE_COUNT_COLUMN_NAME = "page_count";
    private static final String DESCRIPTION_COLUMN_NAME = "description";
    private static final String TOTAL_AMOUNT_COLUMN_NAME = "total_amount";

    private BookFactory() {
    }

    public static BookFactory getInstance() {
        return INSTANCE;
    }

    public Book create(ResultSet resultSet) throws SQLException {
        return Book.builder()
                .id(resultSet.getInt(ID_COLUMN_NAME))
                .isbn(resultSet.getInt(ISBN_COLUMN_NAME))
                .coverLink(resultSet.getString(COVER_LINK_COLUMN_NAME))
                .title(resultSet.getString(TITLE_COLUMN_NAME))
                .publisher(resultSet.getString(PUBLISHER_COLUMN_NAME))
                .publishDate(resultSet.getDate(PUBLISH_DATE_COLUMN_NAME).toLocalDate())
                .pageCount(resultSet.getInt(PAGE_COUNT_COLUMN_NAME))
                .description(resultSet.getString(DESCRIPTION_COLUMN_NAME))
                .totalAmount(resultSet.getInt(TOTAL_AMOUNT_COLUMN_NAME))
                .build();
    }

    public Book create(BookDto bookDto) {
        return Book.builder()
                .id(bookDto.getId())
                .isbn(bookDto.getIsbn())
                .coverLink(bookDto.getCoverLink())
                .title(bookDto.getTitle())
                .publisher(bookDto.getPublisher())
                .publishDate(bookDto.getPublishDate())
                .pageCount(bookDto.getPageCount())
                .description(bookDto.getDescription())
                .totalAmount(bookDto.getTotalAmount())
                .build();
    }
}
