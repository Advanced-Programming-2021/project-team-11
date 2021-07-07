package view.menus;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import controller.GameController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import model.User;
import model.enums.CoinFlipResult;
import model.enums.GameRounds;
import model.exceptions.UserDeckIsInvalidException;
import model.exceptions.UserHaveNoActiveDeckException;
import view.animation.CoinFlip;
import view.components.*;

import java.net.URL;
import java.util.ResourceBundle;

public class DuelStartMenu implements Initializable {
    private static User guest;
    private static GameRounds rounds;
    private static CoinFlipResult bet;
    public JfxCursorButton dialogCancelButton, dialogTailsButton, dialogHeadsButton;
    @FXML
    private HBox dialogContainer;
    @FXML
    private JFXDialog coinFlipDialog;
    @FXML
    private StackPane dialogsStack;
    @FXML
    private JFXTextField otherName;
    @FXML
    private BorderPane rootPane;
    @FXML
    private UserBadge userBadge;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Assets.setMenuBackgroundImage(rootPane);
        userBadge.setUser(MainMenu.loggedInUser);
        coinFlipDialog.setDialogContainer(dialogsStack);
    }

    public void clickedSingleButton(MouseEvent mouseEvent) {
        startGame(GameRounds.ONE);
    }

    public void clickedMatchButton(MouseEvent mouseEvent) {
        startGame(GameRounds.THREE);
    }

    private void startGame(GameRounds rounds) {
        DuelStartMenu.rounds = rounds;
        guest = User.getUserByUsername(otherName.getText());
        if (guest == null) {
            AlertsUtil.showError("User with this username does not exists!");
            return;
        }
        if (guest == MainMenu.loggedInUser) {
            AlertsUtil.showError("You played yourself!");
            return;
        }
        try {
            MainMenu.loggedInUser.validateUserActiveDeck();
            guest.validateUserActiveDeck();
        } catch (UserHaveNoActiveDeckException | UserDeckIsInvalidException e) {
            AlertsUtil.showError(e);
            return;
        }
        dialogContainer.getChildren().clear();
        dialogContainer.getChildren().add(new CoinFlip(this::coinFlippedCallback));
        coinFlipDialog.show();
    }

    private void coinFlippedCallback(CoinFlipResult result) {
        DuelMenu.player1 = result == bet ? MainMenu.loggedInUser : guest;
        DuelMenu.player2 = result == bet ? guest : MainMenu.loggedInUser;
        AlertsUtil.showSuccess(DuelMenu.player1.getNickname() + " is the starter!", () -> {
            DuelMenu.gameController = new GameController(DuelMenu.player1, DuelMenu.player2, rounds);
            DuelMenu.gameController.setupNewRound();
            SceneChanger.changeScene(MenuNames.DUEL);
        });
    }

    public void clickedBackButton(MouseEvent mouseEvent) {
        SceneChanger.changeScene(MenuNames.MAIN);
    }

    public void clickedDialogClose(MouseEvent mouseEvent) {
        coinFlipDialog.close();
    }

    public void clickedDialogTails(MouseEvent mouseEvent) {
        bet = CoinFlipResult.TAIL;
        flip();
    }

    public void clickedDialogHeads(MouseEvent mouseEvent) {
        bet = CoinFlipResult.HEAD;
        flip();
    }

    private void flip() {
        ((CoinFlip) dialogContainer.getChildren().get(0)).flip();
        dialogCancelButton.setDisable(true);
        dialogHeadsButton.setDisable(true);
        dialogTailsButton.setDisable(true);
    }
}
