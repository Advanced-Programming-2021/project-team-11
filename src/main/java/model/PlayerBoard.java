package model;

import model.cards.Card;
import model.cards.TrapCard;
import model.cards.spells.MessengerOfPeace;
import model.cards.spells.SupplySquad;
import model.cards.spells.SwordsOfRevealingLight;
import model.enums.CardPlaceType;
import model.exceptions.BooAnException;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerBoard {
    private final ArrayList<PlayableCard> hand = new ArrayList<>(6);
    private final ArrayList<PlayableCard> graveyard = new ArrayList<>();
    private final PlayableCard[] monsterCard = new PlayableCard[5];
    private final PlayableCard[] spellCard = new PlayableCard[5];
    private final ArrayList<Card> deck;
    private final Player player;
    private PlayableCard field;
    /**
     * The effect of {@link model.cards.spells.SwordsOfRevealingLight} stage
     * 0 means not in effect, other values mean that the card is in effect and at nth round
     */
    private int effectOfSwordsOfRevealingLightStage = 0;

    public PlayerBoard(Player player, ArrayList<Card> cards) {
        this.player = player;
        this.deck = cards;
        shuffleDeck();
        // Setup hand
        for (int i = 0; i < 5; i++) {
            hand.add(new PlayableCard(cards.get(0), CardPlaceType.HAND));
            cards.remove(0);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void shuffleDeck() {
        Collections.shuffle(deck);
    }

    public ArrayList<PlayableCard> getGraveyard() {
        return graveyard;
    }

    public ArrayList<PlayableCard> getHand() {
        return hand;
    }

    /**
     * Tries to draw a card from players deck
     *
     * @return If there is no more cards to draw, returns false
     */
    public boolean drawCard() {
        if (deck.size() == 0)
            return false;
        if (hand.size() < 6) {
            hand.add(new PlayableCard(deck.get(0), CardPlaceType.HAND));
            deck.remove(0);
        }
        return true;
    }

    public PlayableCard getField() {
        return field;
    }

    public void setField(PlayableCard field) {
        this.field = field;
    }

    public void removeHandCard(PlayableCard card) {
        hand.remove(card);
    }

    public void sendMonsterToGraveyard(int cardPosition) {
        tryHandleSupplySquad();
        monsterCard[cardPosition].sendToGraveyard();
        graveyard.add(monsterCard[cardPosition]);
        monsterCard[cardPosition] = null;
    }

    public void addMonsterCard(PlayableCard card) {
        card.setCardPlace(CardPlaceType.MONSTER);
        for (int i = 0; i < monsterCard.length; i++)
            if (monsterCard[i] == null) {
                monsterCard[i] = card;
                return;
            }
        throw new BooAnException("Monster zone is full");
    }

    public void addSpellCard(PlayableCard card) {
        card.setCardPlace(CardPlaceType.SPELL);
        for (int i = 0; i < spellCard.length; i++)
            if (spellCard[i] == null) {
                spellCard[i] = card;
                return;
            }
        throw new BooAnException("Spell zone is full");
    }

    public boolean isMonsterZoneFull() {
        return Arrays.stream(monsterCard).noneMatch(Objects::isNull);
    }

    public boolean isMonsterZoneEmpty() {
        return Arrays.stream(getMonsterCards()).allMatch(Objects::isNull);
    }

    public boolean isSpellZoneFull() {
        return Arrays.stream(spellCard).noneMatch(Objects::isNull);
    }

    public int countActiveMonsterCards() {
        return getMonsterCardsList().size();
    }

    public PlayableCard[] getMonsterCards() {
        return monsterCard;
    }

    public ArrayList<PlayableCard> getMonsterCardsList() {
        return Arrays.stream(monsterCard).filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new));
    }

    public PlayableCard[] getSpellCards() {
        return spellCard;
    }

    public ArrayList<PlayableCard> getSpellCardsList() {
        return Arrays.stream(spellCard).filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void sendToGraveyard(PlayableCard card) {
        for (int i = 0; i < monsterCard.length; i++)
            if (card == monsterCard[i]) {
                tryHandleSupplySquad();
                monsterCard[i] = null;
            }
        for (int i = 0; i < spellCard.length; i++)
            if (card == spellCard[i])
                spellCard[i] = null;
        if (card.getEquippedCard() != null)
            for (int i = 0; i < spellCard.length; i++)
                if (spellCard[i] != null && spellCard[i].getCard() == card.getEquippedCard()) {
                    spellCard[i].sendToGraveyard();
                    spellCard[i] = null;
                    break;
                }
        card.sendToGraveyard();
        graveyard.add(card);
    }

    private void tryHandleSupplySquad() {
        Optional<PlayableCard> supplySquadCard = getSpellCardsList().stream().filter(c -> c.getCard() instanceof SupplySquad && !c.isHidden() && c.isEffectConditionMet(this, null, false)).findFirst();
        supplySquadCard.ifPresent(playableCard -> playableCard.activateEffect(this, null, null));
    }

    public void tryIncreaseSwordOfRevealingLightRound() {
        if (this.effectOfSwordsOfRevealingLightStage != 0)
            this.effectOfSwordsOfRevealingLightStage++;
        if (this.effectOfSwordsOfRevealingLightStage > 3 * 2) { // 3 rounds for this player and 3 for the other
            this.effectOfSwordsOfRevealingLightStage = 0;
            getSpellCardsList().stream().filter(card -> card.getCard() instanceof SwordsOfRevealingLight).forEach(this::sendToGraveyard);
        }
    }

    public boolean isEffectOfSwordOfRevealingLightActive() {
        return this.effectOfSwordsOfRevealingLightStage != 0;
    }

    public void activateSwordOfRevealingLight() {
        this.effectOfSwordsOfRevealingLightStage = 1;
    }

    public void tryApplyMessengerOfPeace() {
        getSpellCardsList().stream().filter(card -> !card.isHidden() && card.getCard() instanceof MessengerOfPeace).findFirst().ifPresent(card -> card.activateEffect(this, null, null));
    }

    public void removeSpellTrapCard(TrapCard card) {
        for (PlayableCard playableCard : getSpellCardsList())
            if (card.getName().equals(playableCard.getCard().getName())) {
                sendToGraveyard(playableCard);
                return;
            }
    }
}