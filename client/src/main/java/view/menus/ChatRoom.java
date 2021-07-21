package view.menus;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXTextField;
import controller.webserver.ChatMessage;
import controller.webserver.ChatWebsocket;
import controller.webserver.MessageUpdate;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.components.ChatMessageView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChatRoom implements Initializable {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private JFXTextField messageBox;
    @FXML
    private VBox messages;
    private final ArrayList<ChatMessageView> messagesData = new ArrayList<>();
    private ChatWebsocket websocket;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        websocket = new ChatWebsocket(new ChatWebsocket.MessageReceivedCallback() {
            @Override
            public void onMessage(MessageUpdate message) {
                ChatRoom.this.onMessage(message);
            }

            @Override
            public void onInitialize(List<ChatMessage> messages) {
                onInitializeMessages(messages);
            }
        });
        websocket.connect();
    }

    private void onInitializeMessages(List<ChatMessage> messages) {
        messages.forEach(this::addMessage);
        scrollPane.setVvalue(1);
    }

    private synchronized void onMessage(MessageUpdate update) {
        switch (update.getType()) {
            case NEW:
                addMessage(update.getMessage());
                break;
            case DELETE:
                deleteMessage(update.getMessage().getMessageId());
                break;
        }
        scrollPane.setVvalue(1);
    }

    private void addMessage(ChatMessage msg) {
        ChatMessageView messageView = new ChatMessageView(msg.getMessageId(), msg.getUsername(), msg.getMessage(), this::viewUserInfo);
        messagesData.add(messageView);
        VBox.setMargin(messageView, new Insets(5, 25, 5, 5)); // 25 for right because of scrollbar
        Platform.runLater(() -> messages.getChildren().add(messageView));
    }

    private void deleteMessage(int messageId) {
        for (int i = 0; i < messagesData.size(); i++) {
            if (messagesData.get(i).getMessageId() == messageId) {
                final ChatMessageView message = messagesData.get(i);
                Platform.runLater(() -> messages.getChildren().remove(message));
                messagesData.remove(i);
                i--;
            }
        }
    }

    private void viewUserInfo(String username) {
        UserInfoDialog.username = username;
        try {
            Stage stage = new Stage();
            stage.setTitle(username);
            Pane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/user_details.fxml")));
            Scene scene = new Scene(root, 550, 400);
            scene.getStylesheets().addAll("/style/global.css", "/style/tables.css");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendClicked(MouseEvent mouseEvent) {
        ChatMessage message = new ChatMessage();
        message.setMessage(messageBox.getText());
        MessageUpdate update = new MessageUpdate(message);
        websocket.send(new Gson().toJson(update));
        messageBox.setText("");
    }
}
