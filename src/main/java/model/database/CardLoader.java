package model.database;

import com.opencsv.CSVReader;
import model.cards.*;
import model.cards.monsters.EffectMonsters;
import model.exceptions.BooAnException;
import model.exceptions.ConfigLoadingException;

import java.io.FileReader;

public class CardLoader {
    private static boolean firstLoad = true;

    public static void loadCards(String monsterFilename, String spellFilename) {
        if (!firstLoad)
            throw new BooAnException("loadCards called twice!");
        loadHardcodedCards();
        // Load the csv
        loadMonsters(monsterFilename);
        firstLoad = false;
    }

    private static void loadHardcodedCards() {
        HardcodedCardsLoader.load();
    }

    private static void loadMonsters(String filename) {
        boolean firstLine = true;
        try {
            FileReader filereader = new FileReader(filename);
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                if (firstLine) { // skip the headers
                    firstLine = false;
                    continue;
                }
                loadMonster(nextRecord);
            }
        } catch (Exception e) {
            throw new ConfigLoadingException(e);
        }
    }

    private static void loadMonster(String[] data) {
        String name = data[0], description = data[7];
        int level = Integer.parseInt(data[1]), attack = Integer.parseInt(data[5]), defence = Integer.parseInt(data[6]),
                price = Integer.parseInt(data[8]);
        MonsterAttributeType attribute = MonsterAttributeType.valueOfCaseInsensitive(data[2]);
        MonsterType monsterType = MonsterType.valueOfCaseInsensitive(data[3]);
        MonsterCardType cardType = MonsterCardType.valueOfCaseInsensitive(data[4]);
        switch (cardType) {
            case NORMAL:
                new SimpleMonster(name, description, price, level, defence, attack, monsterType, attribute);
                break;
            case RITUAL:
                new RitualMonster(name, description, price, level, defence, attack, monsterType, attribute);
                break;
            case EFFECT:
                if (name.equals("Gate Guardian"))
                    new SimpleMonster(name, description, price, level, defence, attack, monsterType, attribute);
                MonsterCard card = MonsterCard.getAllMonsterCardByName(name);
                if (card instanceof EffectMonsters)
                    ((EffectMonsters) card).initialize(description, price, level, defence, attack, monsterType, attribute);
                break;
        }
    }
}
