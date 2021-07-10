package view.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.cards.Card;
import view.global.Assets;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CardInfo extends VBox implements Initializable {
    @FXML
    private ImageView cardImage;
    @FXML
    private Text cardDescription;

    public CardInfo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("card_info.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.setClassLoader(getClass().getClassLoader());
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showEmptyCard() {
        cardDescription.setText("");
        cardImage.setImage(Assets.UNKNOWN_CARD);
    }

    public void setCard(Card card) {
        if (card == null)
            showEmptyCard();
        else {
            cardDescription.setText(card.toString());
            cardImage.setImage(Assets.getCardImage(card));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showEmptyCard();
    }
}
