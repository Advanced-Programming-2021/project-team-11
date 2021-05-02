package model.cards;

public class SimpleMonster extends MonsterCard {

    public SimpleMonster(String name, String description, CardType cardType, int price, int level, int defence, int attack, MonsterCardType monsterCardType, MonsterType monsterType, MonsterAttributeType monsterAttributeType) {
        super(name, description, cardType, price, level, defence, attack, monsterCardType, monsterType, monsterAttributeType);
    }

    @Override
    void activateEffect() {
        // Does nothing!
    }
}
