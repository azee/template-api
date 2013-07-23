package com.mycompany.template.service;

import com.mycompany.template.SomeBeanService;
import com.mycompany.template.beans.Property;
import com.mycompany.template.beans.SomeBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: azee
 * Date: 7/23/13
 * Time: 5:30 PM
  */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:services-test-context.xml")
public class SomeBeanServiceTest {

    @Autowired
    SomeBeanService someBeanService;

    /**
     * Builds filled SomeBean
     * @return SomeBean
     */
    private SomeBean buildSomeBean(){
        SomeBean someBean = new SomeBean();
        someBean.setTime(new Date().getTime());
        someBean.setTitle("Some Bean Test Title");
        someBean.setDescription("Some Bean Test Description");

        Property property = new Property();
        property.setTitle("Property 1");
        property.setKey("prop1");
        property.setValue("p1");
        someBean.getProperties().add(property);

        property = new Property();
        property.setTitle("Property 2");
        property.setKey("prop2");
        property.setValue("p2");
        someBean.getProperties().add(property);
        return someBean;
    }


    @Test
    public void testGetSomeBeans() throws Exception {
        SomeBean someBean = someBeanService.saveSomeBean(buildSomeBean());
        assertThat("Can't persist new someBean", someBean, notNullValue());

        someBean = someBeanService.getSomeBena(someBean.getId());
        assertThat("Can't get persisted someBean by id", someBean, notNullValue());

        assertThat("Wrong Title in persisted bean", someBean.getTitle(), is("Some Bean Test Title"));

        someBeanService.deleteSomeBean(someBean.getId());
        someBean = someBeanService.getSomeBena(someBean.getId());
        assertThat("Remove persisted someBean by id", someBean, nullValue());

    }


}
