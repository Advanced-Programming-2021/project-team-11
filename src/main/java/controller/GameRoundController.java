package controller;

import model.PlayableCard;
import model.PlayerBoard;
import model.cards.*;
import model.cards.monsters.*;
import model.cards.spells.AdvancedRitualArt;
import model.cards.spells.EquipSpellCard;
import model.cards.spells.FieldSpellCard;
import model.cards.spells.MessengerOfPeace;
import model.enums.*;
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
    private final ArrayList<PlayableCard> changeOfHeartCards;
    private PlayableCard selectedCard;
    private boolean player1Turn, playerAlreadySummoned = false, isFirstBattle = true;
    private GameStatus gameStatus;
    private GamePhase phase;

    GameRoundController(PlayerBoard player1Board, PlayerBoard player2Board, boolean player1Starting) {
        this.player1Board = player1Board;
        this.player2Board = player2Board;
        player1Turn = player1Starting;
        chainLink = new Stack<>();
        gameStatus = GameStatus.ONGOING;
        phase = GamePhase.DRAW;
        changeOfHeartCards = new ArrayList<>();
        getPlayerBoard().drawCard();
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
                getPlayerBoard().tryApplyMessengerOfPeace();
                break;
            case STANDBY:
                phase = GamePhase.MAIN1;
                break;
            case MAIN1:
                phase = GamePhase.BATTLE_PHASE;
                break;
            case BATTLE_PHASE:
                isFirstBattle = false;
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

    /**
     * Selects an card from board
     *
     * @param index        The index to select. Please note that this index starts from 1 not zero!
     * @param fromOpponent Is this card from opponent
     * @param cardPlace    Where is this card
     * @throws NoCardFoundInPositionException If there is no card in that position
     */
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
            throw new CantAttackWithThisCardException();
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
        // Check Command Knight
        if (toAttackCard.getCard() instanceof CommandKnight && toAttackCard.isEffectConditionMet(getPlayerBoard(), getRivalBoard())
                || getPlayerBoard().isEffectOfSwordOfRevealingLightActive())
            throw new CantAttackWithThisCardException();
        // Check the attack possibilities
        selectedCard.setHasAttacked(true);
        MonsterAttackResult result = processAttackToMonster(toAttackCard);
        if (result.getBattleResult() == AttackResult.RIVAL_DESTROYED && toAttackCard.getCard() instanceof YomiShip)
            getPlayerBoard().sendToGraveyard(selectedCard);
        selectedCard = null;
        return result;
    }

    private MonsterAttackResult processAttackToMonster(PlayableCard toAttackCard) throws CantAttackWithThisCardException {
        int myMonsterAttack = selectedCard.getAttackPower(getPlayerBoard(), getField());
        if (isMessengerOfPeaceForbiddingTheAttack(myMonsterAttack))
            throw new CantAttackWithThisCardException();
        if (toAttackCard.getCard() instanceof Suijin && toAttackCard.isEffectConditionMet(getPlayerBoard(), getRivalBoard(), true)) {
            toAttackCard.activateEffect(getPlayerBoard(), getRivalBoard(), selectedCard);
            myMonsterAttack = 0;
        }
        if (toAttackCard.isAttacking()) {
            int rivalAttack = toAttackCard.getAttackPower(getRivalBoard(), getField());
            if (myMonsterAttack > rivalAttack) {
                int damageReceived = myMonsterAttack - rivalAttack;
                if (!(toAttackCard.getCard() instanceof Marshmallon))
                    getRivalBoard().sendToGraveyard(toAttackCard);
                if (toAttackCard.getCard() instanceof ExploderDragon)
                    toAttackCard.activateEffect(getRivalBoard(), getPlayerBoard(), selectedCard);
                else
                    getRivalBoard().getPlayer().decreaseHealth(damageReceived);
                return new MonsterAttackResult(damageReceived, false, true, toAttackCard.getCard(), AttackResult.RIVAL_DESTROYED);
            } else if (myMonsterAttack < rivalAttack) {
                int damageReceived = rivalAttack - myMonsterAttack;
                getPlayerBoard().getPlayer().decreaseHealth(damageReceived);
                getPlayerBoard().sendToGraveyard(selectedCard);
                return new MonsterAttackResult(damageReceived, false, true, toAttackCard.getCard(), AttackResult.ME_DESTROYED);
            } else {
                if (!(toAttackCard.getCard() instanceof Marshmallon))
                    getRivalBoard().sendToGraveyard(toAttackCard);
                getPlayerBoard().sendToGraveyard(selectedCard);
                return new MonsterAttackResult(0, false, true, toAttackCard.getCard(), AttackResult.DRAW);
            }
        } else {
            if (toAttackCard.isHidden() && toAttackCard.getCard() instanceof Marshmallon)
                getPlayerBoard().getPlayer().decreaseHealth(Marshmallon.getToReduceHp());
            boolean wasHidden = toAttackCard.isHidden();
            toAttackCard.makeVisible();
            int rivalDefence = toAttackCard.getDefencePower(getRivalBoard(), getField());
            if (myMonsterAttack > rivalDefence) {
                if (!(toAttackCard.getCard() instanceof Marshmallon))
                    getRivalBoard().sendToGraveyard(toAttackCard);
                if (toAttackCard.getCard() instanceof ExploderDragon)
                    toAttackCard.activateEffect(getRivalBoard(), getPlayerBoard(), selectedCard);
                return new MonsterAttackResult(0, wasHidden, false, toAttackCard.getCard(), AttackResult.RIVAL_DESTROYED);
            } else if (myMonsterAttack < rivalDefence) {
                int damageReceived = rivalDefence - myMonsterAttack;
                getPlayerBoard().getPlayer().decreaseHealth(damageReceived);
                getPlayerBoard().sendToGraveyard(selectedCard);
                return new MonsterAttackResult(damageReceived, wasHidden, false, toAttackCard.getCard(), AttackResult.ME_DESTROYED);
            } else {
                return new MonsterAttackResult(0, wasHidden, false, toAttackCard.getCard(), AttackResult.DRAW);
            }
        }
    }

    public int attackToPlayer() throws Exception {
        preAttackChecks();
        if (!getRivalBoard().isMonsterZoneEmpty() || isFirstBattle)
            throw new CantAttackToPlayerException();

        if (getPlayerBoard().isEffectOfSwordOfRevealingLightActive())
            throw new CantAttackToPlayerException();

        int attacked = selectedCard.getAttackPower(getPlayerBoard(), getField());
        if (isMessengerOfPeaceForbiddingTheAttack(attacked))
            throw new CantAttackWithThisCardException();
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

    private void setMonsterCard() throws MonsterCardZoneFullException, AlreadySummonedException, NotEnoughCardsToTributeException, TributeNeededException, CantSetCardException {
        if (((MonsterCard) selectedCard.getCard()).getMonsterCardType() == MonsterCardType.RITUAL)
            throw new CantSetCardException();
        if (getPlayerBoard().isMonsterZoneFull())
            throw new MonsterCardZoneFullException();
        if (playerAlreadySummoned)
            throw new AlreadySummonedException();
        int cardsToTribute = ((MonsterCard) selectedCard.getCard()).getCardsNeededToTribute();
        if (cardsToTribute == 0) { // GG! summon the card
            setSelectedMonsterCard();
            return;
        }
        if (cardsToTribute > getPlayerBoard().countActiveMonsterCards())
            throw new NotEnoughCardsToTributeException(selectedCard.getCard());
        throw new TributeNeededException(selectedCard.getCard(), cardsToTribute);
    }

    private void setSpellCard() throws SpellCardZoneFullException {
        if (getPlayerBoard().isSpellZoneFull())
            throw new SpellCardZoneFullException();
        if (selectedCard.getCard() instanceof FieldSpellCard)
            setFieldFromSelectedCard();
        else
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


    public void summonCard() throws NoCardSelectedYetException, CantSummonCardException, InvalidPhaseActionException, MonsterCardZoneFullException, AlreadySummonedException, NotEnoughCardsToTributeException, TributeNeededException, SpecialSummonNeededException {
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
        if (selectedCard.getCard() instanceof TheTricky)
            throw new SpecialSummonNeededException(selectedCard);
        int cardsToTribute = ((MonsterCard) selectedCard.getCard()).getCardsNeededToTribute();
        if (cardsToTribute == 0) { // GG! summon the card
            summonSelectedCard();
            return;
        }
        if (cardsToTribute > getPlayerBoard().countActiveMonsterCards())
            throw new NotEnoughCardsToTributeException(selectedCard.getCard());
        throw new TributeNeededException(selectedCard.getCard(), cardsToTribute);
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
        tributes.forEach(x -> getPlayerBoard().sendSpellToGraveyard(x));
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

    public ActivateSpellCallback activeSpell() throws NoCardSelectedException, OnlySpellCardsAllowedException, InvalidPhaseActionException, CardAlreadyAttackedException, MonsterEffectMustBeHandledException, RitualSummonNotPossibleException, CantSpecialSummonException, CantUseSpellException, SpellAlreadyActivatedException {
        if (selectedCard == null)
            throw new NoCardSelectedException();
        if (selectedCard.getCardPlace() == CardPlaceType.MONSTER && !selectedCard.hasEffectActivated())
            if (GameUtils.canMonsterCardEffectBeActivated(selectedCard.getCard()) && selectedCard.isEffectConditionMet(getPlayerBoard(), getPlayerBoard(), false))
                throw new MonsterEffectMustBeHandledException(selectedCard);
        if (selectedCard.getCardPlace() != CardPlaceType.SPELL && selectedCard.getCardPlace() != CardPlaceType.FIELD)
            throw new OnlySpellCardsAllowedException();
        if (phase != GamePhase.BATTLE_PHASE)
            throw new InvalidPhaseActionException();
        if (selectedCard.hasEffectActivated() || !selectedCard.isHidden())
            throw new SpellAlreadyActivatedException();
        return handleActivateSpellCard();
    }

    private ActivateSpellCallback handleActivateSpellCard() throws RitualSummonNotPossibleException, CantSpecialSummonException, CantUseSpellException {
        selectedCard.makeVisible();
        if (selectedCard.getCard() instanceof AdvancedRitualArt) {
            if (!AdvancedRitualArt.isRitualSummonPossible(getPlayerBoard()))
                throw new RitualSummonNotPossibleException();
            return ActivateSpellCallback.RITUAL;
        }
        if (!selectedCard.isEffectConditionMet(getPlayerBoard(), getPlayerBoard(), false))
            if (selectedCard.getCard() instanceof SpellCard)
                ((SpellCard) selectedCard.getCard()).throwConditionNotMadeException();
        if (selectedCard.getCard() instanceof SpellCard && ((SpellCard) selectedCard.getCard()).getUserNeedInteraction())
            return ActivateSpellCallback.NORMAL;
        if (selectedCard.getCard() instanceof EquipSpellCard)
            return ActivateSpellCallback.EQUIP;
        selectedCard.activateEffect(getPlayerBoard(), getRivalBoard(), null);
        return ActivateSpellCallback.DONE;
    }

    /**
     * Special summons a card without any checks
     *
     * @param card        The card to summon
     * @param isForPlayer Is this card for rival or player
     */
    public void specialSummon(MonsterCard card, boolean isForPlayer) {
        PlayerBoard board = isForPlayer ? getPlayerBoard() : getRivalBoard();
        board.addMonsterCard(new PlayableCard(card, CardPlaceType.HAND));
    }

    public Card getSelectedCard() throws NoCardSelectedYetException, CardHiddenException {
        if (selectedCard == null)
            throw new NoCardSelectedYetException();
        if (selectedCard.isHidden() && isPlayableCardInRivalHand(selectedCard))
            throw new CardHiddenException();
        return selectedCard.getCard();
    }

    public PlayableCard returnPlayableCard() {
        return selectedCard;
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
        resetCards(getPlayer1Board());
        resetCards(getPlayer2Board());
        restoreChangeOfHeart();
        getPlayer1Board().tryIncreaseSwordOfRevealingLightRound();
        getPlayer2Board().tryIncreaseSwordOfRevealingLightRound();
        player1Turn = !player1Turn;
        playerAlreadySummoned = false;
    }

    private void resetCards(PlayerBoard board) {
        Arrays.stream(board.getMonsterCards()).filter(Objects::nonNull).forEach(card -> card.setHasAttacked(false));
        Arrays.stream(board.getMonsterCards()).filter(Objects::nonNull).forEach(PlayableCard::resetPositionChangedInThisTurn);
        Arrays.stream(board.getMonsterCards()).filter(Objects::nonNull).forEach(PlayableCard::resetEffectActivateCounterRound);
        Arrays.stream(board.getMonsterCards()).filter(Objects::nonNull).forEach(card -> card.setMimicCard(null));
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public PlayableCard getField() {
        if (getPlayer1Board().getField() != null)
            return getPlayer1Board().getField();
        if (getPlayer2Board().getField() != null)
            return getPlayer2Board().getField();
        return null;
    }

    private void setFieldFromSelectedCard() {
        removeField();
        getPlayerBoard().setField(selectedCard);
        selectedCard.setCardPlace(CardPlaceType.FIELD);
        selectedCard = null;
    }

    private void removeField() {
        getPlayer1Board().setField(null);
        getPlayer2Board().setField(null);
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

    /**
     * Forces the player to win the round
     */
    public void nuke() {
        gameStatus = isPlayer1Turn() ? GameStatus.PLAYER1_WON : GameStatus.PLAYER2_WON;
    }

    private void restoreChangeOfHeart() {
        ArrayList<PlayableCard> backup = new ArrayList<>(changeOfHeartCards);
        backup.forEach(this::changeOfHeartSwapOwner);
    }

    /**
     * Moves the card from the person who owns it to the other player
     *
     * @param card The card to move
     */
    public void changeOfHeartSwapOwner(PlayableCard card) {
        PlayerBoard toRemoveFrom = isPlayableCardInRivalHand(card) ? getRivalBoard() : getPlayerBoard();
        PlayerBoard toAddTo = isPlayableCardInRivalHand(card) ? getPlayerBoard() : getRivalBoard();
        for (int i = 0; i < toRemoveFrom.getMonsterCards().length; i++)
            if (toRemoveFrom.getMonsterCards()[i] == card)
                toRemoveFrom.getMonsterCards()[i] = null;
        toAddTo.addMonsterCard(card);
        changeOfHeartCards.add(card);
    }

    private boolean isPlayableCardInRivalHand(PlayableCard card) {
        return getRivalBoard().getHand().stream().anyMatch(x -> x == card) || Arrays.stream(getRivalBoard().getMonsterCards()).anyMatch(x -> x == card)
                || Arrays.stream(getRivalBoard().getSpellCards()).anyMatch(x -> x == card) || getRivalBoard().getGraveyard().stream().anyMatch(x -> x == card) ||
                getRivalBoard().getField() == card;
    }

    private boolean isMessengerOfPeaceForbiddingTheAttack(int attackPoints) {
        return getRivalBoard().getMonsterCardsList().stream().anyMatch(card -> !card.isHidden() && card.getCard() instanceof MessengerOfPeace)
                && attackPoints >= MessengerOfPeace.getMaxAttack();
    }
}
