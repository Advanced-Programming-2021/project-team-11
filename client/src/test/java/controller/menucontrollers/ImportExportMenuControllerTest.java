package controller.menucontrollers;

import model.cards.*;
import model.cards.export.ExportedCard;
import model.cards.monsters.Suijin;
import model.cards.spells.Yami;
import model.cards.traps.TimeSeal;
import model.exceptions.InvalidCardToImportException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ImportExportMenuControllerTest {
    @Test
    void importMalformed() {
        ExportedCard card = ExportedCard.jsonToExportedCard("{\"name\":\"whrjejerj\",\"description\":\"kire khar\",\"cardType\":\"MONSTER\",\"price\":6969,\"level\":1,\"defence\":8585,\"attack\":420,\"monsterCardType\":\"EFFECT\",\"monsterType\":\"BEAST_WARRIOR\",\"monsterAttributeType\":\"WATER\"}");
        try {
            ImportExportMenuController.handleImport(card);
            Assertions.fail("card imported");
        } catch (InvalidCardToImportException ignored) {
        }
        card = ExportedCard.jsonToExportedCard("{\"name\":\"whrjejerj\",\"description\":\"kire khar\",\"cardType\":\"MONSTER\",\"price\":6969,\"level\":1,\"defence\":8585,\"attack\":420,\"monsterCardType\":\"gewgwegwewe\",\"monsterType\":\"BEAST_WARRIOR\",\"monsterAttributeType\":\"WATER\"}");
        try {
            ImportExportMenuController.handleImport(card);
            Assertions.fail("card imported");
        } catch (InvalidCardToImportException ignored) {
        }
        card = ExportedCard.jsonToExportedCard("{\"name\":\"whrjejerj\",\"description\":\"kire khar\",\"cardType\":\"3hgweherhjewr\",\"price\":6969,\"level\":1,\"defence\":8585,\"attack\":420,\"monsterCardType\":\"gewgwegwewe\",\"monsterType\":\"BEAST_WARRIOR\",\"monsterAttributeType\":\"WATER\"}");
        try {
            ImportExportMenuController.handleImport(card);
            Assertions.fail("card imported");
        } catch (InvalidCardToImportException ignored) {
        }
    }

    @Test
    void importSimpleMonster() {
        ExportedCard card = ExportedCard.jsonToExportedCard("{\"name\":\"kir\",\"description\":\"kire khar\",\"cardType\":\"MONSTER\",\"price\":6969,\"level\":1,\"defence\":8585,\"attack\":420,\"monsterCardType\":\"NORMAL\",\"monsterType\":\"BEAST_WARRIOR\",\"monsterAttributeType\":\"WATER\"}");
        try {
            ImportExportMenuController.handleImport(card);
        } catch (InvalidCardToImportException ex) {
            Assertions.fail(ex);
        }
        Assertions.assertNotNull(MonsterCard.getAllMonsterCardByName("kir"));
    }

    @Test
    void importRitualMonster() {
        ExportedCard card = ExportedCard.jsonToExportedCard("{\"name\":\"kir\",\"description\":\"kire khar\",\"cardType\":\"MONSTER\",\"price\":6969,\"level\":1,\"defence\":85850,\"attack\":4200,\"monsterCardType\":\"RITUAL\",\"monsterType\":\"BEAST_WARRIOR\",\"monsterAttributeType\":\"WATER\"}");
        try {
            ImportExportMenuController.handleImport(card);
        } catch (InvalidCardToImportException ex) {
            Assertions.fail(ex);
        }
        MonsterCard cardType = MonsterCard.getAllMonsterCardByName("kir");
        Assertions.assertNotNull(cardType);
        Assertions.assertEquals(4200, cardType.getAttack());
        Assertions.assertEquals(MonsterCardType.RITUAL, cardType.getMonsterCardType());
    }

    @Test
    void importEffectMonster() {
        Suijin suijin = Suijin.getInstance();
        suijin.initialize("...", 1234, 1, 4321, 9876, MonsterType.AQUA, MonsterAttributeType.WATER);
        ExportedCard exportedCard = ExportedCard.jsonToExportedCard(ExportedCard.cardToJson(suijin));
        try {
            ImportExportMenuController.handleImport(exportedCard);
        } catch (InvalidCardToImportException ex) {
            Assertions.fail(ex);
        }
        MonsterCard cardType = MonsterCard.getAllMonsterCardByName(suijin.getName());
        Assertions.assertNotNull(cardType);
        Assertions.assertEquals(9876, cardType.getAttack());
        Assertions.assertEquals(MonsterCardType.EFFECT, cardType.getMonsterCardType());
    }

    @Test
    void importSpell() {
        Yami yami = Yami.getInstance();
        yami.init("test", 3632673);
        ExportedCard exportedCard = ExportedCard.jsonToExportedCard(ExportedCard.cardToJson(yami));
        try {
            ImportExportMenuController.handleImport(exportedCard);
        } catch (InvalidCardToImportException ex) {
            Assertions.fail(ex);
        }
        SpellCard card = SpellCard.getAllSpellCardByName(yami.getName());
        Assertions.assertNotNull(card);
        Assertions.assertEquals(exportedCard.getDescription(), card.getDescription());
        Assertions.assertEquals(exportedCard.getPrice(), card.getPrice());
    }

    @Test
    void importTrap() {
        TimeSeal timeSeal = TimeSeal.getInstance();
        timeSeal.init("test", 473);
        ExportedCard exportedCard = ExportedCard.jsonToExportedCard(ExportedCard.cardToJson(timeSeal));
        try {
            ImportExportMenuController.handleImport(exportedCard);
        } catch (InvalidCardToImportException ex) {
            Assertions.fail(ex);
        }
        TrapCard card = TrapCard.getAllTrapCardByName(timeSeal.getName());
        Assertions.assertNotNull(card);
        Assertions.assertEquals(exportedCard.getDescription(), card.getDescription());
        Assertions.assertEquals(exportedCard.getPrice(), card.getPrice());
    }
}
