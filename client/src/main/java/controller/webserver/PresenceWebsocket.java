package controller.webserver;

import controller.menucontrollers.LoginMenuController;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class PresenceWebsocket extends WebSocketClient {
    public PresenceWebsocket() {
        super(URI.create(WebserverAddresses.WEB_SERVER_WEBSOCKET_ADDRESS +"/users/presence"), LoginMenuController.getHeaders());
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }

    @Override
    public void onMessage(String s) {

    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }
}
