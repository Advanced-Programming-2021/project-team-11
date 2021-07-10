package view.menus;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXScrollPane;
import controller.menucontrollers.DeckMenuController;
import controller.menucontrollers.ShopMenuController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import model.cards.Card;
import model.exceptions.BooAnException;
import model.exceptions.CardNotExistsException;
import model.exceptions.InsufficientBalanceException;
import view.components.AlertsUtil;
import view.components.CardShopViewCard;
import view.components.UserBadge;
import view.global.SoundEffects;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ShopMenu implements Initializable {
    private final ArrayList<CardShopViewCard> cards = new ArrayList<>();
    private CardShopViewCard selectedCard;
    @FXML
    private StackPane rootStackPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private JFXMasonryPane masonryPane;
    @FXML
    private JFXDialog buyConfirmDialog;
    @FXML
    private Label buyDialogBody;
    @FXML
    private UserBadge userBadge;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (Card card : Card.getAllCards()) {
            CardShopViewCard cardView = new CardShopViewCard(card, MainMenu.loggedInUser.getMoney(), this::clickedBuy);
            cardView.setCardStockOfPlayer(MainMenu.loggedInUser.getAvailableCards().stream().filter(x -> x == card).count());
            cards.add(cardView);
        }
        masonryPane.getChildren().addAll(cards);
        Platform.runLater(() -> scrollPane.requestLayout());
        JFXScrollPane.smoothScrolling(scrollPane);
        buyConfirmDialog.setDialogContainer(rootStackPane);
        userBadge.setOnMouseClicked(e -> {
            if (e.isAltDown())
                DeckMenuController.addAllCardsToNewDeck(MainMenu.loggedInUser);
            if (e.isControlDown()) {
                ShopMenuController.addAllCardsCheat(MainMenu.loggedInUser);
                Platform.runLater(() -> cards.forEach(CardShopViewCard::increaseUserStock));
            }
        });
        updateUserBadge();
    }

    private void updateUserBadge() {
        userBadge.setUser(MainMenu.loggedInUser);
    }

    private void clickedBuy(CardShopViewCard cardView) {
        this.selectedCard = cardView;
        if (!cardView.canUserAffordCard())
            AlertsUtil.showError("You do not have enough money to buy this card!");
        else {
            buyDialogBody.setText(cardView.getBuyDialogMessage());
            buyConfirmDialog.show();
        }
    }

    public void clickedBuyDialogClose(MouseEvent mouseEvent) {
        buyConfirmDialog.close();
    }

    public void clickedBuyDialogChange(MouseEvent mouseEvent) {
        try {
            ShopMenuController.buyCardForUser(MainMenu.loggedInUser, selectedCard.getCard().getName());
            SoundEffects.playMedia(SoundEffects.CARD_BOUGHT);
        } catch (CardNotExistsException | InsufficientBalanceException e) {
            throw new BooAnException(e);
        }
        cards.forEach(card -> card.setUserBalance(MainMenu.loggedInUser.getMoney()));
        selectedCard.increaseUserStock();
        updateUserBadge();
        buyConfirmDialog.close();
        AlertsUtil.showSuccess("Card bought!");
    }

    public void clickedBackButton(MouseEvent mouseEvent) {
        SoundEffects.playMedia(SoundEffects.CLICK);
        SceneChanger.changeScene(MenuNames.MAIN);
    }
}