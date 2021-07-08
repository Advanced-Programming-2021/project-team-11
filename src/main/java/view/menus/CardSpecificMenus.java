package view.menus;

import com.jfoenix.controls.JFXButton;
import controller.GameRoundController;
import controller.GameUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.PlayableCard;
import model.PlayerBoard;
import model.cards.*;
import model.cards.monsters.BeastKingBarbaros;
import model.cards.monsters.ManEaterBug;
import model.cards.spells.AdvancedRitualArt;
import model.cards.spells.EquipSpellCard;
import model.cards.traps.MindCrush;
import model.cards.traps.NegateAttack;
import model.enums.CardPlaceType;
import model.exceptions.*;
import view.components.AlertsUtil;
import view.components.Assets;

import java.util.*;
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
        stage.setTitle("Choose cards");
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(5));
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.setTitle("Tribute some cards");
        scene.getStylesheets().addAll("/style/global.css", "/style/tables.css");
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
    public static int chooseCard(String title, ArrayList<Card> cards, Image chosenImage) {
        // Create a new stage for this
        Stage stage = new Stage();
        stage.setResizable(false);
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(5));
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        scene.getStylesheets().addAll("/style/global.css", "/style/tables.css");
        stage.setTitle("Choose a card");
        borderPane.setTop(new Label(title));
        AnchorPane cardPane = new AnchorPane();
        AtomicReference<Integer> result = new AtomicReference<>(-1);
        ArrayList<ImageView> chosenImages = new ArrayList<>(); // list of checkmarks to disable all of them
        final int inEachRow = 4;
        for (int i = 0; i < cards.size(); i++) {
            final int index = i;
            ImageView checkMarkIcon = new ImageView(chosenImage);
            checkMarkIcon.setVisible(false);
            ImageView card = new ImageView(Assets.getCardImage(cards.get(i)));
            card.setFitHeight(100);
            card.setFitWidth(70);
            card.setOnMouseClicked(x -> {
                chosenImages.forEach(image -> image.setVisible(false));
                result.set(index);
                chosenImages.get(index).setVisible(true);
            });
            chosenImages.add(checkMarkIcon);
            StackPane stackPane = new StackPane(card, checkMarkIcon);
            stackPane.setLayoutX(85 * (i % inEachRow) + 10);
            int c = i / inEachRow;
            stackPane.setLayoutY(115 * c + 10);
            cardPane.getChildren().add(stackPane);
        }
        ScrollPane scrollPane = new ScrollPane(cardPane);
        scrollPane.setPrefViewportWidth(350);
        scrollPane.setMaxHeight(200);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        borderPane.setCenter(scrollPane);
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

    public static int chooseCard(String title, ArrayList<Card> cards) {
        return chooseCard(title, cards, Assets.CHECKMARK);
    }

    public static int choosePlayableCard(String title, ArrayList<PlayableCard> cards) {
        return choosePlayableCard(title, cards, Assets.CHECKMARK);
    }

    public static int choosePlayableCard(String title, ArrayList<PlayableCard> cards, Image chosenImage) {
        ArrayList<Card> fixedCards = cards.stream().map(PlayableCard::getCard).collect(Collectors.toCollection(ArrayList::new));
        return chooseCard(title, fixedCards, chosenImage);
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
            index = choosePlayableCard("Choose a monster to summon", list);
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
            int sum = indexesOfMonsters.stream().mapToInt(i -> ((MonsterCard) board.getMonsterCardsList().get(i - 1).getCard()).getLevel()).sum();
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
            index = choosePlayableCard("Choose a card to throw it out", playerBoard.getHand(), Assets.DEATH);
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
        int index = choosePlayableCard("Choose a card to equip it:", monsters);
        if (index == -1)
            return;
        monsters.get(index).setEquippedCard((EquipSpellCard) thisCard.getCard());
    }

    /**
     * Uses {@link model.cards.spells.MonsterReborn} to revive a monster from graveyard
     *
     * @param gameRoundController Game round
     * @param thisCard            The {@link model.cards.spells.MonsterReborn}
     */
    public static void handleMonsterReborn(GameRoundController gameRoundController, PlayableCard thisCard) {
        // Get the graveyards, mix them and then show them
        ArrayList<PlayableCard> cards = new ArrayList<>();
        cards.addAll(gameRoundController.getPlayerBoard().getGraveyard());
        cards.addAll(gameRoundController.getRivalBoard().getGraveyard());
        // Let the player choose a card
        int index = choosePlayableCard("Choose a card to \"revive\" it:", cards, Assets.REVIVE);
        if (index == -1)
            return;
        // Summon that card
        gameRoundController.specialSummon((MonsterCard) cards.get(index).getCard(), true);
        gameRoundController.getPlayerBoard().sendToGraveyard(thisCard);
    }

    /**
     * Adds a card from deck to hand
     *
     * @param board    Player's board
     * @param thisCard The {@link model.cards.spells.Terraforming} card
     */
    public static void handleTerraforming(PlayerBoard board, PlayableCard thisCard) {
        ArrayList<Card> list = board.getDeck().stream()
                .filter(card -> card instanceof SpellCard && ((SpellCard) card).getSpellCardType() == SpellCardType.FIELD)
                .collect(Collectors.toCollection(ArrayList::new));
        int index = chooseCard("Choose a card to add it to your hand.", list);
        if (index == -1)
            return;
        // Remove that card from hand
        board.getDeck().remove(list.get(index));
        board.getHand().add(new PlayableCard(list.get(index), CardPlaceType.HAND));
        board.shuffleDeck();
        board.sendToGraveyard(thisCard);
    }

    /**
     * Applies change of heart on a card
     *
     * @param roundController Game round
     * @param thisCard        The {@link model.cards.spells.ChangeOfHeart} card
     */
    public static void handleChangeOfHeart(GameRoundController roundController, PlayableCard thisCard) {
        ArrayList<PlayableCard> monsters = roundController.getRivalBoard().getMonsterCardsList();
        int index = choosePlayableCard("Choose a card", monsters, Assets.HEARTBLEED);
        if (index == -1)
            return;
        roundController.changeOfHeartSwapOwner(monsters.get(index));
        roundController.getPlayerBoard().sendToGraveyard(thisCard);
    }

    public static void handleTwinTwisters(GameRoundController round, PlayableCard thisCard) {
        ArrayList<PlayableCard> cards = round.getRivalBoard().getSpellCardsList();
        int index1 = choosePlayableCard("Choose your first card", cards, Assets.TORNADO);
        int index2 = choosePlayableCard("Choose your second card", cards, Assets.TORNADO);
        if (index1 != -1)
            round.getRivalBoard().sendToGraveyard(cards.get(index1));
        if (index1 != index2 && index2 != -1)
            round.getRivalBoard().sendToGraveyard(cards.get(index2));
        round.getPlayerBoard().getHand().remove(GameUtils.random.nextInt(round.getPlayerBoard().getHand().size()));
        round.getPlayerBoard().sendToGraveyard(thisCard);
    }

    public static void handleMysticalSpaceTyphoon(GameRoundController round, PlayableCard thisCard) {
        ArrayList<PlayableCard> cards = round.getRivalBoard().getSpellCardsList();
        int index = choosePlayableCard("Choose a card to destroy it", cards, Assets.TORNADO);
        if (index == -1)
            return;
        round.getRivalBoard().sendToGraveyard(cards.get(index));
        round.getPlayerBoard().sendToGraveyard(thisCard);
    }

    /**
     * Sets the scanner as a card
     *
     * @param rivalGraveyard Rival's graveyard cards
     * @param scannerCard    The scanner card which we want to mimic
     * @throws CantActivateSpellException If the graveyard does not contain any monster cards
     */
    public static void handleScannerCardEffect(ArrayList<PlayableCard> rivalGraveyard, PlayableCard scannerCard) throws CantActivateSpellException {
        ArrayList<PlayableCard> monsters = rivalGraveyard.stream().filter(card -> card.getCard().getCardType() == CardType.MONSTER)
                .collect(Collectors.toCollection(ArrayList::new));
        if (monsters.isEmpty())
            throw new CantActivateSpellException();
        // Show the graveyard and ask a user for a card
        int index = choosePlayableCard("Choose a card to mimic it!", monsters, Assets.RADAR);
        if (index == -1)
            return;
        scannerCard.activateEffect(null, null, monsters.get(index));
    }

    /**
     * Tries to move a card from graveyard to your hand with Herald Of Creation card
     *
     * @param playerBoard The board of player who has this card
     * @param thisCard    This card
     */
    public static void summonCardWithHeraldOfCreation(PlayerBoard playerBoard, PlayableCard thisCard) {
        // Throw a card out from hand
        int handIndex = choosePlayableCard("Choose a card from your hand to throw it out", playerBoard.getHand(), Assets.DEATH);
        if (handIndex == -1)
            return;
        // Now show the graveyard
        int graveyardIndex = choosePlayableCard("Choose a card from graveyard to revive it", playerBoard.getGraveyard(), Assets.REVIVE);
        if (graveyardIndex == -1)
            return;
        // Do the effect
        playerBoard.getHand().remove(handIndex);
        PlayableCard toMoveCard = playerBoard.getGraveyard().remove(graveyardIndex);
        toMoveCard.setCardPlace(CardPlaceType.HAND);
        playerBoard.getHand().add(toMoveCard);
        thisCard.activateEffect(null, null, null); // has no effect here
    }

    public static void callOfTheHunted(PlayerBoard board, PlayableCard thisCard) {
        ArrayList<PlayableCard> monsters = board.getGraveyard().stream()
                .filter(card -> card.getCard().getCardType() == CardType.MONSTER).collect(Collectors.toCollection(ArrayList::new));
        int index = choosePlayableCard("Choose a card to revive it", monsters, Assets.REVIVE);
        if (index == -1)
            return;
        // Activate the effect
        board.sendToGraveyard(thisCard);
        PlayableCard toSummon = new PlayableCard(monsters.get(index).getCard(), CardPlaceType.MONSTER);
        toSummon.makeVisible();
        toSummon.setAttacking();
        board.addMonsterCard(toSummon);
    }

    /**
     * Player should randomly choose a card
     * If the rival had it, it will remove all of that cards from rival's hand and deck
     * Otherwise we should randomly throw a card out of our hand
     *
     * @param gameRoundController Game round
     * @param thisCard            The mind crush card
     */
    public static void getMindCrushCard(GameRoundController gameRoundController, PlayableCard thisCard) {
        ArrayList<Card> cards = Card.getAllCards();
        int index = chooseCard("Choose a card (place your bet)", cards);
        if (index == -1)
            return;
        MindCrush.getInstance().activateEffect(gameRoundController.getPlayerBoard(), gameRoundController.getRivalBoard(), null, new PlayableCard(cards.get(index), CardPlaceType.HAND), 0);
        gameRoundController.getPlayerBoard().sendToGraveyard(thisCard);
    }

    public static void handleManEaterBugRemoval(PlayerBoard rivalBoard) {
        // At first check if the rival board is empty; Can't do anything if the board is empty!
        if (rivalBoard.isMonsterZoneEmpty())
            return;
        // Now ask the user to remove one card from rival
        int position = choosePlayableCard("Choose a card to eat it!", rivalBoard.getMonsterCardsList(), Assets.DEATH);
        if (position == -1)
            return;
        ManEaterBug.getInstance().activateEffect(null, rivalBoard, null, rivalBoard.getMonsterCardsList().get(position), 0);
    }

    public static boolean activateTrap(GameRoundController roundController, String[] cards, PlayableCard rivalCard) {
        if (!AlertsUtil.confirmAlert("Do you want to activate your trap or spell?"))
            return false;
        ArrayList<Card> cardsObject = Arrays.stream(cards).map(TrapCard::getTrapCardByName).collect(Collectors.toCollection(ArrayList::new));
        int index = chooseCard("Choose a trap card", cardsObject);
        if (index == -1)
            return false;
        TrapCard card = (TrapCard) cardsObject.get(index);
        card.activateEffect(roundController.getRivalBoard(), roundController.getPlayerBoard(), null, rivalCard, 0);
        Optional<PlayableCard> trap = roundController.getRivalBoard().getSpellCardsList().stream().filter(playerCard -> playerCard.getCard() == card).findFirst();
        trap.ifPresent(playableCard -> roundController.getRivalBoard().sendToGraveyard(playableCard));
        if (card instanceof NegateAttack) {
            try {
                roundController.advancePhase();
            } catch (PlayerTimeSealedException e) {
                throw new BooAnException(e.getMessage());
            }
        }
        return true;
    }
}
