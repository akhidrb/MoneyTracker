package com.example.moneytracker.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class SessionUtils {

    private static final String USER_SESSSION = "currentUser";

    public Boolean userExists(HttpServletRequest request) {
        Object currentUserId = getUserId();
        if (currentUserId == null) {
            return false;
        }
        return true;
    }

    public Long getUserSessionId(HttpServletRequest request) {
        Object currentUserId = getUserId();
        return Long.parseLong(currentUserId.toString());
    }

    public void clearSession(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        session.removeAttribute(USER_SESSSION);
    }

    private Object getUserId() {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        return session.getAttribute(USER_SESSSION);
    }

}
