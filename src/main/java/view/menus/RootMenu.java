package view.menus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Objects;

public class RootMenu extends Application {
    private static Scene scene;

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
        Pane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/root_menu.fxml")));
        scene = new Scene(root, 600, 600);
        scene.getStylesheets().add("/style/global.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
