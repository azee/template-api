package com.mycompany.template.integration;

import com.mycompany.template.beans.SomeBean;
import com.mycompany.template.client.TemplateApiPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sun.jersey.api.client.Client;

import java.net.URI;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: azee
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:integration-beans.xml")
public class SomeBeanRestServiceTestIt {

    private static final String ENDPOINT = "http://localhost:9009/template-api";

    @Test
    public void testCreateSomeBean () throws Exception {
        TemplateApiPath.SomeBean someBeanApi = new TemplateApiPath.SomeBean(new Client(),new URI(ENDPOINT));
        SomeBean someBean = new SomeBean();
        someBean.setTime(new Date().getTime());
        someBean.setTitle("Some bean test");
        someBean = someBeanApi.putXmlAsSomeBean(someBean);

        assertNotNull(someBean);
        assertNotNull(someBean.getId());
        assertTrue("Bean id is null", !"".equals(someBean.getId()));

        SomeBean someBeanRestore = someBeanApi.id(someBean.getId()).getAsSomeBeanXml();

        assertNotNull(someBean);
        assertThat("Bean title is incorrect", someBean.getTitle(), is(someBean.getTitle()));
    }
}
