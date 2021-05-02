package model.cards;

import java.util.ArrayList;

public abstract class TrapCard extends Card {
    private final static ArrayList<TrapCard> allTrapCards = new ArrayList<>();
    private final TrapCardType trapCardType;

    public TrapCard(String name, String description, CardType cardType, int price, TrapCardType trapCardType) {
        super(name, description, cardType, price);
        this.trapCardType = trapCardType;
        allTrapCards.add(this);
    }

    public TrapCardType getTrapCardType() {
        return trapCardType;
    }

    public static ArrayList<TrapCard> getAllTrapCards() {
        return allTrapCards;
    }

    public static TrapCard getTrapCardByName(String name) {
        for (TrapCard card : allTrapCards)
            if (card.getName().equals(name))
                return card;
        return null;
    }
}
