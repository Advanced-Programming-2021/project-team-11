package model.database;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import model.cards.*;
import model.cards.export.MonsterCsvCard;
import model.cards.export.SpellTrapCsvCard;
import model.cards.monsters.InitializableEffectMonsters;
import model.exceptions.BooAnException;
import model.exceptions.ConfigLoadingException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CardLoader {
    private static String monsterFilename, spellFileName;
    private static boolean firstLoad = true;

    public static void loadCards(String monsterFilename, String spellFilename) {
        if (!firstLoad)
            throw new BooAnException("loadCards called twice!");
        CardLoader.monsterFilename = monsterFilename;
        CardLoader.spellFileName = spellFilename;
        HardcodedCardsLoader.load();
        // Load the csv
        try {
            loadMonsters();
            loadSpellsAndTraps();
        } catch (Exception ex) {
            throw new ConfigLoadingException(ex);
        }
        firstLoad = false;
    }

    public static void saveCards() {
        saveMonsters();
        saveSpellsAndTraps();
    }

    private static void saveMonsters() {
        try {
            FileWriter writer = new FileWriter(monsterFilename);
            StatefulBeanToCsv<MonsterCsvCard> beanToCsv = new StatefulBeanToCsvBuilder<MonsterCsvCard>(writer).build();
            beanToCsv.write(MonsterCard.getAllMonsterCards().stream().map(MonsterCsvCard::generateMonsterCsvCard));
            writer.close();
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException ex) {
            ex.printStackTrace();
        }
    }

    private static void saveSpellsAndTraps() {
        try {
            FileWriter writer = new FileWriter(spellFileName);
            StatefulBeanToCsv<SpellTrapCsvCard> beanToCsv = new StatefulBeanToCsvBuilder<SpellTrapCsvCard>(writer).build();
            beanToCsv.write(Card.getAllCards().stream().filter(card -> card instanceof SpellCard || card instanceof TrapCard).map(SpellTrapCsvCard::cardToSpellTrapCsvCard));
            writer.close();
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException ex) {
            ex.printStackTrace();
        }
    }

    private static void loadMonsters() throws FileNotFoundException {
        FileReader reader = new FileReader(monsterFilename);
        List<MonsterCsvCard> cards = new CsvToBeanBuilder<MonsterCsvCard>(reader)
                .withType(MonsterCsvCard.class).build().parse();
        for (MonsterCsvCard card : cards) {
            switch (card.getMonsterCardType()) {
                case NORMAL:
                    new SimpleMonster(card.getName(), card.getDescription(), card.getPrice(), card.getLevel(), card.getDefence(), card.getAttack(), card.getMonsterType(), card.getMonsterAttributeType());
                    break;
                case RITUAL:
                    new RitualMonster(card.getName(), card.getDescription(), card.getPrice(), card.getLevel(), card.getDefence(), card.getAttack(), card.getMonsterType(), card.getMonsterAttributeType());
                    break;
                case EFFECT:
                    if (card.getName().equals("Gate Guardian")) // This specific card can be handled just like normal cards!
                        new SimpleMonster(card.getName(), card.getDescription(), card.getPrice(), card.getLevel(), card.getDefence(), card.getAttack(), card.getMonsterType(), card.getMonsterAttributeType());
                    else {
                        MonsterCard monsterCard = MonsterCard.getAllMonsterCardByName(card.getName());
                        if (monsterCard instanceof InitializableEffectMonsters)
                            ((InitializableEffectMonsters) monsterCard).initialize(card.getDescription(), card.getPrice(), card.getLevel(), card.getDefence(), card.getAttack(), card.getMonsterType(), card.getMonsterAttributeType());
                    }
                    break;
            }
        }
    }

    private static void loadSpellsAndTraps() throws FileNotFoundException {
        FileReader reader = new FileReader(spellFileName);
        List<SpellTrapCsvCard> cards = new CsvToBeanBuilder<SpellTrapCsvCard>(reader)
                .withType(SpellTrapCsvCard.class).build().parse();
        for (SpellTrapCsvCard card : cards) {
            if (card.getCardType() == CardType.SPELL) {
                SpellCard spellCard = SpellCard.getAllSpellCardByName(card.getName());
                if (spellCard == null)
                    continue;
                spellCard.init(card.getDescription(), card.getPrice());
            } else if (card.getCardType() == CardType.TRAP) {
                TrapCard trapCard = TrapCard.getAllTrapCardByName(card.getName());
                if (trapCard == null)
                    continue;
                trapCard.init(card.getDescription(), card.getPrice());
            }
        }
    }
}
