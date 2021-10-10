package com.itechart.studets_lab.book_library.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class to store connections.
 * Automatically increases and decreases storage capacity
 *
 * @author Elmax19
 * @version 1.0
 */
public class ConnectionPool {
    /**
     * class logger for logging errors
     */
    static final Logger LOGGER = LogManager.getLogger(ConnectionPool.class);
    /**
     * Singleton realisation
     */
    private static final ConnectionPool INSTANCE = new ConnectionPool();
    /**
     * store of used connections
     */
    private final List<ProxyConnection> usedConnections = new ArrayList<>();
    /**
     * {@link ReentrantLock} to lock methods
     */
    private final ReentrantLock lock = new ReentrantLock();
    /**
     * condition when non used connections storage if not empty
     */
    private final Condition notEmpty = lock.newCondition();
    /**
     * store of non used connections
     */
    private ArrayBlockingQueue<ProxyConnection> connections;
    /**
     * variables to work with database
     */
    private String url;
    private String username;
    private String password;
    /**
     * minimal and maximal storage capacity values
     */
    private int MIN_CONNECTIONS_AMOUNT;
    private int MAX_CONNECTIONS_AMOUNT;

    /**
     * default class constructor
     */
    private ConnectionPool() {
    }

    /**
     * singleton getter
     *
     * @return {@link ConnectionPool#INSTANCE}
     */
    public static ConnectionPool getInstance() {
        return INSTANCE;
    }

    /**
     * method to check condition
     *
     * @return true if storage is empty
     */
    public boolean isEmpty() {
        return connections.isEmpty();
    }

    /**
     * method that gives back connection from unused storage
     *
     * @return one of connections
     */
    public Connection retrieveConnection() {
        lock.lock();
        ProxyConnection con = null;
        try {
            if (connections.size() + usedConnections.size() < MAX_CONNECTIONS_AMOUNT
                    && connections.size() * 3 < usedConnections.size()) {
                new Thread(this::createNewConnection).start();
            }
            while (isEmpty()) {
                notEmpty.await();
            }
            con = connections.take();
            usedConnections.add(con);
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException at retrieving connection: " + e.getLocalizedMessage());
        } finally {
            lock.unlock();
        }
        return con;
    }

    /**
     * method that return connection to storage
     *
     * @param connection received connection
     */
    public void returnConnection(Connection connection) {
        if (usedConnections.stream().anyMatch(con -> con.equals(connection))) {
            lock.lock();
            try {
                if (connections.size() + usedConnections.size() > MIN_CONNECTIONS_AMOUNT
                        && connections.size() > usedConnections.size() * 3) {
                    ((ProxyConnection) connection).closeConnection();
                } else {
                    connections.put((ProxyConnection) connection);
                    usedConnections.remove(connection);
                    notEmpty.signal();
                }
            } catch (InterruptedException e) {
                LOGGER.error("InterruptedException at returning connection" + e.getLocalizedMessage());
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * method that inits {@link ConnectionPool#MIN_CONNECTIONS_AMOUNT} connections
     */
    public void init() {
        initParams();
        registerDrivers();
        for (int i = 0; i < MIN_CONNECTIONS_AMOUNT; i++) {
            createNewConnection();
        }
    }

    /**
     * method that inits class variables to work with database
     */
    private void initParams() {
        final String fileName = "database.properties";
        try {
            InputStream fileInputStream = ConnectionPool.class.getClassLoader().getResourceAsStream(fileName);
            Properties properties = new Properties();
            properties.load(fileInputStream);
            url = properties.getProperty("db.url") + '?' + properties.getProperty("db.timezoneSettings");
            username = properties.getProperty("db.user");
            password = properties.getProperty("db.password");
            MIN_CONNECTIONS_AMOUNT = Integer.parseInt(properties.getProperty("db.minPoolSize"));
            MAX_CONNECTIONS_AMOUNT = Integer.parseInt(properties.getProperty("db.maxPoolSize"));
            connections = new ArrayBlockingQueue<>(MIN_CONNECTIONS_AMOUNT);
        } catch (IOException e) {
            LOGGER.error("IOException at initiation: " + e.getLocalizedMessage());
        }
    }

    /**
     * method that create new connection and put it in storage
     */
    private void createNewConnection() {
        lock.lock();
        try {
            final Connection realConnection = DriverManager.getConnection(url, username, password);
            final ProxyConnection proxyConnection = new ProxyConnection(realConnection);
            connections.put(proxyConnection);
            notEmpty.signal();
        } catch (InterruptedException | SQLException e) {
            LOGGER.error("Fail at creating connection: " + e.getLocalizedMessage());
        } finally {
            lock.unlock();
        }
    }

    /**
     * method that destroy all connections when tge server closes
     */
    public void destroy() {
        connections.forEach(ProxyConnection::closeConnection);
        deregisterDrivers();
    }

    /**
     * method that declare jdbc driver
     */
    private static void registerDrivers() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.error("Fail at registration Driver: " + e.getLocalizedMessage());
        }
    }

    /**
     * method that deregister jdbc driver
     */
    private static void deregisterDrivers() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            try {
                DriverManager.deregisterDriver(drivers.nextElement());
            } catch (SQLException e) {
                LOGGER.error("SQLException at deregister Drivers" + e.getSQLState());
            }
        }
    }
}
