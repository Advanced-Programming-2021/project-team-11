package view.menus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import view.global.Assets;
import view.global.SoundEffects;

import java.util.Objects;

public class RootMenu extends Application {
    public static Stage primaryStage;

    // Default constructor; DO NOT REMOVE
    @SuppressWarnings("unused")
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
        // Warmup
        Util.forceInit(Assets.class);
        Util.forceInit(SoundEffects.class);
    }

    public void clickedLoginButton(MouseEvent mouseEvent) {
        SoundEffects.playMedia(SoundEffects.CLICK);
        SceneChanger.changeScene(MenuNames.LOGIN);
    }

    public void clickedRegisterButton(MouseEvent mouseEvent) {
        SoundEffects.playMedia(SoundEffects.CLICK);
        SceneChanger.changeScene(MenuNames.REGISTER);
    }
}
