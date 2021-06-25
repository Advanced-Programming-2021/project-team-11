package model.database;

import com.opencsv.CSVReader;
import model.cards.*;
import model.cards.monsters.InitializableEffectMonsters;
import model.exceptions.BooAnException;
import model.exceptions.ConfigLoadingException;

import java.io.FileReader;

public class CardLoader {
    private static boolean firstLoad = true;

    public static void loadCards(String monsterFilename, String spellFilename) {
        if (!firstLoad)
            throw new BooAnException("loadCards called twice!");
        HardcodedCardsLoader.load();

        // Load the csv
        loadCsv(monsterFilename, CardLoader::loadMonster);
        loadCsv(spellFilename, args -> {
            if (args[1].equals("Trap"))
                loadTrap(args);
            else
                loadSpell(args);
        });
        firstLoad = false;
    }

    private static void loadCsv(String filename, Loader loader) {
        try {
            String[] nextRecord;
            CSVReader csvReader = new CSVReader(new FileReader(filename));
            for (boolean firstLine = true; (nextRecord = csvReader.readNext()) != null; firstLine = false)
                if (!firstLine) // skip the headers
                    loader.load(nextRecord);
        } catch (Exception e) {
            throw new ConfigLoadingException(e);
        }
    }

    private static void loadMonster(String[] data) {
        String name = data[0], description = data[7];
        MonsterType monsterType = MonsterType.valueOfCaseInsensitive(data[3]);
        MonsterCardType cardType = MonsterCardType.valueOfCaseInsensitive(data[4]);
        MonsterAttributeType attribute = MonsterAttributeType.valueOfCaseInsensitive(data[2]);
        int level = Integer.parseInt(data[1]), attack = Integer.parseInt(data[5]), defence = Integer.parseInt(data[6]), price = Integer.parseInt(data[8]);
        switch (cardType) {
            case NORMAL:
                new SimpleMonster(name, description, price, level, defence, attack, monsterType, attribute);
                break;
            case RITUAL:
                new RitualMonster(name, description, price, level, defence, attack, monsterType, attribute);
                break;
            case EFFECT:
                if (name.equals("Gate Guardian")) // This specific card can be handled just like normal cards!
                    new SimpleMonster(name, description, price, level, defence, attack, monsterType, attribute);
                else {
                    MonsterCard card = MonsterCard.getAllMonsterCardByName(name);
                    if (card instanceof InitializableEffectMonsters)
                        ((InitializableEffectMonsters) card).initialize(description, price, level, defence, attack, monsterType, attribute);
                }
                break;
        }
    }

    private static void loadSpell(String[] data) {
        SpellCard card = SpellCard.getAllSpellCardByName(data[0]);
        if (card == null)
            return;
        card.init(data[3], Integer.parseInt(data[5]));
    }

    private static void loadTrap(String[] data) {
        TrapCard card = TrapCard.getAllTrapCardByName(data[0]);
        if (card == null)
            return;
        card.init(data[3], Integer.parseInt(data[5]));
    }

    private interface Loader {
        void load(String[] args);
    }
}
