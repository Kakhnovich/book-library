package com.itechart.studets_lab.book_library.command;

import javax.servlet.http.HttpSession;

/**
 * interface of custom request
 *
 * @author Elmax19
 * @version 1.0
 */
public interface RequestContext {

    /**
     * method set new attribute to request
     *
     * @param name name of attribute
     * @param obj  attribute value
     */
    void setAttribute(String name, Object obj);

    /**
     * method reset request parameter
     *
     * @param name name of parameter
     */
    void resetParameter(String name);

    /**
     * method remove session attribute
     *
     * @param name attribute name
     */
    void removeSessionAttribute(String name);

    /**
     * method gives back session
     *
     * @return session
     */
    HttpSession getSession();

    /**
     * method gives back parameter value
     *
     * @param name parameter name
     * @return parameter value
     */
    Object getParameter(String name);

    /**
     * method invalidate active session
     */
    void invalidateSession();

    /**
     * method set new session attribute
     *
     * @param name  session attribute name
     * @param value session attribute value
     */
    void setSessionAttribute(String name, Object value);

    /**
     * method gives back session attribute value
     *
     * @param name session attribute name
     * @return session attribute value
     */
    Object getSessionAttribute(String name);

    /**
     * method gives back referer header
     *
     * @param referer referer value
     * @return header value
     */
    String getHeader(String referer);

    /**
     * method gives back cookie value
     *
     * @param name cookie name
     * @return cookie value
     */
    String getCookieValue(String name);
}
