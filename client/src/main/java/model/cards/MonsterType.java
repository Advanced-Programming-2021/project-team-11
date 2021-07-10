package model.cards;

import controller.GeneralUtil;

public enum MonsterType {
    BEAST_WARRIOR,
    SEA_SERPENT,
    SPELLCASTER,
    WARRIOR,
    THUNDER,
    CYBERSE,
    MACHINE,
    DRAGON,
    INSECT,
    FAIRY,
    FIEND,
    BEAST,
    AQUA,
    PYRO,
    ROCK;

    public static MonsterType valueOfCaseInsensitive(String value) {
        return MonsterType.valueOf(value.replace('-', '_').replace(' ', '_').toUpperCase());
    }

    @Override
    public String toString() {
        return GeneralUtil.formatEnumName(super.toString());
    }
}
