
package com.mycompany.template;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.HandlerCollection;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.handler.RequestLogHandler;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.thread.QueuedThreadPool;
import org.mortbay.thread.ThreadPool;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: azee
 * Date: 7/12/12
 * Time: 7:07 PM
 */
public class Main {
    private static final int PORT = 9001;
    private static final String WEB_XML =
            "META-INF/webapp/WEB-INF/web.xml";

    public static void main(String[] args) throws Exception {
        Server server;
        server = new Server();
        server.setThreadPool(createThreadPool());
        server.addConnector(createConnector());
        server.setHandler(createHandlers());
        server.setStopAtShutdown(true);
        server.start();
    }

    private static ThreadPool createThreadPool()
    {
        // TODO: You should configure these appropriately
        // for your environment - this is an example only
        QueuedThreadPool _threadPool = new QueuedThreadPool();
        _threadPool.setMinThreads(10);
        _threadPool.setMaxThreads(100);
        return _threadPool;
    }

    private static SelectChannelConnector createConnector()
    {
        SelectChannelConnector _connector =
                new SelectChannelConnector();
        _connector.setPort(PORT);
        _connector.setHost("localhost");
        return _connector;
    }

    private static HandlerCollection createHandlers()
    {
        WebAppContext _ctx = new WebAppContext();
        _ctx.setContextPath("/");
        _ctx.setWar(getShadedWarUrl());
        List<Handler> _handlers = new ArrayList<Handler>();

        _handlers.add(_ctx);

        HandlerList _contexts = new HandlerList();
        _contexts.setHandlers(_handlers.toArray(new Handler[0]));

        RequestLogHandler _log = new RequestLogHandler();

        HandlerCollection _result = new HandlerCollection();
        _result.setHandlers(new Handler[] {_contexts, _log});

        return _result;
    }

    private static String getShadedWarUrl()
    {
        String _urlStr = getResource(WEB_XML).toString();
        // Strip off "WEB-INF/web.xml"
        return _urlStr.substring(0, _urlStr.length() - 15);
    }

    private static URL getResource(String aResource)
    {
        return Thread.currentThread().
                getContextClassLoader().getResource(aResource);
    }

}
