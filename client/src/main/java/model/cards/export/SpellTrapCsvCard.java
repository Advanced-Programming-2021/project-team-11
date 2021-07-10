package model.cards.export;

import model.cards.Card;
import model.cards.CardType;
import model.cards.SpellCard;

public class SpellTrapCsvCard {
    private String name, description;
    private CardType cardType;
    private int price;

    public static SpellTrapCsvCard cardToSpellTrapCsvCard(Card card) {
        SpellTrapCsvCard spellTrapCsvCard = new SpellTrapCsvCard();
        spellTrapCsvCard.description = card.getDescription();
        spellTrapCsvCard.name = card.getName();
        spellTrapCsvCard.price = card.getPrice();
        spellTrapCsvCard.cardType = card instanceof SpellCard ? CardType.SPELL : CardType.TRAP;
        return spellTrapCsvCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }
}
