package controller.webserver;

public class ChatMessage {
    private int messageId;
    private String username;
    private String message;
    private boolean pinned = false;

    public ChatMessage() {

    }

    public ChatMessage(String username, String message) {
        this.username = username;
        this.message = message;
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
