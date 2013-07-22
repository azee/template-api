package com.mycompany.template.api;

import com.mycompany.template.SomeBeanService;
import com.mycompany.template.beans.SomeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: azee
 * Date: 2/20/13
 * Time: 6:08 PM
 */
@Component
@Path("/some-bean")
public class SomeBeanRestService {

    @Autowired
    private SomeBeanService someBeanService;

    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/")
    public SomeBean updateSomeBean(SomeBean someBean) throws Exception {
        return someBeanService.saveSomeBean(someBean);
    }

    @PUT
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/")
    public SomeBean createSomeBean(SomeBean someBean) throws Exception {
        return someBeanService.saveSomeBean(someBean);
    }

    @DELETE
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/")
    public void deleteSomeBean(@QueryParam("id") String id) throws Exception {
        someBeanService.deleteSomeBean(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/{id}")
    public SomeBean getSomeBean(@PathParam("id") String id) throws Exception {
        return someBeanService.getSomeBena(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/all")
    public List<SomeBean> getAllSomeBeans() throws Exception {
        return someBeanService.getAllSomeBeans();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/limited")
    public List<SomeBean> findLimited(@QueryParam("skip") int skip, @QueryParam("limit") int limit) throws Exception {
        return someBeanService.findLimited(skip, limit, false);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/limited/simple")
    public List<SomeBean> findLimitedSimple(@QueryParam("skip") int skip, @QueryParam("limit") int limit) throws Exception {
        return someBeanService.findLimited(skip, limit, true);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/filtered")
    public List<SomeBean> getFiltered(@QueryParam("skip") int skip, @QueryParam("limit") int limit,
                                      @QueryParam("title") String title, @QueryParam("after") long after) throws Exception {
        return someBeanService.findFiltered(skip, limit, title, after);
    }
}
