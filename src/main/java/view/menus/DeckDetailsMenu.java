package view.menus;

import com.jfoenix.controls.JFXScrollPane;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.Deck;
import model.cards.Card;
import view.components.Assets;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DeckDetailsMenu implements Initializable {
    public static Deck toEditDeck = null;
    public static String toEditDeckName;
    public AnchorPane mainDeckAnchorPane;
    public AnchorPane inventoryAnchorPane;
    public AnchorPane sideDeckAnchorPane;
    public ScrollPane mainDeckScrollPane;
    public ScrollPane inventoryScrollPane;
    public ScrollPane sideDeckScrollPane;
    public Text deckName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addCards(toEditDeck.getMainDeck(), mainDeckAnchorPane, 5);
        addCards(toEditDeck.getSideDeck(), sideDeckAnchorPane, 5);
        addCards(MainMenu.loggedInUser.getAvailableCards(), inventoryAnchorPane, 3);
        deckName.setText(toEditDeckName);
        JFXScrollPane.smoothScrolling(mainDeckScrollPane);
        JFXScrollPane.smoothScrolling(inventoryScrollPane);
        JFXScrollPane.smoothScrolling(sideDeckScrollPane);
    }

    private void addCards(ArrayList<Card> cards, AnchorPane anchorPane, int inEachRow) {
        ArrayList<ImageView> cardsImages = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            ImageView cardImageView = new ImageView();
            cardImageView.setFitHeight(100);
            cardImageView.setFitWidth(70);
            cardImageView.setImage(Assets.getCardImage(cards.get(i)));
            cardImageView.setX(85 * (i % inEachRow) + 10);
            int c = i / inEachRow;
            cardImageView.setY(115 * c + 10);
            cardsImages.add(cardImageView);
        }
        anchorPane.getChildren().addAll(cardsImages);
    }

    public void clickedBackButton(MouseEvent mouseEvent) {
        SceneChanger.changeScene(MenuNames.DECK);
    }
}
