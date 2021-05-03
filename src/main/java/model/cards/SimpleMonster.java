package model.cards;

public class SimpleMonster extends MonsterCard {

    public SimpleMonster(String name, String description, int price, int level, int defence, int attack, MonsterType monsterType, MonsterAttributeType monsterAttributeType) {
        super(name, description, CardType.MONSTER, price, level, defence, attack, MonsterCardType.NORMAL, monsterType, monsterAttributeType);
    }

    @Override
    void activateEffect() {
        // Does nothing!
    }
}
