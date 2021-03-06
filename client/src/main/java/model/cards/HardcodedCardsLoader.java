package model.cards;

import model.cards.monsters.*;
import model.cards.spells.*;
import model.cards.traps.*;

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
        ChangeOfHeart.getInstance();
        DarkHole.getInstance();
        SwordsOfRevealingLight.getInstance();
        SupplySquad.getInstance();
        SpellAbsorption.getInstance();
        MessengerOfPeace.getInstance();
        Yami.getInstance();
        Forest.getInstance();
        ClosedForest.getInstance();
        Umiiruka.getInstance();
        SwordOfDarkDestruction.getInstance();
        BlackPendant.getInstance();
        UnitedWeStand.getInstance();
        MagnumShield.getInstance();
        TwinTwisters.getInstance();
        MysticalSpaceTyphoon.getInstance();
    }

    private static void loadTraps() {
        TimeSeal.getInstance();
        CallOfTheHaunted.getInstance();
        MirrorForce.getInstance();
        MindCrush.getInstance();
        MagicCylinder.getInstance();
        NegateAttack.getInstance();
        TrapHole.getInstance();
        TorrentialTribute.getInstance();
        MagicJammer.getInstance();
    }
}
