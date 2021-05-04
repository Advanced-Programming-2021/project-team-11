package controller;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.CardType;
import model.cards.SpellCard;
import model.enums.CardPlaceType;
import model.exceptions.*;

import java.util.ArrayList;

public class GameRoundController {
    private static enum Phase {
        DRAW,
        STANDBY,
        MAIN1,
        BATTLE_PHASE,
        MAIN2,
        END_PHASE,
    };
    private static enum GameStatus {
        ONGOING,
        PLAYER1_SURRENDER,
        PlAYER2_SURRENDER,
        PLAYER1_WON,
        PLAYER2_WON,
    };

    private final PlayerBoard player1Board;
    private final PlayerBoard player2Board;
    private final ArrayList<PlayableCard> chainLink;
    private PlayableCard selectedCard;
    private boolean player1Turn;
    private SpellCard field;
    private GameStatus gameStatus;
    private Phase phase;

    GameRoundController(PlayerBoard player1Board, PlayerBoard player2Board, boolean player1Starting) {
        this.player1Board = player1Board;
        this.player2Board = player2Board;
        player1Turn = player1Starting;
        chainLink = new ArrayList<>();
        gameStatus = GameStatus.ONGOING;
        phase = Phase.DRAW;
    }

    public void selectCard(int index, boolean fromPlayer1, CardPlaceType cardPlace) {
        switch (cardPlace) {
            case SPELL:
                selectedCard = getPlayerBoard().getSpellCards()[index - 1];
                break;
            case MONSTER:
                selectedCard = getPlayerBoard().getMonsterCards()[index - 1];
                break;
            case GRAVEYARD:
                selectedCard = getPlayerBoard().getGraveyard().get(index - 1);
                break;
            case HAND:
                selectedCard = getPlayerBoard().getHand()[index - 1];
                break;
            default:
                throw new BooAnException("Card place not found!");
        }
    }

    public void deselectCard() {
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
        if (phase != Phase.BATTLE_PHASE)
            throw new InvalidPhaseActionException();
        if (selectedCard.hasAttacked())
            throw new CardAlreadyAttackedException();
        for (int i = 0; i < 5; i++)
            if (getRivalBoard().getMonsterCards()[i] != null)
                throw new CantAttackToPlayerException();

        int attacked = selectedCard.getAttackDelta();
        getRivalBoard().getPlayer().decreaseHealth(attacked);
        selectedCard.setHasAttacked(true); endTurn();
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
        for (int i = 0; i < 6; i++)
            if (getPlayerBoard().getHand()[i].equals(selectedCard))
                getPlayerBoard().getHand()[i] = null;
        endTurn();
    }

//    public void flipSummon() {
//    }
//
//    public void summonCard(int[] tributes) {
//    }

    public void draw() {
        getPlayerBoard().drawCard();
        endTurn();
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
//    public void surrender() {
//    }
//
//    public void gameEnd() {
//    }
//
//    public String boardForPlayer() {
//    }

    public PlayerBoard getPlayer1Board() {
        return player1Board;
    }

    public PlayerBoard getPlayer2Board() {
        return player2Board;
    }

    private PlayerBoard getPlayerBoard() {
        return player1Turn? player1Board: player2Board;
    }

    private PlayerBoard getRivalBoard() {
        return player1Turn? player2Board: player1Board;
    }

    private void endTurn() {
        deselectCard();
        for (int i = 0; i < 5; i++)
            if (getPlayerBoard().getMonsterCards()[i] != null)
                getPlayerBoard().getMonsterCards()[i].setHasAttacked(false);
        player1Turn = !player1Turn;
    }
}
