package view.components;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.cards.Card;
import view.menus.SceneChanger;

public class CardViewImportExport extends HBox {
    private final Card card;
    private final ImageView cardImageView = new ImageView();
    private final Text cardNameText = new Text(), cardTypeText = new Text(),
            cardDescriptionText = new Text();
    private final JFXButton exportButton = new JFXButton("Export");

    public CardViewImportExport(Card card, CardViewImportExportClickedListener onClicked) {
        super(5);
        this.card = card;
        super.setAlignment(Pos.CENTER_LEFT);
        super.getStyleClass().add("thin-rounded-card");
        super.setPadding(new Insets(3));
        initializeComponents(onClicked);
        setCardDetails();
    }

    private void initializeComponents(CardViewImportExportClickedListener onClicked) {
        VBox cardInfoVBox = new VBox(5);
        HBox exportButtonHBox = new HBox();
        exportButton.setPadding(new Insets(3, 15, 3, 15));
        exportButton.getStyleClass().add("jfx-button-buy-ok");
        exportButton.setOnMouseExited(x -> SceneChanger.getScene().setCursor(Cursor.DEFAULT));
        exportButton.setOnMouseEntered(x -> SceneChanger.getScene().setCursor(Cursor.HAND));
        exportButton.setOnMouseClicked(x -> onClicked.clicked(this));
        exportButtonHBox.setAlignment(Pos.CENTER);
        exportButtonHBox.getChildren().add(exportButton);
        cardInfoVBox.getChildren().addAll(cardNameText, cardDescriptionText, cardTypeText, exportButtonHBox);
        super.getChildren().addAll(cardImageView, cardInfoVBox);
    }

    private void setCardDetails() {
        cardNameText.setText("Name: " + card.getName());
        cardDescriptionText.setWrappingWidth(150);
        cardDescriptionText.setText("Description: " + card.getDescription());
        cardTypeText.setText("Type: " + card.getCardType().toString());
        cardImageView.setFitHeight(100);
        cardImageView.setFitWidth(70);
        cardImageView.setImage(Assets.getCardImage(card));
    }

    public Card getCard() {
        return card;
    }
}
