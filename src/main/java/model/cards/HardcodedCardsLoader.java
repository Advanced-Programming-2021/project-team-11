package model.cards;

import model.cards.monsters.*;
import model.cards.spells.*;

public class HardcodedCardsLoader {
    public static void load() {
        loadMonsters();
        loadSpells();
        loadTraps();
    }

    private static void loadMonsters() {
        CommandKnight.getInstance();
        YomiShip.getInstance();
        ManEaterBug.getInstance();
        Marshmallon.getInstance();
        Suijin.getInstance();
        ScannerCard.getInstance();
        TheCalculator.getInstance();
        BeastKingBarbaros.getInstance();
        MirageDragon.getInstance();
        HeraldOfCreation.getInstance();
        ExploderDragon.getInstance();
        TerratigerTheEmpoweredWarrior.getInstance();
        TheTricky.getInstance();
        Texchanger.getInstance();
    }

    private static void loadSpells() {
        AdvancedRitualArt.getInstance();
        MonsterReborn.getInstance();
        Terraforming.getInstance();
        PotOfGreed.getInstance();
        Raigeki.getInstance();
        HarpiesFeatherDuster.getInstance();
    }

    private static void loadTraps() {
    }
}
