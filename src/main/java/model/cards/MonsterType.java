package model.cards;

import controller.GeneralUtil;

public enum MonsterType {
    AQUA,
    BEAST,
    BEAST_WARRIOR,
    CYBERSE,
    DRAGON,
    FAIRY,
    FIEND,
    INSECT,
    MACHINE,
    PYRO,
    ROCK,
    SEA_SERPENT,
    SPELLCASTER,
    THUNDER,
    WARRIOR;

    public static MonsterType valueOfCaseInsensitive(String value) {
        return MonsterType.valueOf(value.replace('-', '_').replace(' ', '_').toUpperCase());
    }

    @Override
    public String toString() {
        return GeneralUtil.formatEnumName(super.toString());
    }
}
