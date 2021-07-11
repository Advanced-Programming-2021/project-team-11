package model.cards;

import controller.GeneralUtil;

public enum MonsterCardType {
    NORMAL,
    EFFECT,
    RITUAL;

    public static MonsterCardType valueOfCaseInsensitive(String value) {
        return MonsterCardType.valueOf(value.replace('-', '_').toUpperCase());
    }

    @Override
    public String toString() {
        return GeneralUtil.formatEnumName(super.toString());
    }
}
