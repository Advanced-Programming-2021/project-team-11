package model.cards;

import model.PlayerBoard;

public class RitualMonster extends MonsterCard {
    public RitualMonster(String name, String description, int price, int level, int defence, int attack, MonsterType monsterType, MonsterAttributeType monsterAttributeType) {
        super(name, description, price, level, defence, attack, MonsterCardType.RITUAL, monsterType, monsterAttributeType);
    }

    @Override
    public void activateEffect() {
        // Does nothing!
    }

    @Override
    public void deactivateEffect() {
        // Does nothing!
    }

    @Override
    public boolean isConditionMade(PlayerBoard myBoard, PlayerBoard rivalBoard) {
        return false;
    }
}
