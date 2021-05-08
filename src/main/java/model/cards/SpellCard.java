package model.cards;

import java.util.ArrayList;
import java.util.Optional;

public abstract class SpellCard extends Card {
    private final static ArrayList<SpellCard> allSpellCards = new ArrayList<>();
    private final SpellCardType spellCardType;

    public SpellCard(String name, String description, int price, SpellCardType spellCardType) {
        super(name, description, CardType.SPELL, price);
        this.spellCardType = spellCardType;
        allSpellCards.add(this);
        init();
    }

    public SpellCardType getSpellCardType() {
        return spellCardType;
    }

    public static ArrayList<SpellCard> getAllSpellCards() {
        return allSpellCards;
    }

    public static SpellCard getSpellCardByName(String name) {
        Optional<SpellCard> card = getAllSpellCards().stream().filter(x -> x.getName().equals(name) && x.isInitialized()).findFirst();
        return card.orElse(null);
    }

    @Override
    public final String toString() {
        return String.format("Name: %s\n" +
                "Spell\n" +
                "Type: %s\n" +
                "Description: %s", getName(), getSpellCardType().toString(), getDescription());
    }
}
