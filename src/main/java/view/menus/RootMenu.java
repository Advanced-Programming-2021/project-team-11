package view.menus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
        scene.getStylesheets().add("/style/global.css");
        scene.getStylesheets().add("/style/tables.css");
        primaryStage.setScene(scene);
        primaryStage.show();
        SceneChanger.setScene(scene);
        RootMenu.primaryStage = primaryStage;
    }

    public void clickedLoginButton(MouseEvent mouseEvent) {
        SceneChanger.changeScene(MenuNames.LOGIN);
    }

    public void clickedRegisterButton(MouseEvent mouseEvent) {
        SceneChanger.changeScene(MenuNames.REGISTER);
    }
}
