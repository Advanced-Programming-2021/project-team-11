package view.menus;

import controller.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.User;
import model.enums.GameRounds;

import java.util.Objects;

public class RootMenu extends Application {
    public static Stage primaryStage;
    // Default constructor; DO NOT REMOVE
    public RootMenu() {
    }

    public RootMenu(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        primaryStage.setTitle("Yo-Gi-Oh!");
        Pane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(MenuNames.ROOT.getFxmlPath())));
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().addAll("/style/global.css", "/style/tables.css");
        primaryStage.setScene(scene);
        primaryStage.show();
        SceneChanger.setScene(scene);
        RootMenu.primaryStage = primaryStage;
    }

    public void clickedLoginButton(MouseEvent mouseEvent) {
        SceneChanger.changeScene(MenuNames.LOGIN);
    }

    public void clickedRegisterButton(MouseEvent mouseEvent) {
        // TODO: remove
        User u1 = User.getUserByUsername("1");
        User u2 = User.getUserByUsername("2");
        DuelMenu.player1 = u1;
        DuelMenu.player2 = u2;
        DuelMenu.gameController = new GameController(u1, u2, GameRounds.ONE);
        DuelMenu.gameController.setupNewRound();
        SceneChanger.changeScene(MenuNames.DUEL);
        //SceneChanger.changeScene(MenuNames.REGISTER);
    }
}
