package view.menus;

import controller.webserver.ScoreboardWebsocket;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import model.results.DeckListTableResult;
import model.results.UserForScoreboard;
import view.global.SoundEffects;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ScoreboardMenu implements Initializable {
    private ScoreboardWebsocket websocket;
    private TableView<UserForScoreboard> table;
    public VBox tableContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table = new TableView<>();
        table.setRowFactory(tv -> new TableRow<UserForScoreboard>() {
            @Override
            public void updateItem(UserForScoreboard item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && item.getNickname().equals(MainMenu.loggedInUser.getNickname()))
                    setStyle(getStyle() + "; -fx-background-color: #81C784;");
            }
        });
        TableColumn<UserForScoreboard, Integer> rankColumn = new TableColumn<>("Rank");
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        TableColumn<UserForScoreboard, String> usernameColumn = new TableColumn<>("Nickname");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        usernameColumn.setPrefWidth(200);
        TableColumn<UserForScoreboard, Integer> highscoreColumn = new TableColumn<>("Score");
        highscoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        highscoreColumn.setPrefWidth(100);
        TableColumn<UserForScoreboard, Boolean> onlineStatus = new TableColumn<>("Online");
        onlineStatus.setPrefWidth(50);
        onlineStatus.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isOnline()));
        onlineStatus.setCellFactory(col -> new TableCell<UserForScoreboard, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setAlignment(Pos.CENTER);
                setText(empty ? null : (item ? "✅" : "❌"));
            }
        });
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table.getColumns().add(rankColumn);
        table.getColumns().add(usernameColumn);
        table.getColumns().add(highscoreColumn);
        table.getColumns().add(onlineStatus);
        table.setPrefSize(600, 600);
        tableContainer.getChildren().add(0, table);
        // Websocket
        websocket = new ScoreboardWebsocket("ws://127.0.0.1:8888/users/scoreboard", this::onMessage);
        websocket.connect();
    }

    private void onMessage(List<UserForScoreboard> users) {
        table.setItems(FXCollections.observableArrayList(users));
    }

    public void clickedBackButton(MouseEvent mouseEvent) {
        SoundEffects.playMedia(SoundEffects.CLICK);
        SceneChanger.changeScene(MenuNames.MAIN);
        websocket.close();
    }
}
