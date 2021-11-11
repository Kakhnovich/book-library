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

public class BorrowStatusDao {
    private static final Logger LOGGER = LogManager.getLogger(BorrowStatusDao.class);
    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    private static final String FIND_ALL_STATUSES_SQL = "select status from borrow_status";
    private static final String FIND_BORROW_STATUS_SQL = "select status from borrow_status where id = ";
    private static final String GET_BORROW_STATUS_ID_SQL = "select id from borrow_status where status = ";

    BorrowStatusDao() {
    }

    public Optional<List<String>> findAllStatuses() {
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_ALL_STATUSES_SQL)) {
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

    public String findStatus(int id){
        try (final Connection conn = POOL.retrieveConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(FIND_BORROW_STATUS_SQL + id)) {
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
            return "";
        } catch (SQLException e) {
            LOGGER.error("SQLException while trying to find borrow " + id + " status: " + e.getLocalizedMessage());
            return "";
        }
    }

    public int findStatusId(String status) {
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
}
