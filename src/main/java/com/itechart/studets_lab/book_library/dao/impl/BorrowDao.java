package com.itechart.studets_lab.book_library.dao.impl;

import com.itechart.studets_lab.book_library.dao.CommonDao;
import com.itechart.studets_lab.book_library.model.Borrow;
import com.itechart.studets_lab.book_library.model.BorrowFactory;
import com.itechart.studets_lab.book_library.model.Reader;
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

public class BorrowDao implements CommonDao<Borrow> {
    private static final Logger LOGGER = LogManager.getLogger(BorrowDao.class);
    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    private static final String FIND_ALL_BORROWS_SQL = "select borrow_record.id, bookId, readerId, borrowDate, months as timePeriod, returnDate, comment, status from borrow_record join borrow_period bp on bp.id = borrow_record.timePeriodId join borrow_status bs on bs.id = borrow_record.statusId";
    private static final String FIND_BORROWS_FOR_PAGE_SQL = "select borrow_record.id, bookId, readerId, borrowDate, months as timePeriod, returnDate, comment, status from borrow_record join borrow_period bp on bp.id = borrow_record.timePeriodId join borrow_status bs on bs.id = borrow_record.statusId order by borrow_record.id limit ";
    private static final String FIND_BORROW_BY_ID_SQL = "select borrow_record.id, bookId, readerId, borrowDate, months as timePeriod, returnDate, comment, status from borrow_record join borrow_period bp on bp.id = borrow_record.timePeriodId join borrow_status bs on bs.id = borrow_record.statusId where borrow_record.id = ";
    private static final String FIND_ALL_BORROW_PERIODS_SQL = "select months from borrow_period";
    private static final String FIND_ALL_STATUSES_PERIODS_SQL = "select status from borrow_status";
    private static final String GET_COUNT_OF_BORROWS_SQL = "select count(id) AS count from borrow_record";
    private static final String CREATE_NEW_BORROW_SQL = "insert into borrow_record(bookId, readerId, borrowDate, timePeriodId, returnDate, comment, statusId) value (?,?,?,?,?,?,?)";
    private static final String UPDATE_BORROW_DATA_SQL = "update borrow_record set bookId=?, readerId=?, borrowDate=?, timePeriodId=?, returnDate=?, comment=?, statusId=? where id=?";
    private static final String GET_BORROW_PERIOD_ID_SQL = "select id from borrow_period where months = ";
    private static final String GET_BORROW_STATUS_ID_SQL = "select id from borrow_status where status = ";
    private static final String COUNT_COLUMN_NAME = "count";
    private static final String READER_ID_COLUMN_NAME = "readerID";

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
                Optional<Reader> reader =findReader(resultSet);
                if (reader.isPresent()) {
                    return Optional.of(retrieveBorrowData(resultSet, reader.get()));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to find Borrow by id: " + e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    private Optional<List<Borrow>> retrieveBorrowsFromSet(ResultSet resultSet) throws SQLException {
        List<Borrow> borrows = new ArrayList<>();
        while (resultSet.next()) {
            Optional<Reader> reader = findReader(resultSet);
            if (reader.isPresent()) {
                borrows.add(retrieveBorrowData(resultSet, reader.get()));
            }
        }
        return Optional.of(borrows);
    }

    private Optional<Reader> findReader(ResultSet resultSet) throws SQLException {
        ReaderDao readerDao = new ReaderDao();
        return readerDao.findByKey(resultSet.getInt(READER_ID_COLUMN_NAME));
    }

    private Borrow retrieveBorrowData(ResultSet resultSet, Reader reader) throws SQLException {
        return BorrowFactory.getInstance().create(resultSet, reader);
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

    @Override
    public Optional<Borrow> create(Borrow borrow) {
        try (final Connection conn = POOL.retrieveConnection();
             final PreparedStatement preparedStatement = conn.prepareStatement(CREATE_NEW_BORROW_SQL)) {
            fillPreparedStatement(borrow, preparedStatement);
            preparedStatement.execute();
            return Optional.of(borrow);
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to create new Borrow: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    private void fillPreparedStatement(Borrow borrow, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, borrow.getBookId());
        preparedStatement.setString(2, borrow.getReader().getEmail());
        preparedStatement.setDate(3, Date.valueOf(borrow.getBorrowDate()));
        preparedStatement.setInt(4, findTimePeriodId(borrow.getDuration()));
        preparedStatement.setDate(5, Date.valueOf(borrow.getReturnDate()));
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

    public Optional<List<Integer>> findAllPeriods(){
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_ALL_BORROW_PERIODS_SQL)) {
            List<Integer> periods = new ArrayList<>();
            while(resultSet.next()){
                periods.add(resultSet.getInt(1));
            }
            return Optional.of(periods);
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to find all borrow time periods: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    public Optional<List<String>> findAllStatuses(){
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_ALL_STATUSES_PERIODS_SQL)) {
            List<String> statuses = new ArrayList<>();
            while(resultSet.next()){
                statuses.add(resultSet.getString(1));
            }
            return Optional.of(statuses);
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to find all borrow time periods: " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }
}
