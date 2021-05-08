package model.cards;

import java.util.ArrayList;
import java.util.Optional;

public abstract class TrapCard extends Card {
    private final static ArrayList<TrapCard> allTrapCards = new ArrayList<>();
    private final TrapCardType trapCardType;

    public TrapCard(String name, String description, int price, TrapCardType trapCardType) {
        super(name, description, CardType.TRAP, price);
        this.trapCardType = trapCardType;
        allTrapCards.add(this);
        init();
    }

    public TrapCardType getTrapCardType() {
        return trapCardType;
    }

    public static ArrayList<TrapCard> getAllTrapCards() {
        return allTrapCards;
    }

    public static TrapCard getTrapCardByName(String name) {
        Optional<TrapCard> card = getAllTrapCards().stream().filter(x -> x.getName().equals(name) && x.isInitialized()).findFirst();
        return card.orElse(null);
    }

    @Override
    public String toString() {
        return String.format("Name: %s\n" +
                "Trap\n" +
                "Type: %s\n" +
                "Description: %s", getName(), getTrapCardType().toString(), getDescription());
    }
}
