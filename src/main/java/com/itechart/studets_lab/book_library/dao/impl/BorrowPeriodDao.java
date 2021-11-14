package com.itechart.studets_lab.book_library.dao.impl;

import com.itechart.studets_lab.book_library.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BorrowPeriodDao {
    private static final Logger LOGGER = LogManager.getLogger(BorrowStatusDao.class);
    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    private static final String FIND_ALL_BORROW_PERIODS_SQL = "select months from borrow_period";
    private static final String FIND_BORROW_PERIOD_SQL = "select months from borrow_period where id = ";
    private static final String GET_BORROW_PERIOD_ID_SQL = "select id from borrow_period where months = ";

    BorrowPeriodDao() {
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

    public int findPeriod(int id) {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_BORROW_PERIOD_SQL + id)) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to find borrow " + id + " period: " + e.getLocalizedMessage());
            return 0;
        }
    }

    public int findTimePeriodId(int months) {
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
}
