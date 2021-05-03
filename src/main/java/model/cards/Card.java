package model.cards;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Optional;

public abstract class Card implements Comparable<Card> {
    private final String name, description;
    private final int price;
    private final CardType cardType;

    public Card(String name, String description, CardType cardType, int price) {
        this.name = name;
        this.description = description;
        this.cardType = cardType;
        this.price = price;
    }

    public final CardType getCardType() {
        return cardType;
    }

    public final int getPrice() {
        return price;
    }

    public final String getDescription() {
        return description;
    }

    public final String getName() {
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
        Optional<Card> card = getAllCards().stream().filter(x -> x.getName().equals(name)).findFirst();
        return card.orElse(null);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return this.name.equals(((Card) o).name);
    }

    @Override
    public final int hashCode() {
        return name.hashCode();
    }

    @Override
    public final int compareTo(@NotNull Card o) {
        return getName().compareTo(o.getName());
    }
}
