package model.cards;

import java.util.ArrayList;

public abstract class MonsterCard extends Card {
    private static final ArrayList<MonsterCard> allMonsterCards = new ArrayList<>();
    private final int level, defence, attack;
    private final MonsterCardType monsterCardType;
    private final MonsterType monsterType;
    private final MonsterAttributeType monsterAttributeType;

    public MonsterCard(String name, String description, CardType cardType, int price, int level, int defence, int attack, MonsterCardType monsterCardType, MonsterType monsterType, MonsterAttributeType monsterAttributeType) {
        super(name, description, cardType, price);
        this.level = level;
        this.defence = defence;
        this.attack = attack;
        this.monsterCardType = monsterCardType;
        this.monsterAttributeType = monsterAttributeType;
        this.monsterType = monsterType;
        allMonsterCards.add(this);
    }

    public int getLevel() {
        return level;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefence() {
        return defence;
    }

    public MonsterAttributeType getMonsterAttributeType() {
        return monsterAttributeType;
    }

    public MonsterCardType getMonsterCardType() {
        return monsterCardType;
    }

    public MonsterType getMonsterType() {
        return monsterType;
    }

    public int getCardsNeededToTribute() {
        return getCardsNeededToTribute(level);
    }

    public static int getCardsNeededToTribute(int level) {
        if (level <= 4)
            return 0;
        if (level <= 6)
            return 1;
        if (level <= 8)
            return 2;
        return 3;
    }

    public static ArrayList<MonsterCard> getAllMonsterCards() {
        return allMonsterCards;
    }

    public static MonsterCard getMonsterCardByName(String name) {
        for (MonsterCard card : allMonsterCards)
            if (card.getName().equals(name))
                return card;
        return null;
    }
}
