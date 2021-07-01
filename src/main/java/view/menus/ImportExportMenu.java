package view.menus;

import com.google.gson.JsonSyntaxException;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXScrollPane;
import controller.menucontrollers.ImportExportMenuController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import model.cards.Card;
import model.cards.export.ExportedCard;
import model.exceptions.InvalidCardToImportException;
import view.components.AlertsUtil;
import view.components.CardViewImportExport;
import view.components.UserBadge;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ImportExportMenu implements Initializable {
    private final ArrayList<CardViewImportExport> cards = new ArrayList<>();
    @FXML
    private UserBadge userBadge;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private JFXMasonryPane masonryPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Card.getAllCards().forEach(card -> cards.add(new CardViewImportExport(card, this::clickedJsonExport, this::clickedCsvExport)));
        masonryPane.getChildren().addAll(cards);
        Platform.runLater(() -> scrollPane.requestLayout());
        JFXScrollPane.smoothScrolling(scrollPane);
        userBadge.setUser(MainMenu.loggedInUser);
    }

    private void clickedCsvExport(CardViewImportExport cardViewImportExport) {
        Card card = cardViewImportExport.getCard();
        saveCard(card, false);
    }

    private void clickedJsonExport(CardViewImportExport cardViewImportExport) {
        Card card = cardViewImportExport.getCard();
        saveCard(card, true);
    }

    private void saveCard(Card card, boolean isJson) {
        String cardName = card.getName();
        String fileContent = isJson ? ExportedCard.cardToJson(card) : ExportedCard.cardToCsv(card);
        try {
            String filename = cardName + (isJson ? ".json" : ".csv");
            ImportExportMenuController.writeFile(filename, fileContent);
            AlertsUtil.showSuccess("Saved in " + filename);
        } catch (IOException ex) {
            AlertsUtil.showError("Cannot write file: " + ex.getMessage());
        }
    }

    public void clickedBackButton(MouseEvent mouseEvent) {
        SceneChanger.changeScene(MenuNames.MAIN);
    }

    public void clickedImportButton(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select The File To Import");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        File file = fileChooser.showOpenDialog(RootMenu.primaryStage);
        if (file != null)
            importCardFinal(file.getAbsolutePath());
    }

    private void importCardFinal(String filename) {
        try {
            String file = ImportExportMenuController.readFile(filename);
            ExportedCard card = filename.endsWith(".json") ? ExportedCard.jsonToExportedCard(file) : ExportedCard.csvToExportedCard(file);
            ImportExportMenuController.handleImport(card);
            AlertsUtil.showSuccess("Card imported!");
        } catch (JsonSyntaxException ex) {
            AlertsUtil.showError("Cannot parse the json file: " + ex.getMessage());
        } catch (InvalidCardToImportException | IOException ex) {
            AlertsUtil.showError(ex);
        }
    }
}
