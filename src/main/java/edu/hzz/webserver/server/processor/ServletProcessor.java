package edu.hzz.webserver.server.processor;

import edu.hzz.webserver.server.connector.*;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ServletProcessor {

    private URLClassLoader getURLClassLoader() throws MalformedURLException {
        File file = new File(ConnectionUtils.CLASS_ROOT);
        URL url = file.toURI().toURL();
        return new URLClassLoader(new URL[]{url});
    }

    private Servlet getServlet(URLClassLoader urlClassLoader,Request request) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        //  /servlet/TimeServlet
        String requestURI = request.getRequestURI();
        String servletName = requestURI.substring(requestURI.lastIndexOf("/")+1);
        //  edu.hzz.servlet.TimeServlet
        String fullPath = ConnectionUtils.SERVLET_PACKAGE+ConnectionUtils.DOT+servletName;

        Class<?> servletClass = urlClassLoader.loadClass(fullPath);
        Servlet servlet = (Servlet) servletClass.newInstance();
        return servlet;
    }

    public void process(Request request, Response response){
        try {
            URLClassLoader urlClassLoader = getURLClassLoader();
            Servlet servlet = getServlet(urlClassLoader, request);
            //  使用门面设计模式
            RequestFacade requestFacade = new RequestFacade(request);
            ResponseFacade responseFacade = new ResponseFacade(response);

            servlet.service(requestFacade,responseFacade);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
