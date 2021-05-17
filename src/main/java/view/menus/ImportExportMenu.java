package view.menus;

import com.google.gson.JsonSyntaxException;
import controller.menucontrollers.ImportExportMenuController;
import model.cards.Card;
import model.cards.export.ExportedCard;
import model.exceptions.InvalidCardToImportException;
import model.exceptions.InvalidCommandException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImportExportMenu extends Menu {
    private static final String EXPORT_PREFIX = "export card ", IMPORT_PREFIX = "import card ";

    ImportExportMenu() {
        openMenu();
    }

    @Override
    void openMenu() {
        while (true) {
            String command = MenuUtils.readLine();
            try {
                if (processMenuCommands(command))
                    return;
                continue;
            } catch (InvalidCommandException ignored) {
            }
            if (importCard(command) || exportCard(command))
                continue;
            System.out.println(MenuUtils.INVALID_COMMAND);
        }
    }

    private boolean importCard(String command) {
        if (!command.startsWith(IMPORT_PREFIX))
            return false;
        // Read the file
        String filecontent;
        try {
            StringBuilder fileLines = new StringBuilder();
            String filename = command.substring(IMPORT_PREFIX.length()) + ".json";
            Files.readAllLines(Paths.get(filename)).forEach(line -> fileLines.append(line).append('\n'));
            filecontent = fileLines.toString();
        } catch (IOException ex) {
            System.out.println("Cannot read the file: " + ex.getMessage());
            return true;
        }
        try {
            ExportedCard card = ExportedCard.jsonToExportedCard(filecontent);
            ImportExportMenuController.handleImport(card);
            System.out.println(card.getName() + " imported!");
        } catch (JsonSyntaxException ex) {
            System.out.println("Cannot parse the json file: " + ex.getMessage());
        } catch (InvalidCardToImportException ex) {
            System.out.println(ex.getMessage());
        }
        return true;
    }

    private boolean exportCard(String command) {
        if (!command.startsWith(EXPORT_PREFIX))
            return false;
        String cardName = command.substring(EXPORT_PREFIX.length());
        Card card = Card.getCardByName(cardName);
        if (card == null) {
            System.out.println("Card does not exists!");
            return true;
        }
        String json = ExportedCard.cardToJson(card);
        try {
            String filename = cardName + ".json";
            FileWriter writer = new FileWriter(filename);
            writer.append(json);
            writer.close();
            System.out.println("Saved in " + filename);
        } catch (IOException ex) {
            System.out.println("Cannot write file: " + ex.getMessage());
        }
        return true;
    }

    @Override
    void enterMenu(MenuNames menu) {
        System.out.println(MenuUtils.MENU_NAV_FAILED);
    }

    @Override
    void printMenu() {
        System.out.println("Import/Export Menu");
    }
}
