package com.itechart.studets_lab.book_library.dao.impl;

import com.itechart.studets_lab.book_library.dao.CommonDao;
import com.itechart.studets_lab.book_library.model.Book;
import com.itechart.studets_lab.book_library.model.BookFactory;
import com.itechart.studets_lab.book_library.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDao implements CommonDao<Book> {
    private static final Logger LOGGER = LogManager.getLogger(BookDao.class);
    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    private static final String FIND_ALL_BOOKS_SQL = "select id, isbn, cover, title, publisher, publish_date, page_count, description, total_amount from book";
    private static final String FIND_BOOKS_FOR_PAGE_SQL = "select id, isbn, cover, title, publisher, publish_date, page_count, description, total_amount from book order by isbn limit ";
    private static final String FIND_BOOK_BY_ID_SQL = "select id, isbn, cover, title, publisher, publish_date, page_count, description, total_amount from book where id = ";
    private static final String FIND_BOOK_BY_EMAIL_SQL = "select id, isbn, cover, title, publisher, publish_date, page_count, description, total_amount from book where title = ";
    private static final String GET_COUNT_OF_BOOKS_SQL = "select count(id) AS count from book";
    private static final String CREATE_NEW_BOOK_SQL = "insert into book(isbn, title, cover, publisher, publish_date, page_count, description, total_amount) value (?,?,?,?,?,?,?,?)";
    private static final String UPDATE_BOOK_DATA_SQL = "update book set isbn = ?, title = ?, cover = ?, publisher = ?, publish_date = ?, page_count = ?, description = ?, total_amount = ? where id = ?";
    private static final String DELETE_BOOK_DATA_SQL = "delete from book where id = ";
    private static final String COUNT_COLUMN_NAME = "count";

    BookDao() {
    }

    @Override
    public Optional<List<Book>> findAll() {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_ALL_BOOKS_SQL)) {
            return retrieveBooksFromSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to find all Books: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Book>> findByPageNumber(int page) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_BOOKS_FOR_PAGE_SQL + 10 * (page - 1) + ',' + 10)) {
            return retrieveBooksFromSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to find Books by page number: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    private Optional<List<Book>> retrieveBooksFromSet(ResultSet resultSet) throws SQLException {
        List<Book> books = new ArrayList<>();
        while (resultSet.next()) {
            books.add(retrieveBookData(resultSet));
        }
        return Optional.of(books);
    }

    private Book retrieveBookData(ResultSet resultSet) throws SQLException {
        return BookFactory.getInstance().create(resultSet);
    }

    @Override
    public Optional<Book> findByKey(int id) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_BOOK_BY_ID_SQL + id)) {
            if (resultSet.next()) {
                return Optional.of(retrieveBookData(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to find Book by ISBN: " + e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    public Optional<Book> findByTitle(String title) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_BOOK_BY_EMAIL_SQL + '\'' + title + '\'')) {
            if (resultSet.next()) {
                return Optional.of(retrieveBookData(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to find Book by ISBN: " + e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    @Override
    public int getCountOfPages() {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(GET_COUNT_OF_BOOKS_SQL)) {
            if (resultSet.next()) {
                return (resultSet.getInt(COUNT_COLUMN_NAME) + 9) / 10;
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to get count of Book pages: " + e.getLocalizedMessage());
        }
        return 0;
    }

    public Optional<Book> update(Connection conn, Book book) {
        try (final PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_BOOK_DATA_SQL)) {
            fillPreparedStatement(book, preparedStatement);
            preparedStatement.setInt(9, book.getId());
            preparedStatement.executeUpdate();
            return Optional.of(book);
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to update \"" + book.getTitle() + "\" Book data: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    public Optional<Book> create(Connection conn, Book book) {
        try (final PreparedStatement preparedStatement = conn.prepareStatement(CREATE_NEW_BOOK_SQL)) {
            fillPreparedStatement(book, preparedStatement);
            preparedStatement.execute();
            return Optional.of(book);
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to create new Book: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    public boolean delete(Statement statement, int id) {
        try {
            statement.executeUpdate(DELETE_BOOK_DATA_SQL + id);
            return true;
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to delete Book data: " + e.getLocalizedMessage());
        }
        return false;
    }

    private void fillPreparedStatement(Book book, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, book.getIsbn());
        preparedStatement.setString(2, book.getTitle());
        preparedStatement.setString(3, book.getCoverLink());
        preparedStatement.setString(4, book.getPublisher());
        preparedStatement.setDate(5, Date.valueOf(book.getPublishDate()));
        preparedStatement.setInt(6, book.getPageCount());
        preparedStatement.setString(7, book.getDescription());
        preparedStatement.setInt(8, book.getTotalAmount());
    }
}
