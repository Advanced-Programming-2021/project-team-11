package controller.webserver;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.menucontrollers.LoginMenuController;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatWebsocket extends WebSocketClient {
    public interface MessageReceivedCallback {
        void onMessage(MessageUpdate message);

        void onInitialize(List<ChatMessage> messages);
    }

    private final AtomicBoolean firstMessage = new AtomicBoolean(true);
    private final MessageReceivedCallback messageReceivedCallback;

    public ChatWebsocket(MessageReceivedCallback onMessageReceived) {
        super(URI.create("ws://127.0.0.1:8888/chat"), LoginMenuController.getHeaders());
        this.messageReceivedCallback = onMessageReceived;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
    }

    @Override
    public void onMessage(String s) {
        Gson gson = new Gson();
        if (firstMessage.getAndSet(false)) {
            Type listOfMyClassObject = new TypeToken<ArrayList<ChatMessage>>() {
            }.getType();
            messageReceivedCallback.onInitialize(gson.fromJson(s, listOfMyClassObject));
        } else {
            messageReceivedCallback.onMessage(gson.fromJson(s, MessageUpdate.class));
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

    public static int getOnlineUsers() {
        try {
            HttpResponse<String> response = Unirest.get("/users/presence").asString();
            return Integer.parseInt(response.getBody());
        } catch (Exception ex) {
            return 0;
        }
    }
}
