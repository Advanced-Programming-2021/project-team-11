package model.cards;

import model.cards.monsters.*;

public class HardcodedCardsLoader {
    public static void load() {
        loadMonsters();
        loadSpells();
        loadTraps();
    }

    private static void loadMonsters() {
        CommandKnight.makeInstance();
        YomiShip.makeInstance();
        ManEaterBug.makeInstance();
        Marshmallon.makeInstance();
    }

    private static void loadSpells() {

    }

    private static void loadTraps() {
    }
}
