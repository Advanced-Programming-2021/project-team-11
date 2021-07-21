package view.menus;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXTextField;
import controller.webserver.ChatMessage;
import controller.webserver.ChatWebsocket;
import controller.webserver.MessageUpdate;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.components.AlertsUtil;
import view.components.ChatMessageView;
import view.components.UserBadge;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ChatRoom implements Initializable {
    @FXML
    private Label onlineUsersCount;
    @FXML
    private UserBadge userBadge;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private JFXTextField messageBox;
    @FXML
    private VBox messages;
    private final ArrayList<ChatMessageView> messagesData = new ArrayList<>();
    private ChatWebsocket websocket;
    private Timeline onlineUserUpdater;

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
        userBadge.setUser(MainMenu.loggedInUser);
        onlineUserUpdater = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        event -> new Thread(() -> {
                            int onlineUsers = ChatWebsocket.getOnlineUsers();
                            Platform.runLater(() -> onlineUsersCount.setText("Online users: " + onlineUsers));
                        }).start()));
        onlineUserUpdater.setCycleCount(Timeline.INDEFINITE);
        onlineUserUpdater.play();
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
            case PIN:
                messagesData.forEach(msg -> msg.setPinned(update.getMessage().getMessageId() == msg.getMessageId()));
                break;
            case EDIT:
                messagesData.stream().filter(msg -> msg.getMessageId() == update.getMessage().getMessageId()).forEach(msg -> msg.setText(update.getMessage().getMessage()));
                break;
        }
        scrollPane.setVvalue(1);
    }

    private void addMessage(ChatMessage msg) {
        ChatMessageView messageView = new ChatMessageView(msg.getMessageId(), msg.getUsername(), msg.getMessage(), this::viewUserInfo, new ChatMessageView.ActionClickedCallback() {
            @Override
            public void delete(int id) {
                ChatMessage message = new ChatMessage();
                message.setMessageId(id);
                websocket.send(new Gson().toJson(new MessageUpdate(message, MessageUpdate.MessageUpdateType.DELETE)));
            }

            @Override
            public void edit(int id) {
                String edit = AlertsUtil.getTextAlert("Your edit:");
                ChatMessage message = new ChatMessage();
                message.setMessageId(id);
                message.setMessage(edit);
                websocket.send(new Gson().toJson(new MessageUpdate(message, MessageUpdate.MessageUpdateType.EDIT)));
            }

            @Override
            public void pin(int id) {
                ChatMessage message = new ChatMessage();
                message.setMessageId(id);
                websocket.send(new Gson().toJson(new MessageUpdate(message, MessageUpdate.MessageUpdateType.PIN)));
            }
        });
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

    public void clickedBackButton(MouseEvent mouseEvent) {
        onlineUserUpdater.stop();
        websocket.close();
        SceneChanger.changeScene(MenuNames.MAIN);
    }

    public void clickedPinnedButton(MouseEvent mouseEvent) {
        Optional<ChatMessageView> message = messagesData.stream().findFirst().filter(ChatMessageView::isPinned);
        AlertsUtil.showSuccess(message.isPresent() ? message.get().toString() : "No pinned message");
    }
}
