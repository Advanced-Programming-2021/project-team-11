package view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ChatMessageView extends HBox {
    public interface UsernameClickedCallback {
        void usernameClicked(String username);
    }

    private final int id;
    private final Label senderLabel = new Label(), messageLabel = new Label();

    public ChatMessageView(int id, String from, String message, UsernameClickedCallback usernameClickedCallback) {
        super(5);
        this.id = id;
        drawComponents(from, message, usernameClickedCallback);
    }

    private void drawComponents(String from, String message, UsernameClickedCallback usernameClickedCallback) {
        senderLabel.setText(from + ":");
        senderLabel.setOnMouseClicked(x -> usernameClickedCallback.usernameClicked(from));
        senderLabel.setTextFill(Color.RED);
        messageLabel.setText(message);
        super.setPadding(new Insets(5));
        super.getStyleClass().add("thin-rounded-card");
        super.setAlignment(Pos.CENTER_LEFT);
        super.getChildren().addAll(senderLabel, messageLabel);
    }

    public int getMessageId() {
        return id;
    }
}
