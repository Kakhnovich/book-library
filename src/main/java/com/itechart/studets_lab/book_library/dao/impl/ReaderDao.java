package com.itechart.studets_lab.book_library.dao.impl;

import com.itechart.studets_lab.book_library.dao.CommonDao;
import com.itechart.studets_lab.book_library.model.Reader;
import com.itechart.studets_lab.book_library.model.ReaderFactory;
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

public class ReaderDao implements CommonDao<Reader, String> {
    private static final Logger LOGGER = LogManager.getLogger(ReaderDao.class);
    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    private static final String FIND_ALL_READERS_SQL = "select * from reader";
    private static final String FIND_READERS_FOR_PAGE_SQL = "select * from reader order by firstName, lastName limit ";
    private static final String FIND_READER_BY_EMAIL_SQL = "select * from reader where email = ";
    private static final String GET_COUNT_OF_READERS_SQL = "select count(email) AS count from reader";
    private static final String CREATE_NEW_READER_SQL = "insert into reader value (?,?,?,?,?,?)";
    private static final String UPDATE_READER_DATA_SQL = "update reader set firstName = ?, lastName = ?, email = ?, gender = ?, phoneNumber = ?, registrationDate = ? where email = ?";
    private static final String COUNT_COLUMN_NAME = "count";

    @Override
    public Optional<List<Reader>> findAll() {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_ALL_READERS_SQL)) {
            return retrieveReadersFromSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to find all Readers: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Reader>> findByPageNumber(int page) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_READERS_FOR_PAGE_SQL + 10 * (page - 1) + ',' + 10)) {
            return retrieveReadersFromSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to find Readers by page number: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    private Optional<List<Reader>> retrieveReadersFromSet(ResultSet resultSet) throws SQLException {
        List<Reader> readers = new ArrayList<>();
        while (resultSet.next()) {
            readers.add(retrieveReaderData(resultSet));
        }
        return Optional.of(readers);
    }

    private Reader retrieveReaderData(ResultSet resultSet) throws SQLException {
        return ReaderFactory.getInstance().create(resultSet);
    }

    @Override
    public Optional<Reader> findByKey(String email) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_READER_BY_EMAIL_SQL + '\'' + email + '\'')) {
            if (resultSet.next()) {
                return Optional.of(retrieveReaderData(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to find Reader by Email: " + e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    @Override
    public int getCountOfPages() {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(GET_COUNT_OF_READERS_SQL)) {
            if (resultSet.next()) {
                return (int) Math.ceil(resultSet.getInt(COUNT_COLUMN_NAME));
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to get count of Reader pages: " + e.getLocalizedMessage());
        }
        return 0;
    }

    @Override
    public synchronized Optional<Reader> update(Reader reader) {
        try (final Connection conn = POOL.retrieveConnection();
             final PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_READER_DATA_SQL)) {
            fillPreparedStatement(reader, preparedStatement);
            preparedStatement.setString(7, reader.getEmail());
            preparedStatement.executeUpdate();
            return Optional.of(reader);
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to update " + reader.getEmail() + " Reader data: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    @Override
    public synchronized Optional<Reader> create(Reader reader) {
        try (final Connection conn = POOL.retrieveConnection();
             final PreparedStatement preparedStatement = conn.prepareStatement(CREATE_NEW_READER_SQL)) {
            fillPreparedStatement(reader, preparedStatement);
            preparedStatement.execute();
            return Optional.of(reader);
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to create new Reader: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    private void fillPreparedStatement(Reader reader, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, reader.getFirstName());
        preparedStatement.setString(2, reader.getLastName());
        preparedStatement.setString(3, reader.getEmail());
        preparedStatement.setString(4, reader.getGender());
        preparedStatement.setInt(5, reader.getPhoneNumber());
        preparedStatement.setDate(6, Date.valueOf(reader.getDateOfRegistration()));
    }
}
