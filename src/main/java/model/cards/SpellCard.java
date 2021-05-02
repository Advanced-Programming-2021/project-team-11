package model.cards;

import java.util.ArrayList;

public abstract class SpellCard extends Card {
    private final static ArrayList<SpellCard> allSpellCards = new ArrayList<>();
    private final SpellCardType spellCardType;

    public SpellCard(String name, String description, CardType cardType, int price, SpellCardType spellCardType) {
        super(name, description, cardType, price);
        this.spellCardType = spellCardType;
        allSpellCards.add(this);
    }

    public SpellCardType getSpellCardType() {
        return spellCardType;
    }

    public static ArrayList<SpellCard> getAllSpellCards() {
        return allSpellCards;
    }

    public static SpellCard getSpellCardByName(String name) {
        for (SpellCard card : allSpellCards)
            if (card.getName().equals(name))
                return card;
        return null;
    }
}
