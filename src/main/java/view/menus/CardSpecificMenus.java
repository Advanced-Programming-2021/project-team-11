package view.menus;

import com.jfoenix.controls.JFXButton;
import controller.GameRoundController;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.PlayableCard;
import model.PlayerBoard;
import model.cards.MonsterCard;
import model.cards.RitualMonster;
import model.cards.monsters.BeastKingBarbaros;
import model.cards.spells.AdvancedRitualArt;
import model.cards.spells.EquipSpellCard;
import model.exceptions.*;
import view.components.AlertsUtil;
import view.components.Assets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class CardSpecificMenus {
    public static ArrayList<Integer> getCardsToTribute(ArrayList<PlayableCard> cards, int cardsNeeded) {
        return getCardsToTribute(cards.toArray(new PlayableCard[0]), cardsNeeded);
    }

    /**
     * Opens a window and shows some cards and asks the user to choose some of them to tribute them
     *
     * @param cards       The list of cards of user. This list can contain null values
     * @param cardsNeeded How many cards are needed to tribute
     * @return The array list of indexes of cards to tribute, or null if canceled
     */
    public static ArrayList<Integer> getCardsToTribute(PlayableCard[] cards, int cardsNeeded) {
        if (Arrays.stream(cards).filter(Objects::nonNull).count() < cardsNeeded)
            throw new BooAnException("cards.size() < cardsNeeded: " + Arrays.toString(cards) + " " + cardsNeeded);
        if (cardsNeeded == 0)
            return new ArrayList<>();
        // Create a new stage for this
        Stage stage = new Stage();
        stage.setResizable(false);
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(5));
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        borderPane.setTop(new Label("Select cards to tribute: (" + cardsNeeded + " cards needed)"));
        HBox cardBox = new HBox(5);
        TreeSet<Integer> toTributeCards = new TreeSet<>();
        for (int i = 0; i < cards.length; i++) {
            if (cards[i] == null)
                continue;
            final int index = i + 1;
            ImageView deathIcon = new ImageView(Assets.DEATH);
            deathIcon.setVisible(false);
            ImageView card = new ImageView(Assets.getCardImage(cards[i].getCard()));
            card.setFitHeight(100);
            card.setFitWidth(70);
            EventHandler<? super MouseEvent> clicked = x -> {
                if (toTributeCards.contains(index)) {
                    deathIcon.setVisible(false);
                    toTributeCards.remove(index);
                } else {
                    deathIcon.setVisible(true);
                    toTributeCards.add(index);
                }
            };
            card.setOnMouseClicked(clicked);
            deathIcon.setOnMouseClicked(clicked);
            cardBox.getChildren().add(new StackPane(card, deathIcon));
        }
        borderPane.setCenter(cardBox);
        HBox buttons = new HBox(5);
        buttons.setPadding(new Insets(5, 0, 0, 0));
        buttons.setAlignment(Pos.CENTER);
        JFXButton tributeButton = new JFXButton("Tribute");
        tributeButton.setOnMouseClicked(x -> {
            if (toTributeCards.size() != cardsNeeded)
                AlertsUtil.showError("Invalid amount of cards chosen!");
            else
                stage.close();
        });
        tributeButton.getStyleClass().add("jfx-button-blue");
        JFXButton cancelButton = new JFXButton("Cancel");
        cancelButton.setOnMouseClicked(x -> {
            toTributeCards.clear();
            stage.close();
        });
        cancelButton.getStyleClass().add("jfx-button-blue");
        buttons.getChildren().addAll(tributeButton, cancelButton);
        borderPane.setBottom(buttons);
        stage.showAndWait();
        if (toTributeCards.size() != cardsNeeded)
            return null;
        return new ArrayList<>(toTributeCards);
    }

    /**
     * Opens a window and shows the cards. Player must choose one of them
     *
     * @param title       Title of the dialog
     * @param cards       List of cards to choose from
     * @param chosenImage Image to show when a card is choosen
     * @return The index of card selected or -1 if canceled
     */
    public static int chooseCard(String title, ArrayList<PlayableCard> cards, Image chosenImage) {
        // Create a new stage for this
        Stage stage = new Stage();
        stage.setResizable(false);
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(5));
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        borderPane.setTop(new Label(title));
        HBox cardBox = new HBox(5);
        AtomicReference<Integer> result = new AtomicReference<>(-1);
        ArrayList<ImageView> chosenImages = new ArrayList<>(); // list of checkmarks to disable all of them
        for (int i = 0; i < cards.size(); i++) {
            final int index = i;
            ImageView checkMarkIcon = new ImageView(chosenImage);
            checkMarkIcon.setVisible(false);
            ImageView card = new ImageView(Assets.getCardImage(cards.get(i).getCard()));
            card.setFitHeight(100);
            card.setFitWidth(70);
            card.setOnMouseClicked(x -> {
                chosenImages.forEach(image -> image.setVisible(false));
                result.set(index);
                chosenImages.get(index).setVisible(true);
            });
            chosenImages.add(checkMarkIcon);
            cardBox.getChildren().add(new StackPane(card, checkMarkIcon));
        }
        borderPane.setCenter(cardBox);
        HBox buttons = new HBox(5);
        buttons.setPadding(new Insets(5, 0, 0, 0));
        buttons.setAlignment(Pos.CENTER);
        JFXButton chooseButton = new JFXButton("Choose");
        chooseButton.setOnMouseClicked(x -> stage.close());
        chooseButton.getStyleClass().add("jfx-button-blue");
        JFXButton cancelButton = new JFXButton("Cancel");
        cancelButton.setOnMouseClicked(x -> {
            result.set(-1);
            stage.close();
        });
        cancelButton.getStyleClass().add("jfx-button-blue");
        buttons.getChildren().addAll(chooseButton, cancelButton);
        borderPane.setBottom(buttons);
        stage.showAndWait();
        return result.get();
    }

    public static int chooseCard(String title, ArrayList<PlayableCard> cards) {
        return chooseCard(title, cards, Assets.CHECKMARK);
    }

    /**
     * Spawns {@link BeastKingBarbaros} based on number of tributes
     *
     * @param gameRoundController The game round
     */
    public static void summonBeastKingBarbaros(GameRoundController gameRoundController) {
        int tributes, monstersHave = gameRoundController.getPlayerBoard().countActiveMonsterCards();
        while (true) {
            try {
                int result = AlertsUtil.customButtonAlert("", "How many cards you want to tribute?", "0", "2", "3");
                if (result == -1)
                    return;
                if (result > 0)
                    result++;
                tributes = result;
                if (tributes > monstersHave)
                    throw new NotEnoughCardsToTributeException(null);
                break;
            } catch (NotEnoughCardsToTributeException ex) {
                AlertsUtil.showError(ex);
            }
        }
        Stage stage = new Stage();
        stage.showAndWait();
        ArrayList<Integer> cards = getCardsToTribute(gameRoundController.getPlayerBoard().getMonsterCards(), tributes);
        if (cards == null)
            return;
        if (tributes == 0)
            gameRoundController.returnSelectedCard().addAttackDelta(BeastKingBarbaros.getToReduceAttack());
        try {
            gameRoundController.summonCard(cards, true);
        } catch (NoMonsterOnTheseAddressesException | TrapCanBeActivatedException e) {
            AlertsUtil.showError(e);
            return;
        }
        if (tributes == 3)
            Arrays.stream(gameRoundController.getRivalBoard().getMonsterCards()).filter(Objects::nonNull).forEach(card -> gameRoundController.getRivalBoard().sendToGraveyard(card));
    }

    /**
     * Tries to ritual summon a monster card with {@link AdvancedRitualArt}
     *
     * @param board     The player's board which he wants to spawn a card
     * @param spellCard The {@link AdvancedRitualArt} card in user's hand
     */
    public static void handleRitualSpawn(PlayerBoard board, PlayableCard spellCard) {
        // Get the monsters which user can choose
        ArrayList<PlayableCard> list = board.getHand().stream()
                .filter(card -> card.getCard() instanceof RitualMonster).collect(Collectors.toCollection(ArrayList::new));
        int index;
        while (true) {
            System.out.print(MenuUtils.CHOOSE_CARD_BY_INDEX);
            index = chooseCard("Choose a monster to summon", list);
            if (index == -1)
                return;
            if (AdvancedRitualArt.getInstance().isConditionMade(board, null, list.get(index), 0))
                break;
            AlertsUtil.showError("You can't ritual summon this card!");
        }
        // Choose cards which it levels match
        ArrayList<Integer> indexesOfMonsters;
        while (true) {
            indexesOfMonsters = getCardsToTribute(board.getMonsterCardsList(), 1); // kinda tricky; We specify 1 to make sure that user at least chooses one card and we check the level later
            // Check level
            int sum = indexesOfMonsters.stream().mapToInt(i -> ((MonsterCard) board.getMonsterCardsList().get(i).getCard()).getLevel()).sum();
            if (((RitualMonster) list.get(index).getCard()).getLevel() == sum)
                break;
            else
                AlertsUtil.showError("The sum of levels of card which you have chosen is not " + ((RitualMonster) list.get(index).getCard()).getLevel());
        }
        // Tribute and summon
        board.sendToGraveyard(spellCard);
        ArrayList<PlayableCard> monstersToTribute = new ArrayList<>();
        indexesOfMonsters.forEach(i -> monstersToTribute.add(board.getMonsterCardsList().get(i)));
        monstersToTribute.forEach(board::sendToGraveyard);
        board.getHand().remove(list.get(index));
        board.addMonsterCard(list.get(index));
    }

    /**
     * Tries to spawn the {@link model.cards.monsters.TheTricky} card
     * To spawn it, we need to tribute a card from player hand
     *
     * @param playerBoard The player board
     * @param thisCard    This card which we want to spawn
     */
    public static void spawnTheTricky(PlayerBoard playerBoard, PlayableCard thisCard) {
        if (playerBoard.getHand().size() <= 1) { // 1 because we have the tricky in our hands
            AlertsUtil.showError("Your hand is empty!");
            return;
        }
        if (playerBoard.isMonsterZoneFull()) {
            AlertsUtil.showError(new MonsterCardZoneFullException()); // SAM KHALES
            return;
        }
        // Get the card
        int index;
        while (true) {
            index = chooseCard("Choose a card to throw it out", playerBoard.getHand(), Assets.DEATH);
            if (index == -1)
                return;
            if (playerBoard.getHand().get(index) == thisCard)
                AlertsUtil.showError("You played yourself!");
            else
                break;
        }
        // Remove the card
        playerBoard.getHand().remove(index);
        playerBoard.getHand().remove(thisCard);
        playerBoard.addMonsterCard(thisCard);
    }

    /**
     * Equips a monster with selected card
     *
     * @param roundController The round
     * @param thisCard        The equip card
     */
    public static void equip(GameRoundController roundController, PlayableCard thisCard) {
        ArrayList<PlayableCard> monsters = roundController.getPlayerBoard().getMonsterCardsList();
        int index = chooseCard("Choose a card to equip it:", monsters);
        if (index == -1)
            return;
        monsters.get(index).setEquippedCard((EquipSpellCard) thisCard.getCard());
    }
}
