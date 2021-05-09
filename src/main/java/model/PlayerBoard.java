package model;

import model.cards.Card;
import model.enums.CardPlaceType;
import model.exceptions.BooAnException;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerBoard {
    private final Player player;
    private final PlayableCard[] spellCard = new PlayableCard[5];
    private final PlayableCard[] monsterCard = new PlayableCard[5];
    private final ArrayList<PlayableCard> graveyard = new ArrayList<>();
    private final ArrayList<PlayableCard> hand = new ArrayList<>(6);
    private final ArrayList<Card> deck;
    private PlayableCard field;

    public PlayerBoard(Player player, ArrayList<Card> cards) {
        this.player = player;
        this.deck = cards;
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

    public void removeHandCard(int index) {
        hand.remove(index);
    }

    public void removeHandCard(PlayableCard card) {
        hand.remove(card);
    }

    public void sendMonsterToGraveyard(int cardPosition) {
        monsterCard[cardPosition].sendToGraveyard();
        monsterCard[cardPosition] = null;
    }

    public void sendSpellToGraveyard(int cardPosition) {
        spellCard[cardPosition].sendToGraveyard();
        spellCard[cardPosition] = null;
    }

    public void removeSpellCard(int i) {
        spellCard[i] = null;
    }

    public int addMonsterCard(PlayableCard card) {
        card.setCardPlace(CardPlaceType.MONSTER);
        for (int i = 0; i < monsterCard.length; i++)
            if (monsterCard[i] == null) {
                monsterCard[i] = card;
                return i;
            }
        throw new BooAnException("Monster zone is full");
    }

    public int addSpellCard(PlayableCard card) {
        card.setCardPlace(CardPlaceType.SPELL);
        for (int i = 0; i < spellCard.length; i++)
            if (spellCard[i] == null) {
                spellCard[i] = card;
                return i;
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
        return (int) Arrays.stream(monsterCard).filter(Objects::nonNull).count();
    }

    public PlayableCard[] getMonsterCards() {
        return monsterCard;
    }

    public ArrayList<PlayableCard> getMonsterCardsList() {
        return Arrays.stream(monsterCard).filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public PlayableCard[] getSpellCards() {
        return spellCard;
    }

    public ArrayList<PlayableCard> getSpellCardsList() {
        return Arrays.stream(spellCard).filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void sendToGraveyard(PlayableCard card) {
        for (int i = 0; i < monsterCard.length; i++)
            if (card == monsterCard[i])
                monsterCard[i] = null;
        for (int i = 0; i < spellCard.length; i++)
            if (card == spellCard[i])
                spellCard[i] = null;
        card.sendToGraveyard();
        graveyard.add(card);
    }
}
