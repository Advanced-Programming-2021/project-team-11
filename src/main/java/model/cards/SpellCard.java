package model.cards;

import java.util.ArrayList;
import java.util.Optional;

public abstract class SpellCard extends Card {
    private final static ArrayList<SpellCard> allSpellCards = new ArrayList<>();
    private final SpellCardType spellCardType;

    /**
     * Creates an uninitialized spell card
     *
     * @param name          The name of card to create
     * @param spellCardType The spell card type
     */
    public SpellCard(String name, SpellCardType spellCardType) {
        super(name, "", CardType.SPELL, 0);
        this.spellCardType = spellCardType;
        allSpellCards.add(this);
    }

    public SpellCardType getSpellCardType() {
        return spellCardType;
    }

    public void init(String description, int price) {
        setDescription(description);
        setPrice(price);
        init();
    }

    public static ArrayList<SpellCard> getAllSpellCards() {
        return allSpellCards;
    }

    public static SpellCard getAllSpellCardByName(String name) {
        Optional<SpellCard> card = getAllSpellCards().stream().filter(x -> x.getName().equals(name)).findFirst();
        return card.orElse(null);
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
