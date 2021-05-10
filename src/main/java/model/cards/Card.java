package model.cards;

import model.PlayableCard;
import model.PlayerBoard;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Optional;

public abstract class Card implements Comparable<Card> {
    private final String name;
    private int price;
    private String description;
    private final CardType cardType;
    private boolean initialized = false;

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

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public final boolean isInitialized() {
        return initialized;
    }

    public final void init() {
        initialized = true;
    }

    public final String getDescription() {
        return description;
    }

    public final String getName() {
        return name;
    }

    public abstract void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter);

    public abstract void deactivateEffect();

    public abstract boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter);

    public static ArrayList<Card> getAllCards() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(MonsterCard.getAllMonsterCards());
        cards.addAll(TrapCard.getAllTrapCards());
        cards.addAll(SpellCard.getAllSpellCards());
        return cards;
    }

    public static Card getCardByName(String name) {
        Optional<Card> card = getAllCards().stream().filter(x -> x.getName().equals(name) && x.isInitialized()).findFirst();
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
