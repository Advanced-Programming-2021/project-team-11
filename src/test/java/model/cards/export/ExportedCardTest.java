package model.cards.export;

import com.google.gson.JsonParseException;
import model.cards.*;
import model.cards.spells.ChangeOfHeart;
import model.cards.traps.TimeSeal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExportedCardTest {
    @Test
    void testMalformed() {
        try {
            ExportedCard.jsonToExportedCard("kobs");
            Assertions.fail("try catch didn't throw exception");
        } catch (JsonParseException ignored) {
        }
    }

    @Test
    void testMonster() {
        MonsterCard card = new SimpleMonster("kir", "kire khar", 6969, 1, 8585, 420, MonsterType.BEAST_WARRIOR, MonsterAttributeType.WATER);
        String json = ExportedCard.cardToJson(card);
        ExportedCard exportedCard = ExportedCard.jsonToExportedCard(json);
        Assertions.assertEquals(card.getName(), exportedCard.getName());
        Assertions.assertEquals(card.getPrice(), exportedCard.getPrice());
        Assertions.assertEquals(card.getDescription(), exportedCard.getDescription());
        Assertions.assertEquals(card.getCardType(), exportedCard.getCardType());
        Assertions.assertEquals(card.getAttack(), exportedCard.getAttack());
        Assertions.assertEquals(card.getDefence(), exportedCard.getDefence());
        Assertions.assertEquals(card.getLevel(), exportedCard.getLevel());
        Assertions.assertEquals(card.getMonsterAttributeType(), exportedCard.getMonsterAttributeType());
        Assertions.assertEquals(card.getMonsterCardType(), exportedCard.getMonsterCardType());
        Assertions.assertEquals(card.getMonsterType(), exportedCard.getMonsterType());
        MonsterCard.getAllMonsterCards().remove(card);
    }

    @Test
    void testTrap() {
        ChangeOfHeart card = ChangeOfHeart.getInstance();
        card.init("test", 1000);
        String json = ExportedCard.cardToJson(card);
        ExportedCard exportedCard = ExportedCard.jsonToExportedCard(json);
        Assertions.assertEquals(card.getName(), exportedCard.getName());
        Assertions.assertEquals(card.getPrice(), exportedCard.getPrice());
        Assertions.assertEquals(card.getDescription(), exportedCard.getDescription());
        Assertions.assertEquals(card.getCardType(), exportedCard.getCardType());
        Assertions.assertEquals(card.getSpellCardType(), exportedCard.getSpellCardType());
    }

    @Test
    void testSpell() {
        TimeSeal card = TimeSeal.getInstance();
        card.init("trejest", 1036300);
        String json = ExportedCard.cardToJson(card);
        ExportedCard exportedCard = ExportedCard.jsonToExportedCard(json);
        Assertions.assertEquals(card.getName(), exportedCard.getName());
        Assertions.assertEquals(card.getPrice(), exportedCard.getPrice());
        Assertions.assertEquals(card.getDescription(), exportedCard.getDescription());
        Assertions.assertEquals(card.getCardType(), exportedCard.getCardType());
        Assertions.assertEquals(card.getTrapCardType(), exportedCard.getTrapCardType());
    }
}
