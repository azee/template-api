package com.mycompany.template.api;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.aqua.beans.ConfigUtils;
import ru.yandex.aqua.beans.Role;
import ru.yandex.aqua.beans.User;
import ru.yandex.aqua.security.UserDetailsImpl;
import ru.yandex.aqua.service.UserService;
import com.mycompany.template.utils.UserDataUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: azee
 * Date: 7/10/12
 * Time: 3:53 PM
  */

@Component
@Path("/user")
public class UserRestService {
    private static final Logger log = LogManager.getLogger(LaunchRestService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ConfigUtils configUtils;

    @Autowired
    private UserDataUtils userDataUtils;


    private static final String LOOPBACK = "127.0.0.1";
    private static final String IP_HEADER = "X-Real-IP";


    @GET
    @Path("/authorise")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public User authorise(
            @QueryParam("login") final String login,
            @QueryParam("pass") final String pass,
            @Context HttpServletRequest hsr) throws Exception {
        String remoteAdd = hsr.getRemoteAddr();
        String forward = hsr.getHeader(IP_HEADER);
        if (LOOPBACK.equals(remoteAdd) && forward != null && !forward.isEmpty()) {
            remoteAdd = forward;
        }
        return userService.authorise(login,pass,remoteAdd);
    }


    @GET
    @Path("/sid")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public User checkSid(@QueryParam("cookie") final String cookie, @Context HttpServletRequest hsr) throws Exception {

        if (configUtils.getAuthEnabled() != null && configUtils.getAuthEnabled().equals("false")){
		    return userDataUtils.getUserWithoutAuth();
        }

        Cookie sidCookie = userDataUtils.getSidFromRequest(hsr);

        //Return if user is not authorised
        if (sidCookie == null && (cookie == null || cookie.equals(""))){
            return new User();
        }

        //Check custom send sid
        if (sidCookie == null){
            sidCookie = new Cookie(userDataUtils.getSidCookieName(), cookie);
        }

        String remoteAdd = hsr.getRemoteAddr();
        String forward = hsr.getHeader(IP_HEADER);
        if (LOOPBACK.equals(remoteAdd) && forward != null && !forward.isEmpty()) {
            remoteAdd = forward;
        }
        log.debug("forward Ip is : [" +forward + "]  remote addr: [ " + remoteAdd + "]");
        return userService.checkSid(sidCookie.getValue(), sidCookie.getDomain(), remoteAdd);
    }

    @GET
    @Path("/token")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public User getUserByToken(@QueryParam("token") final String token) throws Exception {
        return userService.getUserByToken(token);
    }

    /**
     * TODO: why do we return empty user entity here? don't we get orphan users this way?
     * @param token
     * @return
     * @throws Exception
     */
    @GET
    @Path("/logout")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public User logoutToken(@QueryParam("token") final String token) throws Exception {
        return userService.logoutToken(token);
    }

    @POST
    @Path("/favourite")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void addFavouriteService(
            @QueryParam("label") final String label,
            @Context HttpServletRequest hsr) throws Exception {

        User user = checkSid("", hsr);

        userService.addFavoriteService(user,label);
    }

    @POST
    @Path("/favourite/label")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void addFavouriteLabel(
            @QueryParam("label") final String label,
            @Context HttpServletRequest hsr) throws Exception {

        User user = checkSid("", hsr);

        userService.addFavoriteLabel(user,label);
    }

    @POST
    @Path("/favourite/bulk")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void addBulkFavouriteService(
            @QueryParam("services") final String services,
            @Context HttpServletRequest hsr) throws Exception {

        User user = checkSid("", hsr);
        userService.addBulkFavouriteService(services,user);
    }

    @DELETE
    @Path("/favourite")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void removeFavouriteService(
            @QueryParam("label") final String label,
            @Context HttpServletRequest hsr) throws Exception {

        User user = checkSid("", hsr);
        userService.removeFavouriteService(label,user);
    }

    @DELETE
    @Path("/favourite/label")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void removeFavouriteLabel(
            @QueryParam("label") final String label,
            @Context HttpServletRequest hsr) throws Exception {

        User user = checkSid("", hsr);
        userService.removeFavouriteLabel(label,user);
    }

    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<User> getAllUsers() throws Exception {
        return userService.getAllUsers();
    }

    @GET
    @Path("/name/{name}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public User getUserByName(@PathParam("name") String name) throws Exception {
        return userService.getUserByName(name);
    }


    @POST
    @Path("/roles/bulk")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void addBulkRolesService(
            @QueryParam("roles") final String roles,
            @QueryParam("name") final String name) throws Exception {

        User user = userService.getUserByName(name);
        userService.addBulkRoles(roles, user);
    }

    @DELETE
    @Path("/roles")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void removeRole(
            @QueryParam("role") final String role,
            @Context HttpServletRequest hsr) throws Exception {

        User user = checkSid("", hsr);
        userService.removeFavouriteService(role, user);
    }

    @GET
    @Path("/roles")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Role> getAllRoles (){
        return Arrays.asList(Role.values());
    }

}
