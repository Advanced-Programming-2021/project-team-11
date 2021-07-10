package model.cards;

import model.exceptions.CantSpecialSummonException;
import model.exceptions.CantUseSpellException;

import java.util.ArrayList;
import java.util.Optional;

public abstract class SpellCard extends Card {
    private final static ArrayList<SpellCard> allSpellCards = new ArrayList<>();
    private final boolean needsUserInteraction;
    private final SpellCardType spellCardType;

    /**
     * Creates an uninitialized spell card
     *
     * @param name          The name of card to create
     * @param spellCardType The spell card type
     */
    public SpellCard(String name, SpellCardType spellCardType, boolean needsUserInteraction) {
        super(name, "", CardType.SPELL, 0);
        this.spellCardType = spellCardType;
        this.needsUserInteraction = needsUserInteraction;
        allSpellCards.add(this);
    }

    public static ArrayList<SpellCard> getAllSpellCards() {
        return allSpellCards;
    }

    public static SpellCard getAllSpellCardByName(String name) {
        Optional<SpellCard> card = getAllSpellCards().stream().filter(x -> x.getName().equals(name)).findFirst();
        return card.orElse(null);
    }

    public SpellCardType getSpellCardType() {
        return spellCardType;
    }

    public void init(String description, int price) {
        setDescription(description);
        setPrice(price);
        init();
    }

    /**
     * User needs to choose some card or do something when the spell is activated
     *
     * @return True if user needs to do something
     */
    public boolean getUserNeedInteraction() {
        return this.needsUserInteraction;
    }

    /**
     * In some cards when the condition of using the card is not made, we have to somehow notify the user about it
     *
     * @throws CantSpecialSummonException Can't special summon the card
     * @throws CantUseSpellException      Can't use this spell. Might contain a message in it
     */
    public abstract void throwConditionNotMadeException() throws CantSpecialSummonException, CantUseSpellException;

    @Override
    public final String toString() {
        return String.format("Name: %s\nSpell\nType: %s\nDescription: %s", getName(), getSpellCardType().toString(), getDescription());
    }
}
