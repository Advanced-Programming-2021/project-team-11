package view.menus;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class SceneChanger {
    private static Scene scene;

    public static void setScene(Scene scene) {
        SceneChanger.scene = scene;
    }

    public static Scene getScene() {
        return scene;
    }

    public static void changeScene(MenuNames menu) {
        try {
            Pane root = FXMLLoader.load(Objects.requireNonNull(RootMenu.class.getResource(menu.getFxmlPath())));
            SceneChanger.scene.setRoot(root);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
