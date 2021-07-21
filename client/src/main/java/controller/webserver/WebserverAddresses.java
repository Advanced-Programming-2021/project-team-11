package controller.webserver;

public class WebserverAddresses {
    private final static String WEB_SERVER_RAW_ADDRESS = "127.0.0.1:8080";
    public final static String WEB_SERVER_HTTP_ADDRESS = "http://" + WEB_SERVER_RAW_ADDRESS;
    public final static String WEB_SERVER_WEBSOCKET_ADDRESS = "ws://" + WEB_SERVER_RAW_ADDRESS;
}
