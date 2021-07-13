package controller.webserver.chat;

import java.util.concurrent.atomic.AtomicInteger;

public class ChatMessage {
    private final static AtomicInteger id = new AtomicInteger(0);
    private int messageId;
    private String username, message;
    private boolean pinned = false;

    public ChatMessage() {

    }

    public ChatMessage(String username, String message) {
        this.username = username;
        this.message = message;
        messageId = id.getAndIncrement();
    }

    public String getUsername() {
        return username;
    }

    public int getMessageId() {
        return messageId;
    }

    public String getMessage() {
        return message;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }
}
