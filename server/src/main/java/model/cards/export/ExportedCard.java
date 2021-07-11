package model.cards.export;

import com.google.gson.Gson;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import model.cards.*;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

public class ExportedCard {
    private String name, description;
    private CardType cardType;
    private int price;
    // For monsters
    private int level, defence, attack;
    private MonsterType monsterType;
    private MonsterCardType monsterCardType;
    private MonsterAttributeType monsterAttributeType;
    // For spells
    private SpellCardType spellCardType;
    // For traps
    private TrapCardType trapCardType;

    private static ExportedCard generateExportedCard(Card card) {
        ExportedCard exportedCard = new ExportedCard();
        exportedCard.cardType = card.getCardType();
        exportedCard.description = card.getDescription();
        exportedCard.name = card.getName();
        exportedCard.price = card.getPrice();
        // Check monster
        if (card instanceof MonsterCard) {
            MonsterCard monsterCard = (MonsterCard) card;
            exportedCard.attack = monsterCard.getAttack();
            exportedCard.defence = monsterCard.getDefence();
            exportedCard.level = monsterCard.getLevel();
            exportedCard.monsterCardType = monsterCard.getMonsterCardType();
            exportedCard.monsterAttributeType = monsterCard.getMonsterAttributeType();
            exportedCard.monsterType = monsterCard.getMonsterType();
        } else if (card instanceof SpellCard) {
            exportedCard.spellCardType = ((SpellCard) card).getSpellCardType();
        } else if (card instanceof TrapCard) {
            exportedCard.trapCardType = ((TrapCard) card).getTrapCardType();
        }
        return exportedCard;
    }

    public static String cardToJson(Card card) {
        return new Gson().toJson(generateExportedCard(card));
    }

    public static String cardToCsv(Card card) {
        StringWriter writer = new StringWriter();
        StatefulBeanToCsv<ExportedCard> beanToCsv = new StatefulBeanToCsvBuilder<ExportedCard>(writer).build();
        try {
            beanToCsv.write(Collections.singletonList(generateExportedCard(card)));
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    public static ExportedCard jsonToExportedCard(String json) {
        return new Gson().fromJson(json, ExportedCard.class);
    }

    public static ExportedCard csvToExportedCard(String csv) {
        List<ExportedCard> beans = new CsvToBeanBuilder<ExportedCard>(new StringReader(csv))
                .withType(ExportedCard.class).build().parse();
        return beans.get(0);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CardType getCardType() {
        return cardType;
    }

    public int getPrice() {
        return price;
    }

    public int getLevel() {
        return level;
    }

    public int getDefence() {
        return defence;
    }

    public int getAttack() {
        return attack;
    }

    public MonsterCardType getMonsterCardType() {
        return monsterCardType;
    }

    public MonsterType getMonsterType() {
        return monsterType;
    }

    public MonsterAttributeType getMonsterAttributeType() {
        return monsterAttributeType;
    }

    public SpellCardType getSpellCardType() {
        return spellCardType;
    }

    public TrapCardType getTrapCardType() {
        return trapCardType;
    }
}
