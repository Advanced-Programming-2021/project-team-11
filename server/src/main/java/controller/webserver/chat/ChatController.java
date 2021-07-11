package controller.webserver.chat;

import com.google.gson.Gson;
import controller.webserver.TokenManager;
import io.javalin.websocket.WsContext;
import io.javalin.websocket.WsHandler;
import model.User;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

public class ChatController {
    private final static int MAX_MESSAGES_SIZE = 100;
    private final static Deque<ChatMessage> messages = new LinkedList<>();
    private final static HashMap<WsContext, User> websockets = new HashMap<>();

    private static void addMessage(User from, String message) {
        ChatMessage messageObject = new ChatMessage(from.getUsername(), message);
        synchronized (messages) {
            messages.add(messageObject);
            if (message.length() > MAX_MESSAGES_SIZE)
                messages.removeFirst();
        }
        Gson gson = new Gson();
        synchronized (websockets) {
            websockets.entrySet().stream().filter(set -> set.getKey().session.isOpen() && set.getValue() != null).forEach(ws -> ws.getKey().send(gson.toJson(messageObject)));
        }
    }

    private static ArrayList<ChatMessage> getMessages() {
        synchronized (messages) {
            return new ArrayList<>(messages);
        }
    }

    public static void handleWebsocket(WsHandler websocket) {
        websocket.onConnect(ctx -> {
            synchronized (websockets) {
                websockets.put(ctx, null);
            }
        });
        websocket.onMessage(ctx -> {
            User user = websockets.get(ctx);
            if (user == null) { // Try to authorize
                user = TokenManager.getInstance().getUser(ctx.message().trim());
                if (user == null) {
                    ctx.send("sike");
                    ctx.session.close();
                } else {
                    synchronized (websockets) {
                        websockets.put(ctx, user);
                    }
                    // Send old messages to user
                    ctx.send(new Gson().toJson(getMessages()));
                }
                return;
            }
            // Otherwise this is a simple message
            addMessage(user, ctx.message().trim());
        });
        websocket.onClose(ctx -> {
            synchronized (websockets) {
                websockets.remove(ctx);
            }
        });
    }
}
