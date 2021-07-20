package controller.webserver;

import controller.menucontrollers.LoginMenuController;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class PresenceWebsocket extends WebSocketClient {
    public PresenceWebsocket() {
        super(URI.create("ws://127.0.0.1:8888/users/presence"), LoginMenuController.getHeaders());
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
