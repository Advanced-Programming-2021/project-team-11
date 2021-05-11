package model.cards;

import java.util.ArrayList;
import java.util.Optional;

public abstract class TrapCard extends Card {
    private final static ArrayList<TrapCard> allTrapCards = new ArrayList<>();
    private final TrapCardType trapCardType;

    /**
     * Creates an uninitialized spell card
     *
     * @param name         The name of card to create
     * @param trapCardType The trap card type
     */
    public TrapCard(String name, TrapCardType trapCardType) {
        super(name, "", CardType.TRAP, 0);
        this.trapCardType = trapCardType;
        allTrapCards.add(this);
    }

    public void init(String description, int price) {
        setDescription(description);
        setPrice(price);
        init();
    }

    public TrapCardType getTrapCardType() {
        return trapCardType;
    }

    public static ArrayList<TrapCard> getAllTrapCards() {
        return allTrapCards;
    }

    public static TrapCard getAllTrapCardByName(String name) {
        Optional<TrapCard> card = getAllTrapCards().stream().filter(x -> x.getName().equals(name)).findFirst();
        return card.orElse(null);
    }

    public static TrapCard getTrapCardByName(String name) {
        Optional<TrapCard> card = getAllTrapCards().stream().filter(x -> x.getName().equals(name) && x.isInitialized()).findFirst();
        return card.orElse(null);
    }

    @Override
    public final String toString() {
        return String.format("Name: %s\n" +
                "Trap\n" +
                "Type: %s\n" +
                "Description: %s", getName(), getTrapCardType().toString(), getDescription());
    }
}
