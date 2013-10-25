package com.mycompany.template.api.security;

import com.mycompany.template.beans.User;
import com.mycompany.template.services.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: azee
  */
public class AuthFilter extends AbstractPreAuthenticatedProcessingFilter {

    private static String SID_COOKIE_NAME = "Session_id";
    private static String GUEST_VAL = "Guest";
    private final static Logger log = Logger.getLogger(AuthFilter.class);

    @Autowired
    private UserService userService;

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        User user;

        Cookie sidCookie = null;

        //Searching for Session_id cookie
        if (request != null && request.getCookies() != null){
            for (Cookie coockie : request.getCookies()){
                if (coockie.getName().equals(SID_COOKIE_NAME)){
                    sidCookie = coockie;
                }
            }
        }

        if (sidCookie != null){
            log.info("Found sid cookie, trying to authenticate");
        }
        else {
            log.info("Couldn't find sid cookie");
            return GUEST_VAL;
        }


        try {
            user = userService.checkSid(sidCookie.getValue());
        } catch (Exception e) {
            log.error("Couldn't get a user by sid. Will be authorized as a Guest");
            return GUEST_VAL;
        }

        if (user != null) {
            log.info("Filter: Getting user: " + user.getName());
            return user.getName().trim();
        }

        return GUEST_VAL;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "";
    }
}
