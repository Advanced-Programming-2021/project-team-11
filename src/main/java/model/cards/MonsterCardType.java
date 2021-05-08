package model.cards;

public enum MonsterCardType {
    NORMAL,
    EFFECT,
    RITUAL;

    public static MonsterCardType valueOfCaseInsensitive(String value) {
        return MonsterCardType.valueOf(value.replace('-', '_').toUpperCase());
    }
}
