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
        Suijin.makeInstance();
        ScannerCard.makeInstance();
        TheCalculator.makeInstance();
        BeastKingBarbaros.makeInstance();
        MirageDragon.makeInstance();
        HeraldOfCreation.makeInstance();
        ExploderDragon.makeInstance();
        TerratigerTheEmpoweredWarrior.makeInstance();
        TheTricky.makeInstance();
        Texchanger.makeInstance();
    }

    private static void loadSpells() {

    }

    private static void loadTraps() {
    }
}
