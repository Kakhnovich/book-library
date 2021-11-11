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

public class BookGenresDao {
    private static final Logger LOGGER = LogManager.getLogger(BookGenresDao.class);
    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    private static final String FIND_BOOK_GENRES_SQL = "select genre from book_genre where book_id = ";
    private static final String ADD_BOOK_GENRES_SQL = "insert into book_genre(book_id, genre) value ";
    private static final String DELETE_BOOK_GENRES_SQL = "delete from book_genre where book_id = ";

    BookGenresDao() {
    }

    public List<String> findBookGenres(int id) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet genresSet = statement.executeQuery(FIND_BOOK_GENRES_SQL + id)) {
            return retrieveBookGenres(genresSet);
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to find " + id + " Book genres: " + e.getLocalizedMessage());
            return new ArrayList<>();
        }
    }

    private List<String> retrieveBookGenres(ResultSet resultSet) throws SQLException {
        List<String> genresList = new ArrayList<>();
        while (resultSet.next()) {
            genresList.add(resultSet.getString(1));
        }
        return genresList;
    }

    public boolean addBookGenres(Statement statement, int id, List<String> genres) {
        try {
            for (String genre : genres) {
                statement.executeUpdate(ADD_BOOK_GENRES_SQL + "(" + id + ",'" + genre + "')");
            }
            return true;
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to add " + id + " Book genres: " + e.getLocalizedMessage());
        }
        return false;
    }

    public boolean deleteBookGenres(Statement statement, int id) {
        try {
            statement.executeUpdate(DELETE_BOOK_GENRES_SQL + id);
            return true;
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to delete " + id + " Book genres: " + e.getLocalizedMessage());
        }
        return false;
    }
}
