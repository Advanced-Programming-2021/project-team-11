package view.menus;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controller.menucontrollers.ProfileMenuController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import model.exceptions.*;
import view.components.AlertsUtil;
import view.global.Assets;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class ProfileMenu implements Initializable {
    @FXML
    private BorderPane rootPane;
    @FXML
    private ImageView profilePic;
    @FXML
    private Text usernameText;
    @FXML
    private Text nicknameText;
    @FXML
    private JFXDialog passwordDialog;
    @FXML
    private StackPane dialogsStack;
    @FXML
    private JFXPasswordField oldPasswordText;
    @FXML
    private JFXPasswordField newPasswordText;
    @FXML
    private JFXPasswordField newPasswordTextConfirm;
    @FXML
    private JFXDialog nicknameDialog;
    @FXML
    private JFXTextField newNicknameText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Assets.setMenuBackgroundImage(rootPane);
        profilePic.setImage(MainMenu.loggedInUser.getProfilePicImage());
        usernameText.setText("Username:\n" + MainMenu.loggedInUser.getUsername());
        nicknameText.setText("Nickname:\n" + MainMenu.loggedInUser.getNickname());
        passwordDialog.setDialogContainer(dialogsStack);
        nicknameDialog.setDialogContainer(dialogsStack);
    }

    public void clickedBackButton(MouseEvent mouseEvent) {
        SceneChanger.changeScene(MenuNames.MAIN);
    }

    public void clickedUploadFile(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Your Profile Pic");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpeg", "*.jpg"));
        File image = fileChooser.showOpenDialog(RootMenu.primaryStage);
        if (image != null) {
            try {
                final byte[] imageBytes = Files.readAllBytes(image.toPath());
                MainMenu.loggedInUser.setProfilePicBytes(imageBytes);
                profilePic.setImage(MainMenu.loggedInUser.getProfilePicImage());
                new Thread(() -> ProfileMenuController.setProfilePic(imageBytes)).start();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void clickedRemoveProfilePic(MouseEvent mouseEvent) {
        MainMenu.loggedInUser.setProfilePicBytes(null);
        profilePic.setImage(MainMenu.loggedInUser.getProfilePicImage());
        new Thread(() -> ProfileMenuController.setProfilePic(null)).start();
    }

    public void clickedChangePassword(MouseEvent mouseEvent) {
        oldPasswordText.setText("");
        newPasswordText.setText("");
        newPasswordTextConfirm.setText("");
        passwordDialog.show();
    }

    public void clickedPasswordDialogClose(MouseEvent mouseEvent) {
        passwordDialog.close();
    }

    public void clickedPasswordDialogChange(MouseEvent mouseEvent) {
        try {
            ProfileMenuController.changePassword(oldPasswordText.getText(), newPasswordText.getText(), newPasswordTextConfirm.getText());
            passwordDialog.close();
            AlertsUtil.showSuccess("Password changed!");
        } catch (NetworkErrorException e) {
            AlertsUtil.showError(e);
        }
    }

    public void clickedChangeNickname(MouseEvent mouseEvent) {
        newNicknameText.setText("");
        nicknameDialog.show();
    }

    public void clickedNicknameDialogClose(MouseEvent mouseEvent) {
        nicknameDialog.close();
    }

    public void clickedNicknameDialogChange(MouseEvent mouseEvent) {
        try {
            ProfileMenuController.changeNickname(newNicknameText.getText());
            MainMenu.loggedInUser.setNickname(newNicknameText.getText());
            nicknameDialog.close();
            nicknameText.setText("Nickname:\n" + MainMenu.loggedInUser.getNickname());
            AlertsUtil.showSuccess("Nickname changed!");
        } catch (NicknameExistsException e) {
            AlertsUtil.showError(e);
        }
    }
}
