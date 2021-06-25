package model.cards;

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
}
