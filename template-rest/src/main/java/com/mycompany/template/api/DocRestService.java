package com.mycompany.template.api;

import com.sun.jersey.server.wadl.WadlApplicationContext;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;

/**
 * Created by azee on 4/22/14.
 */
@Path("/doc")
public class DocRestService {

    @Context
    WadlApplicationContext wadlContext;

    /**
     * Get API documentation
     */
    @GET
    @Path("/")
    @Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_XML})
    public String getDoc() throws IOException {
        StringWriter writer = new StringWriter();

        //Transforming wadl
        try {
            InputStream xslIs = getClass().getResourceAsStream(
                    "/wadl/wadl.xsl");

            InputStream result = new URL("http://localhost:9001/template-api/application.wadl").openStream();

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(
                    new javax.xml.transform.stream.StreamSource(xslIs));
            transformer.transform(
                    new javax.xml.transform.stream.StreamSource(result),
                    new javax.xml.transform.stream.StreamResult(writer));
        } catch (Exception e) {return "";}

        return writer.toString();
    }
}
