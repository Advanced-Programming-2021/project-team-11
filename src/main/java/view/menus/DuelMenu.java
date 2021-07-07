package view.menus;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import controller.GameController;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.PlayableCard;
import model.PlayerBoard;
import model.User;
import model.cards.Card;
import model.cards.MonsterCard;
import model.cards.monsters.BeastKingBarbaros;
import model.cards.monsters.HeraldOfCreation;
import model.cards.monsters.ScannerCard;
import model.cards.monsters.TheTricky;
import model.cards.spells.*;
import model.cards.traps.CallOfTheHaunted;
import model.cards.traps.MindCrush;
import model.enums.*;
import model.exceptions.*;
import model.game.GameEndResults;
import model.results.MonsterAttackResult;
import view.animation.CardDestroyedTransition;
import view.animation.CardFlipTransition;
import view.components.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

// TODO: traps!
// TODO: flip summon

public class DuelMenu implements Initializable {
    private static final double CARD_PLACES_WIDTH = 78, CARD_PLACES_FIRST_X = 800 + 107.5 - 570, PLAYER_DOWN_OFFSET = 60,
            CARD_PLACES_SPELL_RIVAL_Y = 106, CARD_PLACES_SPELL_PLAYER_Y = 600 - CARD_PLACES_SPELL_RIVAL_Y - PLAYER_DOWN_OFFSET,
            CARD_PLACES_MONSTER_RIVAL_Y = CARD_PLACES_SPELL_RIVAL_Y + 104, CARD_PLACES_MONSTER_PLAYER_Y = 600 - CARD_PLACES_MONSTER_RIVAL_Y - PLAYER_DOWN_OFFSET,
            CARD_PLACES_SWORD_OFFSET = 25;
    private static final double[] CARD_X_OFFSETS = {CARD_PLACES_FIRST_X + CARD_PLACES_WIDTH * 2, CARD_PLACES_FIRST_X + CARD_PLACES_WIDTH, CARD_PLACES_FIRST_X + CARD_PLACES_WIDTH * 3, CARD_PLACES_FIRST_X, CARD_PLACES_FIRST_X + CARD_PLACES_WIDTH * 4};
    public static GameController gameController;
    public static User player1, player2;
    private final ArrayList<CardView> cardsOnGround = new ArrayList<>();
    private final MediaPlayer musicPlayer = new MediaPlayer(Assets.MUSIC);
    private CardView attackingCard = null;
    public ImageView fieldImageView;
    public DuelistInfo rivalInfo;
    public DuelistInfo playerInfo;
    public CardInfo cardInfo;
    public AnchorPane rootView;
    public JFXDialog dialog;
    public Label dialogHeader;
    public JFXDialogLayout dialogContainerLayout;
    public StackPane stackPane;
    public Text phaseText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        musicPlayer.setOnEndOfMedia(() -> {
            musicPlayer.seek(Duration.ZERO);
            musicPlayer.play();
        });
        musicPlayer.play();
        SceneChanger.getScene().setOnKeyPressed(x -> {
            if (x.getCode() == KeyCode.ESCAPE)
                pause();
            if (x.isControlDown() && x.isShiftDown() && x.getCode() == KeyCode.C)
                cheat();
        });
        dialog.setDialogContainer(stackPane);
        drawScene();
    }

    /**
     * Updates badges of the players
     */
    private void updatePlayersLp() {
        playerInfo.setUser(gameController.getRound().getPlayerBoard().getPlayer());
        rivalInfo.setUser(gameController.getRound().getRivalBoard().getPlayer());
    }

    private void drawScene() {
        updatePlayersLp();
        cardsOnGround.forEach(card -> rootView.getChildren().remove(card));
        cardsOnGround.clear();
        fieldImageView.setImage(Assets.getFieldImage(gameController.getRound().getField()));
        addHands();
        addSpells();
        addMonsters();
    }

    /**
     * Renders all cards in both player's hands
     */
    private void addHands() {
        ArrayList<PlayableCard> cards = gameController.getRound().getPlayerBoard().getHand();
        for (int i = 0; i < cards.size(); i++) {
            CardView view = new CardView(cards.get(i), false, this::cardHovered, i);
            view.setLayoutY(545);
            view.setLayoutX(260 + i * 70);
            final int finalI = i + 1;
            if (gameController.getRound().getPhase() == GamePhase.MAIN1 || gameController.getRound().getPhase() == GamePhase.MAIN2 || gameController.getRound().getPhase() == GamePhase.BATTLE_PHASE)
                view.setOnMouseClicked(x -> handCardSelected(finalI));
            rootView.getChildren().add(view);
            cardsOnGround.add(view);
        }
        cards = gameController.getRound().getRivalBoard().getHand();
        for (int i = 0; i < cards.size(); i++) {
            CardView view = new CardView(cards.get(i), true, this::cardHovered, i);
            view.setLayoutY(-40);
            view.setLayoutX(260 + i * 70);
            rootView.getChildren().add(view);
            cardsOnGround.add(view);
        }
    }

    /**
     * Renders all spells in the game board
     */
    private void addSpells() {
        addMainCards(gameController.getRound().getRivalBoard().getSpellCards(), true, CARD_PLACES_SPELL_RIVAL_Y, this::rivalSpellSelected);
        addMainCards(gameController.getRound().getPlayerBoard().getSpellCards(), false, CARD_PLACES_SPELL_PLAYER_Y, this::playerSpellSelected);
    }

    /**
     * Renders all monster cards in the game board
     */
    private void addMonsters() {
        addMainCards(gameController.getRound().getRivalBoard().getMonsterCards(), true, CARD_PLACES_MONSTER_RIVAL_Y, this::rivalMonsterSelected);
        addMainCards(gameController.getRound().getPlayerBoard().getMonsterCards(), false, CARD_PLACES_MONSTER_PLAYER_Y, this::playerMonsterSelected);
    }

    private void addMainCards(PlayableCard[] cards, boolean forRival, double y, PlayableCardSelectedCallback callback) {
        for (int i = 0; i < cards.length; i++) {
            PlayableCard card = cards[i];
            if (card != null) {
                CardView view = new CardView(card, forRival, this::cardHovered, i + 1);
                view.setLayoutY(y);
                view.setLayoutX(CARD_X_OFFSETS[i]);
                if (callback != null)
                    view.setOnMouseClicked(x -> callback.selected(view));
                rootView.getChildren().add(view);
                cardsOnGround.add(view);
            }
        }
    }

    /**
     * Clears all components in the alert dialog
     */
    private void refreshDialog() {
        dialogContainerLayout.getBody().clear();
        dialogContainerLayout.getActions().clear();
    }

    /**
     * Shows the pause menu
     */
    private void pause() {
        disableAttackMode();
        setupDialog("Pause", new JfxCursorButton("(Un)Pause music", x -> {
                    musicPlayer.setMute(!musicPlayer.isMute());
                    dialog.close();
                }), new JfxCursorButton("Resume", x -> dialog.close()),
                new JfxCursorButton("Surrender", x -> {
                    dialog.close();
                    surrender();
                }));
        dialog.show();
    }

    private void surrender() {
        gameController.getRound().surrender();
        checkRoundEnd();
    }

    private void checkRoundEnd() {
        GameStatus roundStatus = gameController.isRoundEnded();
        if (roundStatus != GameStatus.ONGOING) {
            GameEndResults results = gameController.isGameEnded();
            if (results != null) {
                // Apply the results
                AlertsUtil.showSuccess(String.format("%s won the whole match with score: %d-%d", results.didPlayer1Won() ? player1.getNickname() : player2.getNickname(),
                        results.getPlayer1Score(), results.getPlayer2Score()), () -> SceneChanger.changeScene(MenuNames.MAIN));
                player1.increaseScore(results.getPlayer1Score());
                player1.increaseMoney(results.getPlayer1Money());
                player2.increaseScore(results.getPlayer2Score());
                player2.increaseMoney(results.getPlayer2Money());
                SceneChanger.getScene().setOnKeyPressed(null);
                musicPlayer.stop();
            }
            // TODO:
            // System.out.printf("%s won the game\n", roundStatus == GameStatus.PLAYER1_WON ? player1.getNickname() : player2.getNickname());
        }
    }

    private void cheat() {
        String cheatCode = AlertsUtil.getTextAlert("Enter cheat code:");
        switch (cheatCode) {
            case "PAINKILLER":
                gameController.getRound().painkiller();
                updatePlayersLp();
                break;
            case "NUKE":
                gameController.getRound().nuke();
                checkRoundEnd();
                break;
            default:
                AlertsUtil.showError("Invalid cheat code");
        }
    }

    private void cardHovered(Card card, boolean shouldShow) {
        cardInfo.setCard(shouldShow ? card : null);
    }

    private void setupDialog(String title, JfxCursorButton... buttons) {
        refreshDialog();
        dialogHeader.setText(title);
        dialogContainerLayout.getActions().addAll(buttons);
    }

    private void handCardSelected(int index) {
        try {
            gameController.getRound().selectCard(index, false, CardPlaceType.HAND);
            String title = gameController.getRound().returnSelectedCard().getCard() instanceof MonsterCard ?
                    "Summon or set the card?" : "Activate or set this card?";
            JfxCursorButton firstButton = gameController.getRound().returnSelectedCard().getCard() instanceof MonsterCard ?
                    new JfxCursorButton("Summon", x -> summon()) : new JfxCursorButton("Activate Spell", x -> activateSpell());
            setupDialog(title, new JfxCursorButton("Cancel", x -> dialog.close()),
                    new JfxCursorButton("Set", x -> set()), firstButton);
            dialog.show();
        } catch (NoCardFoundInPositionException ex) {
            throw new BooAnException(ex);
        }
    }

    private void summon() {
        try {
            gameController.getRound().summonCard();
        } catch (AlreadySummonedException | NoCardSelectedYetException | CantSummonCardException
                | InvalidPhaseActionException | MonsterCardZoneFullException e) {
            AlertsUtil.showError(e);
        } catch (TributeNeededException e) {
            if (e.getCard() instanceof BeastKingBarbaros)
                CardSpecificMenus.summonBeastKingBarbaros(gameController.getRound());
            else
                summonWithTribute(e.getNeededTributes());
        } catch (NotEnoughCardsToTributeException e) {
            if (e.getCard() instanceof BeastKingBarbaros)
                CardSpecificMenus.summonBeastKingBarbaros(gameController.getRound());
            else
                AlertsUtil.showError(e);
        } catch (SpecialSummonNeededException e) {
            if (e.getToSummonCard().getCard() instanceof TheTricky)
                CardSpecificMenus.spawnTheTricky(gameController.getRound().getPlayerBoard(), e.getToSummonCard());
        } catch (TrapCanBeActivatedException ex) {
            /*success = !prepareTrap(ex.getAllowedCards());
            if (success)
                gameController.getRound().forceSummonCard();*/
        }
        drawScene();
        dialog.close();
    }

    private void summonWithTribute(int neededTributes) {
        ArrayList<Integer> cards = CardSpecificMenus.getCardsToTribute(gameController.getRound().getPlayerBoard().getMonsterCards(), neededTributes);
        if (cards == null)
            return;
        // Try to tribute
        try {
            gameController.getRound().summonCard(cards, false);
        } catch (NoMonsterOnTheseAddressesException e) {
            System.out.println(e.getMessage());
        } catch (TrapCanBeActivatedException ex) {
            /*if (!prepareTrap(ex.getAllowedCards()))
                gameController.getRound().forceSummonCard();*/
        }
    }

    private void set() {
        try {
            gameController.getRound().setCard();
            drawScene();
        } catch (Exception e) {
            AlertsUtil.showError(e);
        }
        dialog.close();
    }

    private void activateSpell() {
        try {
            PlayableCard selectedCard = gameController.getRound().returnSelectedCard();
            handleActivateSpellCallBack(gameController.getRound().activeSpell(), selectedCard);
        } catch (OnlySpellCardsAllowedException | NoCardSelectedException | CardAlreadyAttackedException |
                InvalidPhaseActionException | RitualSummonNotPossibleException | CantSpecialSummonException |
                CantUseSpellException | SpellAlreadyActivatedException | SpellCardZoneFullException e) {
            AlertsUtil.showError(e);
        } catch (MonsterEffectMustBeHandledException e) {
            handleMonsterWithEffectCard(e.getCard());
        }
        dialog.close();
    }

    private void handleMonsterWithEffectCard(PlayableCard card) {
        if (card.getCard() instanceof ScannerCard)
            try {
                CardSpecificMenus.handleScannerCardEffect(gameController.getRound().getRivalBoard().getGraveyard(), card);
            } catch (CantActivateSpellException e) {
                AlertsUtil.showError(e);
            }
        if (card.getCard() instanceof HeraldOfCreation)
            CardSpecificMenus.summonCardWithHeraldOfCreation(gameController.getRound().getPlayerBoard(), card);
    }

    private void handleActivateSpellCallBack(ActivateSpellCallback result, PlayableCard selectedCard) {
        switch (result) {
            case RITUAL:
                CardSpecificMenus.handleRitualSpawn(gameController.getRound().getPlayerBoard(), selectedCard);
                break;
            case NORMAL:
                if (selectedCard.getCard() instanceof MonsterReborn)
                    CardSpecificMenus.handleMonsterReborn(gameController.getRound(), selectedCard);
                if (selectedCard.getCard() instanceof Terraforming)
                    CardSpecificMenus.handleTerraforming(gameController.getRound().getPlayerBoard(), selectedCard);
                if (selectedCard.getCard() instanceof ChangeOfHeart)
                    CardSpecificMenus.handleChangeOfHeart(gameController.getRound(), selectedCard);
                if (selectedCard.getCard() instanceof TwinTwisters)
                    CardSpecificMenus.handleTwinTwisters(gameController.getRound(), selectedCard);
                if (selectedCard.getCard() instanceof MysticalSpaceTyphoon)
                    CardSpecificMenus.handleMysticalSpaceTyphoon(gameController.getRound(), selectedCard);
                break;
            case EQUIP:
                CardSpecificMenus.equip(gameController.getRound(), selectedCard);
                break;
            case TRAP:
                if (selectedCard.getCard() instanceof CallOfTheHaunted)
                    CardSpecificMenus.callOfTheHunted(gameController.getRound().getPlayerBoard(), selectedCard);
                if (selectedCard.getCard() instanceof MindCrush)
                    CardSpecificMenus.getMindCrushCard(gameController.getRound(), selectedCard);
        }
        drawScene();
    }

    public void nextPhase(MouseEvent mouseEvent) {
        try {
            gameController.getRound().advancePhase();
        } catch (PlayerTimeSealedException ignored) {
        }
        GamePhase nowPhase = gameController.getRound().getPhase();
        checkRoundEnd();
        // Otherwise process the data
        phaseText.setText("Phase: " + nowPhase.toString());
        if (nowPhase == GamePhase.END_PHASE) {
            cardInfo.setCard(null);
            blur();
            updatePlayersLp();
            AlertsUtil.showSuccess("Pass the computer to " + gameController.getRound().getPlayerBoard().getPlayer().getUser().getNickname() + "!",
                    () -> stackPane.setEffect(null));
        }
        drawScene();
    }

    /**
     * Blurs the playground
     */
    private void blur() {
        ColorAdjust adj = new ColorAdjust(0, -0.9, -0.5, 0);
        GaussianBlur blur = new GaussianBlur(55);
        adj.setInput(blur);
        stackPane.setEffect(adj);
    }

    private void rivalMonsterSelected(CardView card) {
        if (attackingCard != null)
            attackToCard(card);
    }

    private void playerMonsterSelected(CardView card) {
        try {
            gameController.getRound().selectCard(card.getIndex(), false, CardPlaceType.MONSTER);
        } catch (NoCardFoundInPositionException e) {
            throw new BooAnException(e);
        }
        if (gameController.getRound().getPhase() == GamePhase.BATTLE_PHASE)
            attack(card);
        if (gameController.getRound().getPhase() == GamePhase.MAIN1 || gameController.getRound().getPhase() == GamePhase.MAIN2)
            swapCardPosition(card);
    }

    private void playerSpellSelected(CardView card) {
        try {
            gameController.getRound().selectCard(card.getIndex(), false, CardPlaceType.SPELL);
        } catch (NoCardFoundInPositionException e) {
            throw new BooAnException(e);
        }
        activateSpell();
    }

    private void rivalSpellSelected(CardView card) {

    }

    private void attack(CardView card) {
        if (gameController.getRound().getRivalBoard().isMonsterZoneEmpty()) {
            directAttack(card.getIndex());
            return;
        }
        AlertsUtil.showHelp("Select a card to attack to it!", null);
        attackingCard = card;
    }

    /**
     * Attacks to rival with selected card
     */
    private void directAttack(int index) {
        index--;
        try {
            gameController.getRound().attackToPlayer();
            ImageView swordImage = new ImageView(Assets.SWORD);
            rootView.getChildren().add(swordImage);
            TranslateTransition sword = new TranslateTransition(Duration.millis(800), swordImage);
            sword.setFromX(CARD_X_OFFSETS[index] + CARD_PLACES_SWORD_OFFSET);
            sword.setFromY(CARD_PLACES_MONSTER_PLAYER_Y);
            sword.setToY(0);
            sword.setToX(CARD_X_OFFSETS[index] + CARD_PLACES_SWORD_OFFSET);
            sword.setOnFinished(x -> {
                updatePlayersLp();
                rootView.getChildren().remove(swordImage);
            });
            sword.play();
        } catch (Exception ex) {
            AlertsUtil.showError(ex);
        }
    }

    private void swapCardPosition(CardView card) {
        double start = gameController.getRound().returnSelectedCard().isAttacking() ? 0 : 90;
        double end = Math.abs(90 - start);
        try {
            gameController.getRound().swapCardPosition();
            RotateTransition rotateTransition = new RotateTransition(Duration.millis(500), card);
            rotateTransition.setFromAngle(start);
            rotateTransition.setToAngle(end);
            rotateTransition.setOnFinished(x -> drawScene());
            rotateTransition.play();
        } catch (Exception ex) {
            AlertsUtil.showError(ex);
        }
    }

    private void attackToCard(CardView toAttackCard) {
        try {
            MonsterAttackResult attackResult = gameController.getRound().attackToMonster(toAttackCard.getIndex());
            playAttackAnimations(attackingCard, toAttackCard, attackResult);
            attackingCard = null;
        } catch (TrapCanBeActivatedException ex) {
            // TODO
           /* if (prepareTrap(ex.getAllowedCards())) {
                MonsterAttackResult result = gameController.getRound().attackToMonsterForced(positionToAttack);
                System.out.println(result.toString());
                printBoard();
            }*/
        } catch (Exception e) {
            AlertsUtil.showError(e);
        }
    }

    private void playAttackAnimations(CardView playerCard, CardView rivalCard, MonsterAttackResult attackResult) {
        boolean boardDrawDone = false;
        boolean rivalCardDestroyed = false;
        if (attackResult.getBattleResult() == AttackResult.RIVAL_DESTROYED || (attackResult.isAttackedCardInAttackMode() && attackResult.getBattleResult() == AttackResult.DRAW)) {
            CardDestroyedTransition transition = new CardDestroyedTransition(rivalCard);
            transition.setOnFinished(x -> drawScene());
            transition.play();
            boardDrawDone = true;
            rivalCardDestroyed = true;
        }
        if (attackResult.getBattleResult() == AttackResult.ME_DESTROYED || (attackResult.isAttackedCardInAttackMode() && attackResult.getBattleResult() == AttackResult.DRAW)) {
            CardDestroyedTransition transition = new CardDestroyedTransition(playerCard);
            if (!boardDrawDone)
                transition.setOnFinished(x -> drawScene());
            transition.play();
            boardDrawDone = true;
        }
        if (attackResult.wasHidden() && !rivalCardDestroyed) {
            CardFlipTransition transition = new CardFlipTransition(rivalCard);
            if (!boardDrawDone)
                transition.setOnFinished(x -> drawScene());
            transition.play();
        }
    }

    private void disableAttackMode() {
        this.attackingCard = null;
    }

    /**
     * Shows the graveyard of a player in a dialog
     *
     * @param board Player's board
     */
    private void showGraveyard(PlayerBoard board) {
        setupDialog(board.getPlayer().getUser().getNickname() + "'s graveyard", new JfxCursorButton("Close", x -> dialog.close()));
        ArrayList<PlayableCard> cards = board.getGraveyard();
        if (cards.isEmpty()) {
            dialogContainerLayout.getBody().add(new Label("Empty graveyard!"));
        } else {
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setMaxHeight(200);
            dialogContainerLayout.getBody().add(scrollPane);
            AnchorPane anchorPane = new AnchorPane();
            scrollPane.setContent(anchorPane);
            final int inEachRow = 4;
            for (int i = 0; i < cards.size(); i++) {
                ImageView cardImageView = new ImageView(Assets.getCardImage(cards.get(i).getCard()));
                cardImageView.setFitWidth(70);
                cardImageView.setFitHeight(100);
                cardImageView.setX(85 * (i % inEachRow) + 10);
                int c = i / inEachRow;
                cardImageView.setY(115 * c + 10);
                int finalI = i;
                cardImageView.setOnMouseEntered(x -> cardInfo.setCard(cards.get(finalI).getCard()));
                anchorPane.getChildren().add(cardImageView);
            }
        }
        dialog.show();
    }

    public void clickedRivalGraveyard(MouseEvent mouseEvent) {
        showGraveyard(gameController.getRound().getRivalBoard());
    }

    public void clickedPlayerGraveyard(MouseEvent mouseEvent) {
        showGraveyard(gameController.getRound().getPlayerBoard());
    }
}
