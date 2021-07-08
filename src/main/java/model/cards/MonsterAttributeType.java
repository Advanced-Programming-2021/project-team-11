package model.cards;

import controller.GeneralUtil;

public enum MonsterAttributeType {
    EARTH,
    WATER,
    LIGHT,
    DARK,
    FIRE,
    WIND;

    public static MonsterAttributeType valueOfCaseInsensitive(String value) {
        return MonsterAttributeType.valueOf(value.replace('-', '_').toUpperCase());
    }

    @Override
    public String toString() {
        return GeneralUtil.formatEnumName(super.toString());
    }
}
