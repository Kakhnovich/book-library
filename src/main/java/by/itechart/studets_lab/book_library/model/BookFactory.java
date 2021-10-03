package by.itechart.studets_lab.book_library.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BookFactory {
    private static final BookFactory INSTANCE = new BookFactory();
    private static final String ISBN_COLUMN_NAME = "isbn";
    private static final String COVER_LINK_COLUMN_NAME = "cover";
    private static final String TITLE_COLUMN_NAME = "title";
    private static final String PUBLISHER_COLUMN_NAME = "publisher";
    private static final String PUBLISH_DATE_COLUMN_NAME = "publishDate";
    private static final String PAGE_COUNT_COLUMN_NAME = "pageCount";
    private static final String DESCRIPTION_COLUMN_NAME = "description";
    private static final String TOTAL_AMOUNT_COLUMN_NAME = "totalAmount";
    private static final String STATUS_COLUMN_NAME = "status";

    private BookFactory() {
    }

    public static BookFactory getInstance() {
        return INSTANCE;
    }

    public Book create(ResultSet resultSet, List<String> authors,List<String> genres) throws SQLException {
        return Book.builder()
                .isbn(resultSet.getInt(ISBN_COLUMN_NAME))
                .coverLink(resultSet.getString(COVER_LINK_COLUMN_NAME))
                .title(resultSet.getString(TITLE_COLUMN_NAME))
                .authors(authors)
                .publisher(resultSet.getString(PUBLISHER_COLUMN_NAME))
                .publishDate(resultSet.getDate(PUBLISH_DATE_COLUMN_NAME).toLocalDate())
                .genres(genres)
                .pageCount(resultSet.getInt(PAGE_COUNT_COLUMN_NAME))
                .description(resultSet.getString(DESCRIPTION_COLUMN_NAME))
                .totalAmount(resultSet.getInt(TOTAL_AMOUNT_COLUMN_NAME))
                .status(resultSet.getString(STATUS_COLUMN_NAME))
                .build();
    }
}
