package com.itechart.studets_lab.book_library.command;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * {@link RequestContext} wrapper class
 *
 * @author Elmax19
 * @version 1.0
 */
public class WrappingRequestContext implements RequestContext {

    /**
     * variable of original request
     */
    private final HttpServletRequest request;

    /**
     * class constructor - declare {@link WrappingRequestContext#request}
     *
     * @param request received request
     */
    private WrappingRequestContext(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setAttribute(String name, Object obj) {
        request.setAttribute(name, obj);
    }

    @Override
    public void resetParameter(String name) {
        request.setAttribute(name, request.getParameter(name));
    }

    @Override
    public Object getParameter(String name) {
        return request.getParameter(name);
    }

    @Override
    public HttpSession getSession() {
        return request.getSession();
    }

    @Override
    public void invalidateSession() {
        final HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    @Override
    public void setSessionAttribute(String name, Object value) {
        final HttpSession session = request.getSession();
        session.setAttribute(name, value);
    }

    @Override
    public Object getSessionAttribute(String name) {
        final HttpSession session = request.getSession();
        return session.getAttribute(name);
    }

    @Override
    public void removeSessionAttribute(String name) {
        request.getSession().removeAttribute(name);
    }

    @Override
    public String getHeader(String referer) {
        return request.getHeader(referer);
    }

    @Override
    public String getCookieValue(String name) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        return "";
    }

    /**
     * method of wrapping {@link HttpServletRequest} into {@link RequestContext}
     *
     * @param request received request
     * @return result of {@link WrappingRequestContext#WrappingRequestContext(HttpServletRequest)} constructor
     */
    public static RequestContext of(HttpServletRequest request) {
        return new WrappingRequestContext(request);
    }
}
