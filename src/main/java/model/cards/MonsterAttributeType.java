package model.cards;

public enum MonsterAttributeType {
    EARTH,
    DARK,
    WATER,
    FIRE,
    LIGHT,
    WIND;

    public static MonsterAttributeType valueOfCaseInsensitive(String value) {
        return MonsterAttributeType.valueOf(value.replace('-', '_').toUpperCase());
    }
}
