package com.itechart.studets_lab.book_library.command;

/**
 * Custom response class
 *
 * @author Elmax19
 * @version 1.0
 */
public interface ResponseContext {
    /**
     * method that gives back page to redirect or forward to
     *
     * @return page link
     */
    String getPage();

    /**
     * method that gives back is it redirect or not
     *
     * @return true or false
     */
    boolean isRedirect();

}
