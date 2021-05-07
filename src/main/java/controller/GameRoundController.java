package controller;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.*;
import model.enums.AttackResult;
import model.enums.CardPlaceType;
import model.enums.GamePhase;
import model.enums.GameStatus;
import model.exceptions.*;
import model.results.MonsterAttackResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Stack;

public class GameRoundController {
    private final PlayerBoard player1Board;
    private final PlayerBoard player2Board;
    private final Stack<PlayableCard> chainLink;
    private PlayableCard selectedCard;
    private boolean player1Turn, playerAlreadySummoned = false;
    private SpellCard field;
    private GameStatus gameStatus;
    private GamePhase phase;

    GameRoundController(PlayerBoard player1Board, PlayerBoard player2Board, boolean player1Starting) {
        this.player1Board = player1Board;
        this.player2Board = player2Board;
        player1Turn = player1Starting;
        chainLink = new Stack<>();
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

    private void preAttackChecks() throws Exception {
        if (selectedCard == null)
            throw new NoCardSelectedException();
        if (selectedCard.getCardPlace() != CardPlaceType.MONSTER || isPlayableCardInRivalHand(selectedCard))
            throw new CantAttackCardException();
        if (phase != GamePhase.BATTLE_PHASE)
            throw new InvalidPhaseActionException();
        if (selectedCard.hasAttacked())
            throw new CardAlreadyAttackedException();
    }

    /**
     * Attacks to a monster.
     *
     * @param index The index of card in rival board. Please note that the range is [1,5]
     * @return The attack result
     * @throws Exception If anything goes wrong
     */
    public MonsterAttackResult attackToMonster(int index) throws Exception {
        preAttackChecks();
        PlayableCard toAttackCard = getRivalBoard().getMonsterCards()[index - 1];
        if (toAttackCard == null)
            throw new NoCardHereToAttackException();
        // Check the attack possibilities
        selectedCard.setHasAttacked(true);
        int myMonsterAttack = selectedCard.getAttackPower();
        if (toAttackCard.isAttacking()) {
            int rivalAttack = toAttackCard.getAttackPower();
            if (myMonsterAttack > rivalAttack) {
                int damageReceived = myMonsterAttack - rivalAttack;
                getRivalBoard().getPlayer().decreaseHealth(damageReceived);
                toAttackCard.sendToGraveyard();
                return new MonsterAttackResult(damageReceived, false, true, toAttackCard.getCard(), AttackResult.RIVAL_DESTROYED);
            } else if (myMonsterAttack < rivalAttack) {
                int damageReceived = rivalAttack - myMonsterAttack;
                getPlayerBoard().getPlayer().decreaseHealth(damageReceived);
                selectedCard.sendToGraveyard();
                selectedCard = null;
                return new MonsterAttackResult(damageReceived, false, true, toAttackCard.getCard(), AttackResult.ME_DESTROYED);
            } else {
                toAttackCard.sendToGraveyard();
                selectedCard.sendToGraveyard();
                selectedCard = null;
                return new MonsterAttackResult(0, false, true, toAttackCard.getCard(), AttackResult.DRAW);
            }
        } else {
            int rivalDefence = toAttackCard.getDefencePower();
            if (myMonsterAttack > rivalDefence) {
                toAttackCard.sendToGraveyard();
                return new MonsterAttackResult(0, toAttackCard.isHidden(), false, toAttackCard.getCard(), AttackResult.RIVAL_DESTROYED);
            } else if (myMonsterAttack < rivalDefence) {
                int damageReceived = rivalDefence - myMonsterAttack;
                getPlayerBoard().getPlayer().decreaseHealth(damageReceived);
                selectedCard.sendToGraveyard();
                selectedCard = null;
                return new MonsterAttackResult(damageReceived, toAttackCard.isHidden(), false, toAttackCard.getCard(), AttackResult.ME_DESTROYED);
            } else {
                return new MonsterAttackResult(0, toAttackCard.isHidden(), false, toAttackCard.getCard(), AttackResult.DRAW);
            }
        }
    }

    public int attackToPlayer() throws Exception {
        preAttackChecks();
        if (Arrays.stream(getRivalBoard().getMonsterCards()).anyMatch(Objects::nonNull))
            throw new CantAttackToPlayerException();

        int attacked = selectedCard.getAttackPower();
        getRivalBoard().getPlayer().decreaseHealth(attacked);
        selectedCard.setHasAttacked(true);
        return attacked;
    }

    public void setCard() throws Exception {
        if (selectedCard == null)
            throw new NoCardSelectedException();
        if (selectedCard.getCardPlace() != CardPlaceType.HAND || isPlayableCardInRivalHand(selectedCard))
            throw new CantSetCardException();
        if (phase != GamePhase.MAIN2 && phase != GamePhase.MAIN1)
            throw new InvalidPhaseActionException();

        if (selectedCard.getCard().getCardType() == CardType.MONSTER)
            setMonsterCard();
        else
            setSpellCard();
    }

    private void setMonsterCard() throws MonsterCardZoneFullException, AlreadySummonedException, NotEnoughCardsToTributeException, TributeNeededException {
        if (getPlayerBoard().isSpellZoneFull())
            throw new MonsterCardZoneFullException();
        if (playerAlreadySummoned)
            throw new AlreadySummonedException();
        int cardsToTribute = ((MonsterCard) selectedCard.getCard()).getCardsNeededToTribute();
        if (cardsToTribute == 0) { // GG! summon the card
            setSelectedMonsterCard();
            return;
        }
        if (cardsToTribute > getPlayerBoard().countActiveMonsterCards())
            throw new NotEnoughCardsToTributeException();
        throw new TributeNeededException(cardsToTribute);
    }

    private void setSpellCard() throws SpellCardZoneFullException {
        if (getPlayerBoard().isSpellZoneFull())
            throw new SpellCardZoneFullException();
        setSelectedSpellCard();
    }

    public void flipSummon() throws Exception {
        if (selectedCard == null)
            throw new NoCardSelectedYetException();
        if (selectedCard.getCardPlace() != CardPlaceType.MONSTER || isPlayableCardInRivalHand(selectedCard))
            throw new CantChangeCardPositionException();
        if (phase != GamePhase.MAIN2 && phase != GamePhase.MAIN1)
            throw new InvalidPhaseActionException();
        if (!(selectedCard.isHidden() && !selectedCard.isAttacking()) || selectedCard.isPositionChangedInThisTurn())
            throw new CantFlipSummonException();
        selectedCard.flipSummon();
    }


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
        int cardsToTribute = ((MonsterCard) selectedCard.getCard()).getCardsNeededToTribute();
        if (cardsToTribute == 0) { // GG! summon the card
            summonSelectedCard();
            return;
        }
        if (cardsToTribute > getPlayerBoard().countActiveMonsterCards())
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
        getPlayerBoard().removeHandCard(selectedCard);
        selectedCard = null;
        playerAlreadySummoned = true;
    }

