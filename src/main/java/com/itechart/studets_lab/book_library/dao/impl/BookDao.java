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

public class BookDao implements CommonDao<Book, Integer> {
    private static final Logger LOGGER = LogManager.getLogger(BookDao.class);
    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    private static final String FIND_ALL_BOOKS_SQL = "select isbn, cover, title, publisher, publishDate, pageCount, description, totalAmount from book";
    private static final String FIND_BOOKS_FOR_PAGE_SQL = "select isbn, cover, title, publisher, publishDate, pageCount, description, totalAmount from book order by isbn limit ";
    private static final String FIND_BOOK_GENRES_SQL = "select genre from book_genres join book b on b.isbn = book_genres.book_isbn where book_isbn = ";
    private static final String FIND_BOOK_AUTHORS_SQL = "select author from book_authors join book b on b.isbn = book_authors.book_isbn where book_isbn = ";
    private static final String FIND_BOOK_BY_ISBN_SQL = "select isbn, cover, title, publisher, publishDate, pageCount, description, totalAmount from book where isbn = ";
    private static final String GET_COUNT_OF_BOOKS_SQL = "select count(isbn) AS count from book";
    private static final String CREATE_NEW_BOOK_SQL = "insert into book value (?,?,?,?,?,?,?,?)";
    private static final String UPDATE_BOOK_DATA_SQL = "update book set isbn = ?, cover = ?, title = ?, publisher = ?, publishDate = ?, pageCount = ?, description = ?, totalAmount = ? where isbn = ?";
    private static final String DELETE_BOOK_AUTHORS_SQL = "delete from book_authors where book_isbn = ";
    private static final String DELETE_BOOK_GENRES_SQL = "delete from book_genres where book_isbn = ";
    private static final String ADD_BOOK_AUTHORS_SQL = "insert into book_authors(book_isbn, author) value (?,?)";
    private static final String ADD_BOOK_GENRES_SQL = "insert into book_genres(book_isbn, genre) value (?,?)";
    private static final String ISBN_COLUMN_NAME = "isbn";
    private static final String COUNT_COLUMN_NAME = "count";

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
        int isbn = resultSet.getInt(ISBN_COLUMN_NAME);
        List<String> authors = findBookAuthors(isbn),
                genres = findBookGenres(isbn);
        return BookFactory.getInstance().create(resultSet, authors, genres);
    }

    private List<String> findBookAuthors(int isbn) throws SQLException {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet authorsSet = statement.executeQuery(FIND_BOOK_AUTHORS_SQL + isbn)) {
            return retrieveBookAuthorsOrGenres(authorsSet);
        }
    }

    private List<String> findBookGenres(int isbn) throws SQLException {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet genresSet = statement.executeQuery(FIND_BOOK_GENRES_SQL + isbn)) {
            return retrieveBookAuthorsOrGenres(genresSet);
        }
    }

    private List<String> retrieveBookAuthorsOrGenres(ResultSet resultSet) throws SQLException {
        List<String> resultLit = new ArrayList<>();
        while (resultSet.next()) {
            resultLit.add(resultSet.getString(1));
        }
        return resultLit;
    }

    @Override
    public Optional<Book> findByKey(Integer isbn) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_BOOK_BY_ISBN_SQL + isbn)) {
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
                return (int) Math.ceil(resultSet.getInt(COUNT_COLUMN_NAME));
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to get count of Book pages: " + e.getLocalizedMessage());
        }
        return 0;
    }

    @Override
    public synchronized Optional<Book> update(Book book) {
        try (final Connection conn = POOL.retrieveConnection();
             final PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_BOOK_DATA_SQL)) {
            fillPreparedStatement(book, preparedStatement);
            preparedStatement.setInt(9, book.getIsbn());
            deleteBookAuthors(book.getIsbn());
            deleteBookGenres(book.getIsbn());
            preparedStatement.executeUpdate();
            addBookAuthors(book.getIsbn(), book.getAuthors());
            addBookGenres(book.getIsbn(), book.getGenres());
            return Optional.of(book);
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to update " + book.getIsbn() + " Book data: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Book> create(Book book) {
        try (final Connection conn = POOL.retrieveConnection();
             final PreparedStatement preparedStatement = conn.prepareStatement(CREATE_NEW_BOOK_SQL)) {
            fillPreparedStatement(book, preparedStatement);
            preparedStatement.execute();
            addBookAuthors(book.getIsbn(), book.getAuthors());
            addBookGenres(book.getIsbn(), book.getGenres());
            return Optional.of(book);
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to create new Book: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    private void fillPreparedStatement(Book book, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, book.getIsbn());
        preparedStatement.setString(2, book.getCoverLink());
        preparedStatement.setString(3, book.getTitle());
        preparedStatement.setString(4, book.getPublisher());
        preparedStatement.setDate(5, Date.valueOf(book.getPublishDate()));
        preparedStatement.setInt(6, book.getPageCount());
        preparedStatement.setString(7, book.getDescription());
        preparedStatement.setInt(8, book.getTotalAmount());
    }

    private void deleteBookAuthors(int isbn) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement()) {
            statement.executeUpdate(DELETE_BOOK_AUTHORS_SQL + isbn);
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to delete " + isbn + " Book authors: " + e.getLocalizedMessage());
        }
    }

    private void deleteBookGenres(int isbn) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement()) {
            statement.executeUpdate(DELETE_BOOK_GENRES_SQL + isbn);
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to delete " + isbn + " Book genres: " + e.getLocalizedMessage());
        }
    }

    private void addBookAuthors(int isbn, List<String> authors) {
        try (final Connection conn = POOL.retrieveConnection();
             final PreparedStatement preparedStatement = conn.prepareStatement(ADD_BOOK_AUTHORS_SQL)) {
            for (String author : authors) {
                preparedStatement.setInt(1, isbn);
                preparedStatement.setString(2, author);
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to add " + isbn + " Book authors: " + e.getLocalizedMessage());
        }
    }

    private void addBookGenres(int isbn, List<String> genres) {
        try (final Connection conn = POOL.retrieveConnection();
             final PreparedStatement preparedStatement = conn.prepareStatement(ADD_BOOK_GENRES_SQL)) {
            for (String genre : genres) {
                preparedStatement.setInt(1, isbn);
                preparedStatement.setString(2, genre);
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to add " + isbn + " Book genres: " + e.getLocalizedMessage());
        }
    }
}
