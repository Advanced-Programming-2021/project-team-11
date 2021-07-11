package model.cards;

import model.PlayableCard;
import model.PlayerBoard;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class Card implements Comparable<Card> {
    private final CardType cardType;
    private final String name;
    private boolean initialized;
    private String description;
    private int price;

    /**
     * Creates a card. Please note that {@link #init()} is not called in the constructor
     *
     * @param name        The name of card. Must be unique
     * @param description The description of card
     * @param cardType    The card type
     * @param price       Price of card to buy it
     */
    public Card(String name, String description, CardType cardType, int price) {
        this.name = name;
        this.description = description;
        this.cardType = cardType;
        this.price = price;
    }

    public static ArrayList<Card> getAllCards() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(MonsterCard.getAllMonsterCards().stream().filter(Card::isInitialized).collect(Collectors.toList()));
        cards.addAll(TrapCard.getAllTrapCards().stream().filter(Card::isInitialized).collect(Collectors.toList()));
        cards.addAll(SpellCard.getAllSpellCards().stream().filter(Card::isInitialized).collect(Collectors.toList()));
        return cards;
    }

    public static Card getCardByName(String name) {
        Optional<Card> card = getAllCards().stream().filter(x -> x.getName().equals(name) && x.isInitialized()).findFirst();
        return card.orElse(null);
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

    public final boolean isInitialized() {
        return initialized;
    }

    public final void init() {
        initialized = true;
    }

    public final String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public final String getName() {
        return name;
    }

    public final int getSpeed() {
        if (this instanceof TrapCard && ((TrapCard) this).getTrapCardType() == TrapCardType.COUNTER)
            return 3;
        if (this instanceof TrapCard || (this instanceof SpellCard && ((SpellCard) this).getSpellCardType() == SpellCardType.QUICK_PLAY))
            return 2;
        return 1;
    }

    public abstract void activateEffect(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, PlayableCard rivalCard, int activationCounter);

    public abstract void deactivateEffect();

    public abstract boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard, PlayableCard thisCard, int activationCounter);

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
