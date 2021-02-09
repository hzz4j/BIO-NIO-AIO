package edu.hzz.servlet;

import edu.hzz.webserver.server.connector.ConnectionUtils;
import edu.hzz.webserver.server.connector.HttpStatus;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeServlet implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        Writer out;
        PrintWriter writer = servletResponse.getWriter();
        writer.print(ConnectionUtils.renderStatus(HttpStatus.SC_OK));
        writer.println("What time is it now?");
        writer.println(DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .format(LocalDateTime.now()));
        writer.close();
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
