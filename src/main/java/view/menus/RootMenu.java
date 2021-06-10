package view.menus;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import view.components.Assets;

import java.util.Objects;

public class RootMenu extends Application {
    @FXML
    ImageView loginButton;
    @FXML
    ImageView registerButton;

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
        primaryStage.setScene(scene);
        primaryStage.show();
        SceneChanger.setScene(scene);
    }

    public void enterLoginButton(MouseEvent mouseEvent) {
        loginButton.setImage(Assets.SELECTED_BUTTON_IMAGE);
    }

    public void exitLoginButton(MouseEvent mouseEvent) {
        loginButton.setImage(Assets.BUTTON_IMAGE);
    }

    public void enterRegisterButton(MouseEvent mouseEvent) {
        registerButton.setImage(Assets.SELECTED_BUTTON_IMAGE);
    }

    public void exitRegisterButton(MouseEvent mouseEvent) {
        registerButton.setImage(Assets.BUTTON_IMAGE);
    }
}
