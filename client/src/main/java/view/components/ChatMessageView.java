package view.components;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import view.menus.MainMenu;

public class ChatMessageView extends HBox {
    public interface UsernameClickedCallback {
        void usernameClicked(String username);
    }

    public interface ActionClickedCallback {
        void delete(int id);
        void edit(int id);
        void pin(int id);
    }

    private final int id;
    private final Label senderLabel = new Label(), messageLabel = new Label();
    private boolean pinned;

    public ChatMessageView(int id, String from, String message, UsernameClickedCallback usernameClickedCallback, ActionClickedCallback actionClickedCallback) {
        super(5);
        this.id = id;
        drawComponents(from, message, usernameClickedCallback, actionClickedCallback);
    }

    private void drawComponents(String from, String message, UsernameClickedCallback usernameClickedCallback, ActionClickedCallback actionClickedCallback) {
        senderLabel.setText(from + ":");
        senderLabel.setOnMouseClicked(x -> usernameClickedCallback.usernameClicked(from));
        senderLabel.setTextFill(Color.RED);
        messageLabel.setText(message);
        messageLabel.setMaxWidth(Double.POSITIVE_INFINITY);
        HBox.setHgrow(messageLabel, Priority.ALWAYS);
        super.setPadding(new Insets(5));
        super.getStyleClass().add("thin-rounded-card");
        super.setAlignment(Pos.CENTER_LEFT);
        super.getChildren().addAll(senderLabel, messageLabel);
        if (from.equals(MainMenu.loggedInUser.getUsername()))
            drawUserButtons(actionClickedCallback);
    }

    private void drawUserButtons(ActionClickedCallback actionClickedCallback) {
        JFXButton delete = new JFXButton("❌");
        delete.setOnMouseClicked(x -> actionClickedCallback.delete(id));
        delete.getStyleClass().add("jfx-button-buy-not-ok");
        JFXButton pin = new JFXButton("📌");
        pin.setOnMouseClicked(x -> actionClickedCallback.pin(id));
        pin.getStyleClass().add("jfx-button-blue");
        JFXButton edit = new JFXButton("✏");
        edit.setOnMouseClicked(x -> actionClickedCallback.edit(id));
        edit.getStyleClass().add("jfx-button-blue");
        super.getChildren().addAll(delete, pin, edit);
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setText(String text) {
        Platform.runLater(() -> messageLabel.setText(text));
    }

    public int getMessageId() {
        return id;
    }

    @Override
    public String toString() {
        return senderLabel.getText() + ": " + messageLabel.getText();
    }
}
