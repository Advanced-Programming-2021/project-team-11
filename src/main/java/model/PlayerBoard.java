package model;

import model.cards.Card;
import model.exceptions.BooAnException;

import java.util.ArrayList;
import java.util.Collections;

public class PlayerBoard {
    private final Player player;
    private final PlayableCard[] spellCard = new PlayableCard[5];
    private final PlayableCard[] monsterCard = new PlayableCard[5];
    private final ArrayList<Card> graveyard = new ArrayList<>();
    private final Card[] hand = new Card[6];
    private final ArrayList<Card> deck;

    public PlayerBoard(Player player, ArrayList<Card> cards) {
        this.player = player;
        this.deck = cards;
    }

    public Player getPlayer() {
        return player;
    }

    public void reshuffleDeck() {
        Collections.shuffle(deck);
    }

    public ArrayList<Card> getGraveyard() {
        return graveyard;
    }

    public Card[] getHand() {
        return hand;
    }

    public void drawCard() {
        for (int i = 0; i < hand.length; i++)
            if (hand[i] == null) {
                hand[i] = deck.get(0);
                deck.remove(0);
                return;
            }
    }

    public void removeHandCard(int i) {
        hand[i] = null;
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
