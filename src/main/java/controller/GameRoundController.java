package controller;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.CardType;
import model.cards.MonsterCard;
import model.cards.MonsterCardType;
import model.cards.SpellCard;
import model.enums.CardPlaceType;
import model.enums.GamePhase;
import model.enums.GameStatus;
import model.exceptions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class GameRoundController {
    private final PlayerBoard player1Board;
    private final PlayerBoard player2Board;
    private final ArrayList<PlayableCard> chainLink;
    private PlayableCard selectedCard;
    private boolean player1Turn, playerAlreadySummoned = false;
    private SpellCard field;
    private GameStatus gameStatus;
    private GamePhase phase;

    GameRoundController(PlayerBoard player1Board, PlayerBoard player2Board, boolean player1Starting) {
        this.player1Board = player1Board;
        this.player2Board = player2Board;
        player1Turn = player1Starting;
        chainLink = new ArrayList<>();
        gameStatus = GameStatus.ONGOING;
        phase = GamePhase.DRAW;
    }

    /**
     * Moves one phase forward and does anything if needed
     *
     * @return The current phase after advancing
     */
    public GamePhase advancePhase() {
        switch (phase) {
            case DRAW:
                phase = GamePhase.STANDBY;
                // TODO: Do something?
                break;
            case STANDBY:
                phase = GamePhase.MAIN1;
                break;
            case MAIN1:
                phase = GamePhase.BATTLE_PHASE;
                break;
            case BATTLE_PHASE:
                phase = GamePhase.MAIN2;
                break;
            case MAIN2:
                phase = GamePhase.END_PHASE;
                endTurn(); // change the turn
                break;
            case END_PHASE:
                phase = GamePhase.DRAW;
                if (!getPlayerBoard().drawCard()) // player lost the round!
                    gameStatus = isPlayer1Turn() ? GameStatus.PLAYER2_WON : GameStatus.PLAYER1_WON;
                break;
        }
        return phase;
    }

    public void selectCard(int index, boolean fromOpponent, CardPlaceType cardPlace) throws NoCardFoundInPositionException {
        switch (cardPlace) {
            case SPELL:
                selectedCard = (fromOpponent ? getRivalBoard() : getPlayerBoard()).getSpellCards()[index - 1];
                break;
            case MONSTER:
                selectedCard = (fromOpponent ? getRivalBoard() : getPlayerBoard()).getMonsterCards()[index - 1];
                break;
            case GRAVEYARD:
                selectedCard = (fromOpponent ? getRivalBoard() : getPlayerBoard()).getGraveyard().get(index - 1);
                break;
            case HAND:
                selectedCard = (fromOpponent ? getRivalBoard() : getPlayerBoard()).getHand().get(index - 1);
                break;
            case FIELD:
                selectedCard = (fromOpponent ? getRivalBoard() : getPlayerBoard()).getField();
                break;
            default:
                throw new BooAnException("Card place not found!");
        }
        if (selectedCard == null)
            throw new NoCardFoundInPositionException();
    }

    public void deselectCard() throws NoCardSelectedYetException {
        if (selectedCard == null)
            throw new NoCardSelectedYetException();
        selectedCard = null;
    }

//    public String attackToMonster(int index) {
//        PlayerBoard rivalBoard = player1Turn? player2Board: player1Board;
//        PlayableCard monster = rivalBoard.getMonsterCards()[index - 1];
//    }

    public int attackToPlayer() throws Exception {
        if (selectedCard == null)
            throw new NoCardSelectedException();
        if (selectedCard.getCardPlace() != CardPlaceType.MONSTER)
            throw new CantAttackCardException();
        if (phase != GamePhase.BATTLE_PHASE)
            throw new InvalidPhaseActionException();
        if (selectedCard.hasAttacked())
            throw new CardAlreadyAttackedException();
        if (Arrays.stream(getRivalBoard().getMonsterCards()).anyMatch(Objects::nonNull))
            throw new CantAttackToPlayerException();

        int attacked = selectedCard.getAttack();
        getRivalBoard().getPlayer().decreaseHealth(attacked);
        selectedCard.setHasAttacked(true);
        return attacked;
    }

    public void setCard() throws Exception {
        if (selectedCard == null)
            throw new NoCardSelectedException();
        if (selectedCard.getCardPlace() != CardPlaceType.HAND)
            throw new CantSummonCardException();

        if (selectedCard.getCard().getCardType() == CardType.MONSTER)
            getPlayerBoard().addMonsterCard(selectedCard);
        else
            getPlayerBoard().addSpellCard(selectedCard);
        getPlayerBoard().removeHandCard(selectedCard);
    }

//    public void flipSummon() {
//    }
//

    public void summonCard() throws NoCardSelectedYetException, CantSummonCardException, InvalidPhaseActionException, MonsterCardZoneFullException, AlreadySummonedException, NotEnoughCardsToTributeException, TributeNeededException {
        if (selectedCard == null)
            throw new NoCardSelectedYetException();
        if (selectedCard.getCardPlace() != CardPlaceType.HAND || selectedCard.getCard().getCardType() != CardType.MONSTER
                || isPlayableCardInRivalHand(selectedCard) || ((MonsterCard) selectedCard.getCard()).getMonsterCardType() == MonsterCardType.RITUAL)
            throw new CantSummonCardException();
        if (phase != GamePhase.MAIN2 && phase != GamePhase.MAIN1)
            throw new InvalidPhaseActionException();
        if (getPlayerBoard().isMonsterZoneFull())
            throw new MonsterCardZoneFullException();
        if (playerAlreadySummoned)
            throw new AlreadySummonedException();
        int level = ((MonsterCard) selectedCard.getCard()).getLevel();
        if (level <= 4) { // GG! summon the card
            summonSelectedCard();
            return;
        }
        int cardsToTribute, activeMonstersCount = getPlayerBoard().countActiveMonsterCards();
        if (level <= 6)
            cardsToTribute = 1;
        else if (level <= 8)
            cardsToTribute = 2;
        else
            cardsToTribute = 3;
        if (cardsToTribute > activeMonstersCount)
            throw new NotEnoughCardsToTributeException();
        throw new TributeNeededException(cardsToTribute);
    }

    /**
     * Summons card with tributes. Please note that you must not immodestly call this function
     * At first call {@link #summonCard()} then call this function
     *
     * @param tributes The positions of cards to tribute
     */
    public void summonCard(ArrayList<Integer> tributes) throws NoMonsterOnTheseAddressesException {
        // Check these places
        if (tributes.stream().anyMatch(x -> getPlayerBoard().getMonsterCards()[x] == null))
            throw new NoMonsterOnTheseAddressesException();
        // Remove them and add the card!
        tributes.forEach(x -> getPlayerBoard().removeMonsterCard(x));
        summonSelectedCard();
    }

    private void summonSelectedCard() {
        selectedCard.makeVisible();
        selectedCard.setAttacking();
        getPlayerBoard().addMonsterCard(selectedCard);
        selectedCard = null;
        playerAlreadySummoned = true;
    }

    //    public void setCardPosition(boolean attacking) {
//    }
//
//    public void activeSpell() {
//    }
//
//    public String getCard() {
//    }
//
//    public int checkWinner() {
//    }
//
    public void surrender() {
        gameStatus = isPlayer1Turn() ? GameStatus.PLAYER1_SURRENDER : GameStatus.PLAYER2_SURRENDER;
    }

//    public String boardForPlayer() {
//    }

    public PlayerBoard getPlayer1Board() {
        return player1Board;
    }

    public PlayerBoard getPlayer2Board() {
        return player2Board;
    }

    public PlayerBoard getPlayerBoard() {
        return player1Turn ? player1Board : player2Board;
    }

    public PlayerBoard getRivalBoard() {
        return player1Turn ? player2Board : player1Board;
    }

    private void endTurn() {
        selectedCard = null;
        for (int i = 0; i < 5; i++)
            if (getPlayerBoard().getMonsterCards()[i] != null)
                getPlayerBoard().getMonsterCards()[i].setHasAttacked(false);
        player1Turn = !player1Turn;
        playerAlreadySummoned = false;
    }

    public GamePhase getPhase() {
        return phase;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public boolean isPlayer1Turn() {
        return player1Turn;
    }

    /**
     * Painkiller is a cheat to increase the players health
     */
    public void painkiller() {
        getPlayerBoard().getPlayer().increaseHealth(8000);
    }

    private boolean isPlayableCardInRivalHand(PlayableCard card) {
        return getRivalBoard().getHand().stream().anyMatch(x -> x == card) || Arrays.stream(getRivalBoard().getMonsterCards()).anyMatch(x -> x == card)
                || Arrays.stream(getRivalBoard().getSpellCards()).anyMatch(x -> x == card) || getRivalBoard().getGraveyard().stream().anyMatch(x -> x == card) ||
                getRivalBoard().getField() == card;
    }
}
