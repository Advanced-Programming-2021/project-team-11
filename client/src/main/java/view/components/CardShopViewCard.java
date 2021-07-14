package view.components;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.cards.Card;
import view.global.Assets;
import view.menus.SceneChanger;

public class CardShopViewCard extends HBox {
    private final Card card;
    private boolean canAffordThisCard;
    private final boolean isForbidden;
    private final ImageView cardImageView = new ImageView();
    private final Text cardNameText = new Text(), cardPriceText = new Text(), cardTypeText = new Text(),
            cardDescriptionText = new Text(), playerStock = new Text();
    private final JFXButton buyButton = new JFXButton("Buy"), sellButton = new JFXButton("Sell");

    public CardShopViewCard(Card card, int userBalance, CardShopViewCardClickedListener onBuyClicked, CardShopViewCardClickedListener onSellClicked, boolean isForbidden) {
        super(5);
        this.card = card;
        this.isForbidden = isForbidden;
        super.setAlignment(Pos.CENTER_LEFT);
        super.getStyleClass().add("thin-rounded-card");
        super.setPadding(new Insets(3));
        initializeComponents(onBuyClicked, onSellClicked);
        setCardDetails();
        setUserBalance(userBalance);
        if (isForbidden)
            buyButton.setText("Forbidden");
    }

    private void initializeComponents(CardShopViewCardClickedListener onBuyClicked, CardShopViewCardClickedListener onSellClicked) {
        VBox cardInfoVBox = new VBox(5);
        HBox buyButtonHBox = new HBox(5);
        buyButton.setPadding(new Insets(3, 15, 3, 15));
        buyButton.setOnMouseExited(x -> SceneChanger.getScene().setCursor(Cursor.DEFAULT));
        buyButton.setOnMouseClicked(x -> onBuyClicked.clicked(this));
        sellButton.setPadding(new Insets(3, 15, 3, 15));
        sellButton.setOnMouseExited(x -> SceneChanger.getScene().setCursor(Cursor.DEFAULT));
        sellButton.setOnMouseClicked(x -> onBuyClicked.clicked(this));
        buyButtonHBox.setAlignment(Pos.CENTER);
        buyButtonHBox.getChildren().addAll(buyButton, sellButton);
        cardInfoVBox.getChildren().addAll(cardNameText, cardPriceText, cardDescriptionText, cardTypeText, buyButtonHBox);
        VBox imageAndStockBox = new VBox(playerStock, cardImageView);
        imageAndStockBox.setAlignment(Pos.CENTER);
        super.getChildren().addAll(imageAndStockBox, cardInfoVBox);
    }

    public void setUserBalance(int balance) {
        canAffordThisCard = balance >= card.getPrice() && !isForbidden;
        // Reset everything
        cardPriceText.setStyle("");
        buyButton.getStyleClass().clear();
        // Set things
        if (!canAffordThisCard) {
            cardPriceText.setFill(Color.RED);
            buyButton.getStyleClass().add("jfx-button-buy-not-ok");
            buyButton.setOnMouseEntered(x -> SceneChanger.getScene().setCursor(Assets.UNAVAILABLE_CURSOR));
        } else {
            buyButton.getStyleClass().add("jfx-button-buy-ok");
            buyButton.setOnMouseEntered(x -> SceneChanger.getScene().setCursor(Cursor.HAND));
        }
        sellButton.getStyleClass().add("jfx-button-buy-ok");
        sellButton.setOnMouseEntered(x -> SceneChanger.getScene().setCursor(Cursor.HAND));
    }

    private void setCardDetails() {
        cardNameText.setText("Name: " + card.getName());
        cardPriceText.setText("Price: " + card.getPrice());
        cardDescriptionText.setWrappingWidth(150);
        cardDescriptionText.setText("Description: " + card.getDescription());
        cardTypeText.setText("Type: " + card.getCardType().toString());
        cardImageView.setFitHeight(100);
        cardImageView.setFitWidth(70);
        cardImageView.setImage(Assets.getCardImage(card));
    }

    /**
     * Sets how many cards does market have
     *
     * @param i The number of cards which this user have
     */
    public void setCardStockOfMarket(int i) {
        if (i <= 0) {
            buyButton.getStyleClass().clear();
            buyButton.getStyleClass().add("jfx-button-buy-not-ok");
            buyButton.setOnMouseEntered(x -> SceneChanger.getScene().setCursor(Assets.UNAVAILABLE_CURSOR));
        }
        playerStock.setText("Market stock: " + i);
    }

    public String getBuyDialogMessage() {
        return "Are you sure you want to buy " + card.getName() + " card for " + card.getPrice() + " units?";
    }

    public String getSellDialogMessage() {
        return "Are you sure you want to sell " + card.getName() + " card for " + card.getPrice() + " units?";
    }

    public boolean canUserAffordCard() {
        return canAffordThisCard;
    }

    public Card getCard() {
        return card;
    }
}
