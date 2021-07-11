package controller.menucontrollers;

import model.cards.*;
import model.cards.export.ExportedCard;
import model.exceptions.InvalidCardToImportException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImportExportMenuController {
    public static String readFile(String filename) throws IOException {
        StringBuilder fileLines = new StringBuilder();
        Files.readAllLines(Paths.get(filename)).forEach(line -> fileLines.append(line).append('\n'));
        return fileLines.toString();
    }

    public static void writeFile(String filename, String content) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.append(content);
        writer.close();
    }

    public static void handleImport(ExportedCard exportedCard) throws InvalidCardToImportException {
        if (exportedCard.getCardType() == null)
            throw new InvalidCardToImportException();
        switch (exportedCard.getCardType()) {
            case MONSTER:
                handleMonsterImport(exportedCard);
                break;
            case SPELL:
                handleSpellImport(exportedCard);
                break;
            case TRAP:
                handleTrapImport(exportedCard);
                break;
        }
    }

    private static void handleMonsterImport(ExportedCard exportedCard) throws InvalidCardToImportException {
        if (exportedCard.getMonsterCardType() == null)
            throw new InvalidCardToImportException("Monster card type is not valid");
        switch (exportedCard.getMonsterCardType()) {
            case EFFECT:
                handleEffectMonsterCard(exportedCard);
                break;
            case RITUAL:
                handleRitualMonsterCard(exportedCard);
                break;
            case NORMAL:
                handleNormalMonsterCard(exportedCard);
                break;
        }
    }

    private static void handleEffectMonsterCard(ExportedCard exportedCard) throws InvalidCardToImportException {
        MonsterCard card = MonsterCard.getAllMonsterCardByName(exportedCard.getName());
        if (card == null)
            throw new InvalidCardToImportException("card does not exists!");
        card.setLevel(exportedCard.getLevel());
        card.setPrice(exportedCard.getPrice());
        card.setAttack(exportedCard.getAttack());
        card.setDefence(exportedCard.getDefence());
        card.setDescription(exportedCard.getDescription());
        card.setMonsterType(exportedCard.getMonsterType());
        card.setMonsterAttributeType(exportedCard.getMonsterAttributeType());
        card.init();
    }

    private static void handleNormalMonsterCard(ExportedCard exportedCard) {
        MonsterCard.getAllMonsterCards().remove(MonsterCard.getMonsterCardByName(exportedCard.getName()));
        new SimpleMonster(exportedCard.getName(), exportedCard.getDescription(), exportedCard.getPrice(), exportedCard.getLevel(), exportedCard.getDefence(), exportedCard.getAttack(), exportedCard.getMonsterType(), exportedCard.getMonsterAttributeType());
    }

    private static void handleRitualMonsterCard(ExportedCard exportedCard) {
        MonsterCard.getAllMonsterCards().remove(MonsterCard.getMonsterCardByName(exportedCard.getName()));
        new RitualMonster(exportedCard.getName(), exportedCard.getDescription(), exportedCard.getPrice(), exportedCard.getLevel(), exportedCard.getDefence(), exportedCard.getAttack(), exportedCard.getMonsterType(), exportedCard.getMonsterAttributeType());
    }

    private static void handleSpellImport(ExportedCard exportedCard) {
        SpellCard card = SpellCard.getAllSpellCardByName(exportedCard.getName());
        card.setDescription(exportedCard.getDescription());
        card.setPrice(exportedCard.getPrice());
        card.init();
    }

    private static void handleTrapImport(ExportedCard exportedCard) {
        TrapCard card = TrapCard.getAllTrapCardByName(exportedCard.getName());
        card.setDescription(exportedCard.getDescription());
        card.setPrice(exportedCard.getPrice());
        card.init();
    }
}
