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
    private static final String FIND_ALL_BORROWS_SQL = "select borrow_record.id, book_id, reader_id, borrow_date, months as time_period, return_date, comment, status from borrow_record join borrow_period bp on bp.id = borrow_record.time_period_id join borrow_status bs on bs.id = borrow_record.status_id";
    private static final String FIND_BORROWS_FOR_PAGE_SQL = "select borrow_record.id, book_id, reader_id, borrow_date, months as time_period, return_date, comment, status from borrow_record join borrow_period bp on bp.id = borrow_record.time_period_id join borrow_status bs on bs.id = borrow_record.status_id order by borrow_record.id limit ";
    private static final String FIND_BORROW_BY_ID_SQL = "select borrow_record.id, book_id, reader_id, borrow_date, months as time_period, return_date, comment, status from borrow_record join borrow_period bp on bp.id = borrow_record.time_period_id join borrow_status bs on bs.id = borrow_record.status_id where borrow_record.id = ";
    private static final String FIND_ALL_BORROW_PERIODS_SQL = "select months from borrow_period";
    private static final String FIND_ALL_STATUSES_PERIODS_SQL = "select status from borrow_status";
    private static final String GET_COUNT_OF_BORROWS_SQL = "select count(id) AS count from borrow_record";
    private static final String CREATE_NEW_BORROW_SQL = "insert into borrow_record(book_id, reader_id, borrow_date, time_period_id, return_date, comment, status_id) value (?,?,?,?,?,?,?)";
    private static final String UPDATE_BORROW_DATA_SQL = "update borrow_record set book_id=?, reader_id=?, borrow_date=?, time_period_id=?, return_date=?, comment=?, status_id=? where id=?";
    private static final String DELETE_BORROW_BY_BOOK_ID_SQL = "delete from borrow_record where book_id = ";
    private static final String GET_BORROW_PERIOD_ID_SQL = "select id from borrow_period where months = ";
    private static final String GET_BORROW_STATUS_ID_SQL = "select id from borrow_status where status = ";
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

    @Override
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
                if (!findAll().isPresent() || findAll().get().size() >= booksCount) {
                    throw new SQLException("Not enough books to borrow");
                }
                fillPreparedStatement(borrow, preparedStatement);
                preparedStatement.execute();
                if (!findAll().isPresent() || findAll().get().size() > booksCount) {
                    throw new SQLException("Not enough books to borrow");
                }
                conn.commit();
                return Optional.of(borrow);
            } catch (SQLException e) {
                LOGGER.error("SQLException while trying to create new Borrow: " + e.getLocalizedMessage());
                conn.rollback(savepoint);
                conn.commit();
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to get Connection: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    public void deleteByBookId(int bookId) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement()) {
            statement.executeUpdate(DELETE_BORROW_BY_BOOK_ID_SQL + bookId);
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to delete " + bookId + " Book Borrows: " + e.getLocalizedMessage());
        }
    }

    private void fillPreparedStatement(Borrow borrow, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, borrow.getBookId());
        preparedStatement.setInt(2, borrow.getReaderId());
        preparedStatement.setDate(3, Date.valueOf(borrow.getBorrowDate()));
        preparedStatement.setInt(4, findTimePeriodId(borrow.getDuration()));
        preparedStatement.setDate(5, borrow.getStatus().equals("not returned") ? null : Date.valueOf(borrow.getReturnDate()));
        preparedStatement.setString(6, borrow.getComment());
        preparedStatement.setInt(7, findStatusId(borrow.getStatus()));
    }

    private int findTimePeriodId(int months) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(GET_BORROW_PERIOD_ID_SQL + months)) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to find " + months + " months Period ID: " + e.getLocalizedMessage());
        }
        return 0;
    }

    private int findStatusId(String status) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(GET_BORROW_STATUS_ID_SQL + '\'' + status + '\'')) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to find " + status + " Status ID: " + e.getLocalizedMessage());
        }
        return 0;
    }

    public Optional<List<Integer>> findAllPeriods() {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_ALL_BORROW_PERIODS_SQL)) {
            List<Integer> periods = new ArrayList<>();
            while (resultSet.next()) {
                periods.add(resultSet.getInt(1));
            }
            return Optional.of(periods);
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to find all borrow time periods: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    public Optional<List<String>> findAllStatuses() {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_ALL_STATUSES_PERIODS_SQL)) {
            List<String> statuses = new ArrayList<>();
            while (resultSet.next()) {
                statuses.add(resultSet.getString(1));
            }
            return Optional.of(statuses);
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to find all borrow time periods: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }
}
