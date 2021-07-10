package model.cards.export;

import model.cards.MonsterAttributeType;
import model.cards.MonsterCard;
import model.cards.MonsterCardType;
import model.cards.MonsterType;

public class MonsterCsvCard {
    private String name, description;
    private int price;
    private int level, defence, attack;
    private MonsterType monsterType;
    private MonsterCardType monsterCardType;
    private MonsterAttributeType monsterAttributeType;

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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public MonsterType getMonsterType() {
        return monsterType;
    }

    public void setMonsterType(MonsterType monsterType) {
        this.monsterType = monsterType;
    }

    public MonsterCardType getMonsterCardType() {
        return monsterCardType;
    }

    public void setMonsterCardType(MonsterCardType monsterCardType) {
        this.monsterCardType = monsterCardType;
    }

    public MonsterAttributeType getMonsterAttributeType() {
        return monsterAttributeType;
    }

    public void setMonsterAttributeType(MonsterAttributeType monsterAttributeType) {
        this.monsterAttributeType = monsterAttributeType;
    }

    public static MonsterCsvCard generateMonsterCsvCard(MonsterCard card) {
        MonsterCsvCard exportedCard = new MonsterCsvCard();
        exportedCard.description = card.getDescription();
        exportedCard.name = card.getName();
        exportedCard.price = card.getPrice();
        exportedCard.attack = card.getAttack();
        exportedCard.defence = card.getDefence();
        exportedCard.level = card.getLevel();
        exportedCard.monsterCardType = card.getMonsterCardType();
        exportedCard.monsterAttributeType = card.getMonsterAttributeType();
        exportedCard.monsterType = card.getMonsterType();
        return exportedCard;
    }
}
