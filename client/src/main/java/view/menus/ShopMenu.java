package view.menus;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXScrollPane;
import controller.menucontrollers.LoginMenuController;
import controller.menucontrollers.ShopMenuController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import model.cards.Card;
import model.cards.CardShopDetails;
import model.exceptions.NetworkErrorException;
import view.components.AlertsUtil;
import view.components.CardShopViewCard;
import view.components.UserBadge;
import view.global.SoundEffects;

import java.net.URL;
import java.util.*;

public class ShopMenu implements Initializable {
    private final ArrayList<CardShopViewCard> cards = new ArrayList<>();
    private CardShopViewCard selectedCard;
    private boolean isSelling;
    @FXML
    private Label dialogLabel;
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
        generateCards();
        JFXScrollPane.smoothScrolling(scrollPane);
        buyConfirmDialog.setDialogContainer(rootStackPane);
        updateUserBadge();
    }

    private void updateUserBadge() {
        userBadge.setUser(MainMenu.loggedInUser);
    }

    private void clickedBuy(CardShopViewCard cardView) {
        this.isSelling = false;
        this.selectedCard = cardView;
        dialogLabel.setText("Buy this card?");
        if (!cardView.canUserAffordCard())
            AlertsUtil.showError("You do not have enough money to buy this card!");
        else {
            buyDialogBody.setText(cardView.getBuyDialogMessage());
            buyConfirmDialog.show();
        }
    }

    private void clickedSell(CardShopViewCard cardView) {
        this.isSelling = true;
        this.selectedCard = cardView;
        dialogLabel.setText("Sell this card?");
        buyDialogBody.setText(cardView.getSellDialogMessage());
        buyConfirmDialog.show();
    }

    public void clickedBuyDialogClose(MouseEvent mouseEvent) {
        buyConfirmDialog.close();
    }

    public void clickedBuyDialogChange(MouseEvent mouseEvent) {
        try {
            if (isSelling)
                ShopMenuController.sellCard(selectedCard.getCard().getName());
            else
                ShopMenuController.buyCard(selectedCard.getCard().getName());
            MainMenu.loggedInUser = LoginMenuController.getUserByUsername(MainMenu.loggedInUser.getUsername());
            SoundEffects.playMedia(SoundEffects.CARD_BOUGHT);
        } catch (NetworkErrorException e) {
            AlertsUtil.showError(e);
            return;
        } finally {
            buyConfirmDialog.close();
        }
        updateUserBadge();
        generateCards();
        AlertsUtil.showSuccess("Card bought!");
    }

    public void clickedBackButton(MouseEvent mouseEvent) {
        SoundEffects.playMedia(SoundEffects.CLICK);
        SceneChanger.changeScene(MenuNames.MAIN);
    }

    private void generateCards() {
        cards.clear();
        masonryPane.getChildren().clear();
        new Thread(() -> {
            HashMap<String, CardShopDetails> result = ShopMenuController.getAllCards();
            result.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
                Card card = Card.getCardByName(entry.getKey());
                CardShopViewCard cardView = new CardShopViewCard(card, MainMenu.loggedInUser.getMoney(), this::clickedBuy, this::clickedSell, entry.getValue().isForbidden());
                cardView.setCardStockOfMarket(entry.getValue().getStock());
                cards.add(cardView);
            });
            Platform.runLater(() -> {
                masonryPane.getChildren().addAll(cards);
                scrollPane.requestLayout();
                rootStackPane.requestLayout();
            });
        }).start();
    }
}
