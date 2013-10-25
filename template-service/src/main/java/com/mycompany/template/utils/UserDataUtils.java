package com.mycompany.template.utils;

import com.mycompany.template.beans.Role;
import com.mycompany.template.beans.User;
import com.mycompany.template.services.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: azee
 */
public class UserDataUtils {
    private static final Logger logger = Logger.getLogger(UserDataUtils.class);

    @Autowired
    UserService userService;

    private String SID_COOKIE_NAME = "JSESSIONID";

    public String getSidCookieName() {
        return SID_COOKIE_NAME;
    }

    /**
     * Return a default Unauthorised user
     * @return
     */
    public User getUserWithoutAuth(){
        User user = new User();
        user.setName("Unauthorised");
        user.setCookieExpire(Long.MAX_VALUE);
        user.setTokenExpire(Long.MAX_VALUE);
        user.setId(UUID.randomUUID().toString());
        user.setToken(Long.toString(new Date().getTime()));
        user.getRoles().clear();
        user.getRoles().add(Role.ROLE_GUEST);
        return user;
    }

    /**
     * Find a cookie
     * @param hsr
     * @return
     */
    public Cookie getSidFromRequest(HttpServletRequest hsr){
        Cookie sidCookie = null;
        //Searching for Session_id cookie
        if (hsr != null && hsr.getCookies() != null){
            for (Cookie requestCookie : hsr.getCookies()){
                if (requestCookie.getName().equals(SID_COOKIE_NAME)){
                    sidCookie = requestCookie;
                }
            }
        }
        return sidCookie;
    }

    /**
     * Return a user by cookie
     * @param hsr
     * @return
     * @throws Exception
     */
    public User getUserData(HttpServletRequest hsr) throws Exception {
        User user = new User();

        if ((hsr == null || hsr.getCookies() == null)){
            return user;
        }
        Cookie sid = getSidFromRequest(hsr);
        if (sid == null){
            return user;
        }

        //Authorise User by cookie
        return userService.checkSid(sid.getValue());
    }
}
