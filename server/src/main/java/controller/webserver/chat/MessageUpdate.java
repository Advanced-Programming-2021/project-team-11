package controller.webserver.chat;

public class MessageUpdate {
    public enum MessageUpdateType {
        NEW,
        DELETE,
        EDIT,
        PIN;
    }

    private MessageUpdateType type;
    private ChatMessage message;

    public MessageUpdate() {

    }

    public MessageUpdate(ChatMessage message) {
        this.message = message;
        this.type = MessageUpdateType.NEW;
    }

    public MessageUpdate(ChatMessage message, MessageUpdateType type) {
        this.message = message;
        this.type = type;
    }

    public MessageUpdateType getType() {
        return type;
    }

    public ChatMessage getMessage() {
        return message;
    }
}
