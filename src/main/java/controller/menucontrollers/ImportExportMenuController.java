package controller.menucontrollers;

import model.cards.*;
import model.cards.export.ExportedCard;
import model.exceptions.InvalidCardToImportException;

public class ImportExportMenuController {
    public static void handleImport(ExportedCard exportedCard) throws InvalidCardToImportException {
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
            default:
                throw new InvalidCardToImportException();
        }
    }

    private static void handleMonsterImport(ExportedCard exportedCard) throws InvalidCardToImportException {
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
            default:
                throw new InvalidCardToImportException("Monster card type is not valid");
        }
    }

    private static void handleEffectMonsterCard(ExportedCard exportedCard) throws InvalidCardToImportException {
        MonsterCard card = MonsterCard.getAllMonsterCardByName(exportedCard.getName());
        if (card == null)
            throw new InvalidCardToImportException("card does not exists!");
        card.setDescription(exportedCard.getDescription());
        card.setPrice(exportedCard.getPrice());
        card.setAttack(exportedCard.getAttack());
        card.setDefence(exportedCard.getDefence());
        card.setLevel(exportedCard.getLevel());
        card.setMonsterType(exportedCard.getMonsterType());
        card.setMonsterAttributeType(exportedCard.getMonsterAttributeType());
        card.init();
    }

    private static void handleNormalMonsterCard(ExportedCard exportedCard) {
        MonsterCard.getAllMonsterCards().remove(MonsterCard.getMonsterCardByName(exportedCard.getName()));
        new SimpleMonster(exportedCard.getName(), exportedCard.getDescription(), exportedCard.getPrice(), exportedCard.getLevel(),
                exportedCard.getDefence(), exportedCard.getAttack(), exportedCard.getMonsterType(), exportedCard.getMonsterAttributeType());
    }

    private static void handleRitualMonsterCard(ExportedCard exportedCard) {
        MonsterCard.getAllMonsterCards().remove(MonsterCard.getMonsterCardByName(exportedCard.getName()));
        new RitualMonster(exportedCard.getName(), exportedCard.getDescription(), exportedCard.getPrice(), exportedCard.getLevel(),
                exportedCard.getDefence(), exportedCard.getAttack(), exportedCard.getMonsterType(), exportedCard.getMonsterAttributeType());
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