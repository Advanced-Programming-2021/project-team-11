package controller.webserver.chat;

import com.google.gson.Gson;
import controller.webserver.TokenManager;
import io.javalin.websocket.WsContext;
import io.javalin.websocket.WsHandler;
import model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatController {
    private final static Gson gson = new Gson();
    private final static int MAX_MESSAGES_SIZE = 100;
    private final static Queue<ChatMessage> messages = new LinkedList<>();
    private final static HashMap<WsContext, User> websockets = new HashMap<>();

    private static void addMessage(User from, String message) {
        ChatMessage messageObject = new ChatMessage(from.getUsername(), message);
        synchronized (messages) {
            messages.add(messageObject);
            if (message.length() > MAX_MESSAGES_SIZE)
                messages.remove();
        }
        MessageUpdate update = new MessageUpdate(messageObject);
        synchronized (websockets) {
            websockets.keySet().stream().filter(set -> set.session.isOpen()).forEach(ws -> ws.send(gson.toJson(update)));
        }
    }

    private static ArrayList<ChatMessage> getMessages() {
        synchronized (messages) {
            return new ArrayList<>(messages);
        }
    }

    private static void deleteMessage(User from, int id) {
        boolean removed;
        synchronized (messages) {
            removed = messages.removeIf(msg -> msg.getMessageId() == id && msg.getUsername().equals(from.getUsername()));
        }
        if (!removed)
            return;
        ChatMessage message = new ChatMessage();
        message.setMessageId(id);
        MessageUpdate update = new MessageUpdate(message, MessageUpdate.MessageUpdateType.DELETE);
        synchronized (websockets) {
            websockets.keySet().stream().filter(set -> set.session.isOpen()).forEach(ws -> ws.send(gson.toJson(update)));
        }
    }

    private static void editMessage(User from, int id, String newText) {
        AtomicInteger editCounter = new AtomicInteger(0);
        synchronized (messages) {
            messages.stream().filter(msg -> msg.getMessageId() == id && msg.getUsername().equals(from.getUsername())).forEach(msg -> {
                msg.setMessage(newText);
                editCounter.incrementAndGet();
            });
        }
        if (editCounter.get() == 0)
            return;
        ChatMessage message = new ChatMessage();
        message.setMessageId(id);
        message.setMessage(newText);
        MessageUpdate update = new MessageUpdate(message, MessageUpdate.MessageUpdateType.EDIT);
        synchronized (websockets) {
            websockets.keySet().stream().filter(set -> set.session.isOpen()).forEach(ws -> ws.send(gson.toJson(update)));
        }
    }

    private static void pinMessage(User from, int id) {
        synchronized (messages) {
            AtomicInteger editCounter = new AtomicInteger(0);
            messages.stream().filter(msg -> msg.getMessageId() == id && msg.getUsername().equals(from.getUsername())).forEach(msg -> {
                msg.setPinned(true);
                editCounter.incrementAndGet();
            });
            if (editCounter.get() == 0)
                return;
            messages.forEach(msg -> msg.setPinned(false));
        }
        ChatMessage message = new ChatMessage();
        message.setMessageId(id);
        MessageUpdate update = new MessageUpdate(message, MessageUpdate.MessageUpdateType.PIN);
        synchronized (websockets) {
            websockets.keySet().stream().filter(set -> set.session.isOpen()).forEach(ws -> ws.send(gson.toJson(update)));
        }
    }

    public static void handleWebsocket(WsHandler websocket) {
        websocket.onConnect(ctx -> {
            User user = TokenManager.getInstance().getUser(ctx.header(TokenManager.TOKEN_HEADER));
            if (user == null) {
                ctx.session.close();
                return;
            }
            synchronized (websockets) {
                websockets.put(ctx, user);
            }
            // Send messages
            ctx.send(gson.toJson(getMessages()));
        });
        websocket.onMessage(ctx -> {
            User user;
            synchronized (websockets) {
                user = websockets.get(ctx);
            }
            MessageUpdate update = gson.fromJson(ctx.message().trim(), MessageUpdate.class);
            switch (update.getType()) {
                case NEW:
                    addMessage(user, update.getMessage().getMessage());
                    break;
                case EDIT:
                    editMessage(user, update.getMessage().getMessageId(), update.getMessage().getMessage());
                    break;
                case DELETE:
                    deleteMessage(user, update.getMessage().getMessageId());
                    break;
                case PIN:
                    pinMessage(user, update.getMessage().getMessageId());
            }
        });
        websocket.onClose(ctx -> {
            synchronized (websockets) {
                websockets.remove(ctx);
            }
        });
    }
}
