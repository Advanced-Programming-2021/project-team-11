package model.cards;

import java.util.ArrayList;
import java.util.Optional;

public abstract class MonsterCard extends Card {
    private static final ArrayList<MonsterCard> allMonsterCards = new ArrayList<>();
    private int level, defence, attack;
    private final MonsterCardType monsterCardType;
    private MonsterType monsterType;
    private MonsterAttributeType monsterAttributeType;

    public MonsterCard(String name) {
        super(name, "", CardType.MONSTER, 0);
        this.monsterCardType = MonsterCardType.EFFECT;
        allMonsterCards.add(this);
    }

    public MonsterCard(String name, String description, int price, int level, int defence, int attack, MonsterCardType monsterCardType, MonsterType monsterType, MonsterAttributeType monsterAttributeType) {
        super(name, description, CardType.MONSTER, price);
        this.level = level;
        this.defence = defence;
        this.attack = attack;
        this.monsterCardType = monsterCardType;
        this.monsterAttributeType = monsterAttributeType;
        this.monsterType = monsterType;
        allMonsterCards.add(this);
        init();
    }

    public final int getLevel() {
        return level;
    }

    public final int getAttack() {
        return attack;
    }

    public final int getDefence() {
        return defence;
    }

    public final MonsterAttributeType getMonsterAttributeType() {
        return monsterAttributeType;
    }

    public final void setMonsterAttributeType(MonsterAttributeType monsterAttributeType) {
        this.monsterAttributeType = monsterAttributeType;
    }

    public final MonsterCardType getMonsterCardType() {
        return monsterCardType;
    }

    public final MonsterType getMonsterType() {
        return monsterType;
    }

    public final void setMonsterType(MonsterType monsterType) {
        this.monsterType = monsterType;
    }

    public final void setAttack(int attack) {
        this.attack = attack;
    }

    public final void setDefence(int defence) {
        this.defence = defence;
    }

    public final void setLevel(int level) {
        this.level = level;
    }

    public final int getCardsNeededToTribute() {
        return getCardsNeededToTribute(getLevel());
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

    public static MonsterCard getAllMonsterCardByName(String name) {
        Optional<MonsterCard> card = getAllMonsterCards().stream().filter(x -> x.getName().equals(name)).findFirst();
        return card.orElse(null);
    }

    public static MonsterCard getMonsterCardByName(String name) {
        Optional<MonsterCard> card = getAllMonsterCards().stream().filter(x -> x.getName().equals(name) && x.isInitialized()).findFirst();
        return card.orElse(null);
    }

    @Override
    public final String toString() {
        return String.format("Name: %s\n" +
                        "Level: %d\n" +
                        "Type: %s\n" +
                        "ATK: %d\n" +
                        "DEF: %d\n" +
                        "Description: %s",
                getName(), getLevel(), getMonsterType().toString(), getAttack(), getDefence(), getDescription());
    }
}