    private void setSelectedMonsterCard() {
        selectedCard.setDefencing();
        getPlayerBoard().addMonsterCard(selectedCard);
        getPlayerBoard().removeHandCard(selectedCard);
        selectedCard = null;
        playerAlreadySummoned = true;
    }

    private void setSelectedSpellCard() {
        getPlayerBoard().addSpellCard(selectedCard);
        getPlayerBoard().removeHandCard(selectedCard);
        selectedCard = null;
    }

    public void setCardPosition(boolean attacking) throws Exception {
        if (selectedCard == null)
            throw new NoCardSelectedYetException();
        if (selectedCard.getCardPlace() != CardPlaceType.MONSTER || isPlayableCardInRivalHand(selectedCard)
                || selectedCard.isHidden())
            throw new CantChangeCardPositionException();
        if (phase != GamePhase.MAIN2 && phase != GamePhase.MAIN1)
            throw new InvalidPhaseActionException();
        if (attacking == selectedCard.isAttacking())
            throw new CardAlreadyInWantedPositionException();
        if (selectedCard.isPositionChangedInThisTurn())
            throw new CardPositionAlreadyChanged();
        selectedCard.swapAttackMode();
    }

    //    public void activeSpell() {
//    }
//
    public Card getSelectedCard() throws NoCardSelectedYetException, CardHiddenException {
        if (selectedCard == null)
            throw new NoCardSelectedYetException();
        if (selectedCard.isHidden())
            throw new CardHiddenException();
        return selectedCard.getCard();
    }


    public void surrender() {
        gameStatus = isPlayer1Turn() ? GameStatus.PLAYER1_SURRENDER : GameStatus.PLAYER2_SURRENDER;
    }

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
        Arrays.stream(getPlayerBoard().getMonsterCards()).filter(Objects::nonNull).forEach(x -> x.setHasAttacked(false));
        Arrays.stream(getPlayerBoard().getMonsterCards()).filter(Objects::nonNull).forEach(PlayableCard::resetPositionChangedInThisTurn);
        player1Turn = !player1Turn;
        playerAlreadySummoned = false;
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
