package com.example.moneytracker.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class SessionUtils {

    private static final String USER_SESSSION = "currentUser";

    public Boolean userExists(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object currentUserId = session.getAttribute(USER_SESSSION);
        if (currentUserId == null) {
            return false;
        }
        return true;
    }

    public Long getUserSessionId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object currentUserId = session.getAttribute(USER_SESSSION);
        return Long.parseLong(currentUserId.toString());
    }

    public void setUserSessionId(Long userId, HttpServletRequest request) {
        if (request != null && userId != null) {
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", userId);
        }
    }

    public void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(USER_SESSSION);
    }

}
