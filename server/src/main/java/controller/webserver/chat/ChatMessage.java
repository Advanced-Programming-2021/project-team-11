package controller.webserver.chat;

import java.util.concurrent.atomic.AtomicInteger;

public class ChatMessage {
    private final static AtomicInteger id = new AtomicInteger(0);
    private final int messageId;
    private final String username, message;

    public ChatMessage(String username, String message) {
        this.username = username;
        this.message = message;
        messageId = id.getAndIncrement();
    }
}
