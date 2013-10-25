package com.mycompany.template.api;

import com.mycompany.template.beans.User;
import com.mycompany.template.services.UserService;
import com.mycompany.template.utils.UserDataUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;


/**
 * Created with IntelliJ IDEA.
 * User: azee
 */
public class UserRestService {
    @Autowired
    UserService userService;

    @Autowired
    UserDataUtils userDataUtils;

    @GET
    @Path("/authorise")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response authorise(
            @QueryParam("login") final String login,
            @QueryParam("pass") final String pass,
            @Context HttpServletRequest hsr) throws Exception {
        User user = userService.authenticate(login, pass);
        if (user == null){
            return Response.serverError().status(401).build();
        }
        return Response.ok(user).cookie(new NewCookie(userDataUtils.getSidCookieName(), user.getSid())).build();
    }
}
