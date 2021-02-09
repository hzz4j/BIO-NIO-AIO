package edu.hzz.webserver.server;

import edu.hzz.webserver.server.connector.Connector;

public class Bootstrap {
    public static void main(String[] args) {
        Connector connector = new Connector();
        connector.start();
    }
}
