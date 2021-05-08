package model.cards;

import model.cards.monsters.CommandKnight;

public class HardcodedCardsLoader {
    public static void load() {
        loadMonsters();
        loadSpells();
        loadTraps();
    }

    private static void loadMonsters() {
        CommandKnight.makeInstance();
    }

    private static void loadSpells() {

    }

    private static void loadTraps() {
        CommandKnight.makeInstance();
    }
}
