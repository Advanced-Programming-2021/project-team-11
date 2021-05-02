package model.cards;

import java.util.ArrayList;

public abstract class Card {
    private final String name, description;
    private final int price;
    private final CardType cardType;

    public Card(String name, String description, CardType cardType, int price) {
        this.name = name;
        this.description = description;
        this.cardType = cardType;
        this.price = price;
    }

    public CardType getCardType() {
        return cardType;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    abstract void activateEffect();

    public static ArrayList<Card> getAllCards() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(MonsterCard.getAllMonsterCards());
        cards.addAll(TrapCard.getAllTrapCards());
        cards.addAll(SpellCard.getAllSpellCards());
        return cards;
    }

    public static Card getCardByName(String name) {
        for (Card card : getAllCards())
            if (card.getName().equals(name))
                return card;
        return null;
    }
}
