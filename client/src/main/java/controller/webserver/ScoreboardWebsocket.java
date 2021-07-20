package controller.webserver;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.menucontrollers.LoginMenuController;
import model.results.UserForScoreboard;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardWebsocket extends WebSocketClient {
    public interface Receiver {
        void onMessage(List<UserForScoreboard> users);
    }

    private final Receiver onMessageCallback;

    public ScoreboardWebsocket(String serverUri, Receiver onMessageCallback) {
        super(URI.create(serverUri), LoginMenuController.getHeaders());
        this.onMessageCallback = onMessageCallback;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
    }

    @Override
    public void onMessage(String s) {
        Type listOfMyClassObject = new TypeToken<ArrayList<UserForScoreboard>>() {
        }.getType();
        Gson gson = new Gson();
        onMessageCallback.onMessage(gson.fromJson(s, listOfMyClassObject));
    }

    @Override
    public void onClose(int i, String s, boolean b) {
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }
}
