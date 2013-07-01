package com.mycompany.template.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import org.codehaus.jackson.jaxrs.Annotations;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

/**
 * Created by IntelliJ IDEA.
 * User: azee
 * Date: 9/5/12
 * Time: 10:06 PM
 */

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Provider
public class PGJsonProducer extends JacksonJsonProvider {
    @Context HttpServletRequest servletRequest;
    public final static Annotations[] DEFAULT_ANNOTATIONS = {
        Annotations.JACKSON
    };

    public PGJsonProducer(){
        this(null, Annotations.JACKSON);
    }

    public PGJsonProducer(Annotations... annotationsToUse){
        this(null, annotationsToUse);
    }

    public PGJsonProducer(ObjectMapper mapper, Annotations[] annotationsToUse) {
        super(mapper, annotationsToUse);
        if (mapper==null){
            mapper=_mapperConfig.getDefaultMapper();
        }
        mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
     }

     @Override
    public long getSize(Object t, Class<?> type, Type type1, Annotation[] antns, MediaType mt){
        long sz=super.getSize(t, type, type1, antns, mt);
        String callback=servletRequest.getParameter("callback");
        if (sz>=0 && callback!=null){
            sz+=callback.length()+2;
        }
        return sz;
     }

     @Override
    public void writeTo(Object t, Class<?> type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap<String, Object> mm,
                OutputStream out) throws IOException, WebApplicationException{
        String callback=servletRequest.getParameter("callback");

        if (callback!=null) {
            out.write(callback.getBytes());
            out.write('(');
        }

        super.writeTo(t, type, type1, antns, mt, mm, out);

        if (callback!=null)
            out.write(')');
    }
}
