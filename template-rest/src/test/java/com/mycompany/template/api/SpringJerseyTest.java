package com.mycompany.template.api;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerException;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;
import org.junit.After;
import org.junit.Before;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

/**
 * Created with IntelliJ IDEA.
 * User: azee
 * Date: 7/12/13
 * Time: 2:21 PM
 */
public class SpringJerseyTest extends JerseyTest{

    public static final AppDescriptor APP_DESCRIPTOR = new WebAppDescriptor.Builder("com.ycompany.template.api")
            .contextParam("contextConfigLocation", "classpath:/web-test-context.xml")
            .contextPath("/").servletClass(SpringServlet.class)
            .contextListenerClass(ContextLoaderListener.class)
            .requestListenerClass(RequestContextListener.class)
            .build();

    private static TestContainerFactory tcf = null;

    public static int bCounter = 0;
    public static int aCounter = 0;


    public SpringJerseyTest () {
        super(APP_DESCRIPTOR);
    }

    @Override
    protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
        if (tcf == null ) {
            tcf = new OnePerAppDescriptorTestContainerFactory(super.getTestContainerFactory());
        }
        return tcf;
    }


    @Before
    public void setUp() throws Exception {
        if (aCounter == 0) {
            super.setUp();
            aCounter++;
        }

    }

    /**
     * Tear down the test by invoking {@link com.sun.jersey.test.framework.spi.container.TestContainer#stop() } on
     * the test container obtained from the test container factory.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {

    }
}
