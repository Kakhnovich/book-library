package com.itechart.studets_lab.book_library.dao.impl;

import com.itechart.studets_lab.book_library.dao.CommonDao;
import com.itechart.studets_lab.book_library.model.Borrow;
import com.itechart.studets_lab.book_library.model.BorrowFactory;
import com.itechart.studets_lab.book_library.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BorrowDao implements CommonDao<Borrow> {
    private static final Logger LOGGER = LogManager.getLogger(BorrowDao.class);
    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    private static final String FIND_ALL_BORROWS_SQL = "select borrow_record.id, book_id, reader_id, borrow_date, time_period_id, return_date, comment, status_id from borrow_record";
    private static final String FIND_BORROWS_FOR_PAGE_SQL = "select borrow_record.id, book_id, reader_id, borrow_date, time_period_id, return_date, comment, status_id from borrow_record order by id limit ";
    private static final String FIND_BORROW_BY_ID_SQL = "select borrow_record.id, book_id, reader_id, borrow_date, time_period_id, return_date, comment, status_id from borrow_record where borrow_record.id = ";
    private static final String GET_COUNT_OF_BORROWS_SQL = "select count(id) AS count from borrow_record";
    private static final String CREATE_NEW_BORROW_SQL = "insert into borrow_record(book_id, reader_id, borrow_date, time_period_id, return_date, comment, status_id) value (?,?,?,?,?,?,?)";
    private static final String UPDATE_BORROW_DATA_SQL = "update borrow_record set book_id=?, reader_id=?, borrow_date=?, time_period_id=?, return_date=?, comment=?, status_id=? where id=?";
    private static final String DELETE_BORROW_BY_BOOK_ID_SQL = "delete from borrow_record where book_id = ";
    private static final String COUNT_COLUMN_NAME = "count";

    BorrowDao() {
    }

    @Override
    public Optional<List<Borrow>> findAll() {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_ALL_BORROWS_SQL)) {
            return retrieveBorrowsFromSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to find all Borrows: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Borrow>> findByPageNumber(int page) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_BORROWS_FOR_PAGE_SQL + 10 * (page - 1) + ',' + 10)) {
            return retrieveBorrowsFromSet(resultSet);
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to find Borrows by page number: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Borrow> findByKey(int id) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_BORROW_BY_ID_SQL + id)) {
            if (resultSet.next()) {
                return Optional.of(retrieveBorrowData(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to find Borrow by id: " + e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    private Optional<List<Borrow>> retrieveBorrowsFromSet(ResultSet resultSet) throws SQLException {
        List<Borrow> borrows = new ArrayList<>();
        while (resultSet.next()) {
            borrows.add(retrieveBorrowData(resultSet));
        }
        return Optional.of(borrows);
    }

    private Borrow retrieveBorrowData(ResultSet resultSet) throws SQLException {
        return BorrowFactory.getInstance().create(resultSet);
    }

    @Override
    public int getCountOfPages() {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(GET_COUNT_OF_BORROWS_SQL)) {
            if (resultSet.next()) {
                return (int) Math.ceil(resultSet.getInt(COUNT_COLUMN_NAME));
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to get count of Borrow pages: " + e.getLocalizedMessage());
        }
        return 0;
    }

    public Optional<Borrow> update(Borrow borrow) {
        try (final Connection conn = POOL.retrieveConnection();
             final PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_BORROW_DATA_SQL)) {
            fillPreparedStatement(borrow, preparedStatement);
            preparedStatement.setInt(8, borrow.getId());
            preparedStatement.executeUpdate();
            return Optional.of(borrow);
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to update " + borrow.getId() + " Borrow data: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    public Optional<Borrow> create(Borrow borrow, int booksCount) {
        try (final Connection conn = POOL.retrieveConnection()) {
            conn.setAutoCommit(false);
            Savepoint savepoint = conn.setSavepoint("savepoint");
            try (final PreparedStatement preparedStatement = conn.prepareStatement(CREATE_NEW_BORROW_SQL)) {
                if (!findAll().isPresent() || findAll().get().stream()
                        .filter(borrow1 -> borrow1.getBookId() == borrow.getBookId())
                        .filter(borrow1 -> borrow1.getStatusId() == 4).count() >= booksCount) {
                    throw new SQLException("Not enough books to borrow");
                }
                fillPreparedStatement(borrow, preparedStatement);
                preparedStatement.execute();
                if (!findAll().isPresent() || findAll().get().stream()
                        .filter(borrow1 -> borrow1.getBookId() == borrow.getBookId())
                        .filter(borrow1 -> borrow1.getStatusId() == 4).count() >= booksCount) {
                    throw new SQLException("Not enough books to borrow");
                }
                conn.commit();
                return Optional.of(borrow);
            } catch (SQLException e) {
                LOGGER.error("SQLException while trying to create new Borrow: " + e.getLocalizedMessage());
                conn.rollback(savepoint);
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to get Connection: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    public boolean deleteByBookId(Statement statement, int bookId) {
        try {
            statement.executeUpdate(DELETE_BORROW_BY_BOOK_ID_SQL + bookId);
            return true;
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to delete " + bookId + " Book Borrows: " + e.getLocalizedMessage());
        }
        return false;
    }

    private void fillPreparedStatement(Borrow borrow, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, borrow.getBookId());
        preparedStatement.setInt(2, borrow.getReaderId());
        preparedStatement.setDate(3, Date.valueOf(borrow.getBorrowDate()));
        preparedStatement.setInt(4, borrow.getDurationId());
        preparedStatement.setDate(5, borrow.getStatusId() == 4 ? null : Date.valueOf(borrow.getReturnDate()));
        preparedStatement.setString(6, borrow.getComment());
        preparedStatement.setInt(7, borrow.getStatusId());
    }
}
