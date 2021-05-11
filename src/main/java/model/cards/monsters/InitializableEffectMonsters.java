package model.cards.monsters;

import model.cards.MonsterAttributeType;
import model.cards.MonsterCard;
import model.cards.MonsterType;

public abstract class InitializableEffectMonsters extends MonsterCard {

    public InitializableEffectMonsters(String name) {
        super(name);
    }

    public final void initialize(String description, int price, int level, int defence, int attack, MonsterType monsterType, MonsterAttributeType monsterAttributeType) {
        setDescription(description);
        setPrice(price);
        setLevel(level);
        setDefence(defence);
        setAttack(attack);
        setMonsterType(monsterType);
        setMonsterAttributeType(monsterAttributeType);
        init();
    }

    @Override
    public final void deactivateEffect() {
        // Monsters don't have this
    }
}
