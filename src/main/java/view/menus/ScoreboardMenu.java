package view.menus;

import controller.menucontrollers.ScoreboardMenuController;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import model.results.UserForScoreboard;

import java.net.URL;
import java.util.ResourceBundle;

public class ScoreboardMenu implements Initializable {
    public VBox tableContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<UserForScoreboard> scoreboard = ScoreboardMenuController.getScoreboardRows();
        TableView<UserForScoreboard> table = new TableView<>();
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
        table.setItems(scoreboard);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table.getColumns().add(rankColumn);
        table.getColumns().add(usernameColumn);
        table.getColumns().add(highscoreColumn);
        table.setPrefSize(600, 600);
        tableContainer.getChildren().add(0, table);
    }

    public void clickedBackButton(MouseEvent mouseEvent) {
        SceneChanger.changeScene(MenuNames.MAIN);
    }
}
