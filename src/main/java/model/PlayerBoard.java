package model;

import model.cards.Card;
import model.enums.CardPlaceType;
import model.exceptions.BooAnException;

import java.util.ArrayList;
import java.util.Collections;

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

    public void removeMonsterCard(int i) {
        monsterCard[i] = null;
    }

    public void removeSpellCard(int i) {
        spellCard[i] = null;
    }

    public int addMonsterCard(PlayableCard card) {
        for (int i = 0; i < monsterCard.length; i++)
            if (monsterCard[i] == null) {
                monsterCard[i] = card;
                return i;
            }
        throw new BooAnException("Monster deck is full");
    }

    public int addSpellCard(PlayableCard card) {
        for (int i = 0; i < spellCard.length; i++)
            if (spellCard[i] == null) {
                spellCard[i] = card;
                return i;
            }
        throw new BooAnException("Spell deck is full");
    }

    public PlayableCard[] getMonsterCards() {
        return monsterCard;
    }

    public PlayableCard[] getSpellCards() {
        return spellCard;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }
}
