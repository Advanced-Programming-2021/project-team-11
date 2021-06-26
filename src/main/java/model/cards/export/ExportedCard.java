package model.cards.export;

import com.google.gson.Gson;
import model.cards.*;

public class ExportedCard {
    private String name, description;
    private CardType cardType;
    private int price;
    // For monsters
    private int level, defence, attack;
    private MonsterType monsterType;
    private MonsterCardType monsterCardType;
    private MonsterAttributeType monsterAttributeType;
    // For spells
    private SpellCardType spellCardType;
    // For traps
    private TrapCardType trapCardType;

    public static String cardToJson(Card card) {
        Gson gson = new Gson();
        ExportedCard exportedCard = new ExportedCard();
        exportedCard.cardType = card.getCardType();
        exportedCard.description = card.getDescription();
        exportedCard.name = card.getName();
        exportedCard.price = card.getPrice();
        // Check monster
        if (card instanceof MonsterCard) {
            MonsterCard monsterCard = (MonsterCard) card;
            exportedCard.attack = monsterCard.getAttack();
            exportedCard.defence = monsterCard.getDefence();
            exportedCard.level = monsterCard.getLevel();
            exportedCard.monsterCardType = monsterCard.getMonsterCardType();
            exportedCard.monsterAttributeType = monsterCard.getMonsterAttributeType();
            exportedCard.monsterType = monsterCard.getMonsterType();
        } else if (card instanceof SpellCard) {
            exportedCard.spellCardType = ((SpellCard) card).getSpellCardType();
        } else if (card instanceof TrapCard) {
            exportedCard.trapCardType = ((TrapCard) card).getTrapCardType();
        }
        return gson.toJson(exportedCard);
    }

    public static ExportedCard jsonToExportedCard(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ExportedCard.class);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CardType getCardType() {
        return cardType;
    }

    public int getPrice() {
        return price;
    }

    public int getLevel() {
        return level;
    }

    public int getDefence() {
        return defence;
    }

    public int getAttack() {
        return attack;
    }

    public MonsterCardType getMonsterCardType() {
        return monsterCardType;
    }

    public MonsterType getMonsterType() {
        return monsterType;
    }

    public MonsterAttributeType getMonsterAttributeType() {
        return monsterAttributeType;
    }

    public SpellCardType getSpellCardType() {
        return spellCardType;
    }

    public TrapCardType getTrapCardType() {
        return trapCardType;
    }
}
