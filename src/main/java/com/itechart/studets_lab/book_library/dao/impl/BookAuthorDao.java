package com.itechart.studets_lab.book_library.dao.impl;

import com.itechart.studets_lab.book_library.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookAuthorDao {
    private static final Logger LOGGER = LogManager.getLogger(BookAuthorDao.class);
    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    private static final String FIND_BOOK_AUTHORS_SQL = "select author from book_author where book_id = ";
    private static final String DELETE_BOOK_AUTHORS_SQL = "delete from book_author where book_id = ";
    private static final String ADD_BOOK_AUTHORS_SQL = "insert into book_author(book_id, author) value ";

    BookAuthorDao() {
    }

    public List<String> findBookAuthors(int id) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet authorsSet = statement.executeQuery(FIND_BOOK_AUTHORS_SQL + id)) {
            return retrieveBookAuthors(authorsSet);
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to find " + id + " Book authors: " + e.getLocalizedMessage());
            return new ArrayList<>();
        }
    }

    private List<String> retrieveBookAuthors(ResultSet resultSet) throws SQLException {
        List<String> authorsList = new ArrayList<>();
        while (resultSet.next()) {
            authorsList.add(resultSet.getString(1));
        }
        return authorsList;
    }

    public boolean addBookAuthors(Statement statement, int id, List<String> authors) {
        try {
            for (String author : authors) {
                statement.executeUpdate(ADD_BOOK_AUTHORS_SQL + "(" + id + ",'" + author + "')");
            }
            return true;
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to add " + id + " Book authors: " + e.getLocalizedMessage());
        }
        return false;
    }

    public boolean deleteBookAuthors(Statement statement, int id) {
        try {
            statement.executeUpdate(DELETE_BOOK_AUTHORS_SQL + id);
            return true;
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to delete " + id + " Book authors: " + e.getLocalizedMessage());
        }
        return false;
    }
}
