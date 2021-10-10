package com.itechart.studets_lab.book_library.listener;

import com.itechart.studets_lab.book_library.pool.ConnectionPool;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Custom listener of initialization and destroying of the server
 *
 * @author Elmax19
 * @version 1.0
 * @see ServletContextListener
 */
@WebListener
public class ApplicationListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ConnectionPool.getInstance().init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionPool.getInstance().destroy();
    }

}
