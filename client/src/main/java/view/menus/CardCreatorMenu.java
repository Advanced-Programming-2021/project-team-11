package view.menus;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.CardCreatorController;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import model.cards.MonsterAttributeType;
import model.cards.MonsterType;
import model.cards.RitualMonster;
import model.cards.SimpleMonster;
import view.components.AlertsUtil;
import view.global.Assets;

import java.net.URL;
import java.util.ResourceBundle;

public class CardCreatorMenu implements Initializable {
    private static final String INVALID_PRICE = "N/A";
    public BorderPane rootPane;
    public JFXTextField nameField;
    public JFXTextField attackPoints;
    public JFXTextField defensePoints;
    public JFXComboBox<Integer> levelCombo;
    public JFXCheckBox ritualMonster;
    public Text estimatePrice;
    public JFXComboBox<MonsterType> monsterTypeCombo;
    public JFXComboBox<MonsterAttributeType> monsterAttributeTypeCombo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Assets.setMenuBackgroundImage(rootPane);
        setupComboBoxes();
        setupListeners();
        updatePrice();
    }

    private void setupComboBoxes() {
        // Level
        for (int i = 1; i <= 8; i++)
            levelCombo.getItems().add(i);
        levelCombo.getSelectionModel().selectFirst();
        // Monster types
        monsterTypeCombo.getItems().addAll(MonsterType.values());
        monsterTypeCombo.getSelectionModel().selectFirst();
        monsterAttributeTypeCombo.getItems().addAll(MonsterAttributeType.values());
        monsterAttributeTypeCombo.getSelectionModel().selectFirst();
    }

    private void setupListeners() {
        attackPoints.textProperty().addListener((observable, oldValue, newValue) -> updatePrice());
        defensePoints.textProperty().addListener((observable, oldValue, newValue) -> updatePrice());
        levelCombo.valueProperty().addListener((observable, oldValue, newValue) -> updatePrice());
        ritualMonster.selectedProperty().addListener((observable, oldValue, newValue) -> updatePrice());
    }

    private void updatePrice() {
        String text = "Price of this card: ";
        int price = CardCreatorController.predictPrice(attackPoints.getText(), defensePoints.getText(), levelCombo.getValue(), ritualMonster.isSelected());
        if (price == -1)
            text += INVALID_PRICE;
        else
            text += price;
        estimatePrice.setText(text);
    }

    public void clickedCreate(MouseEvent mouseEvent) {
        int price = CardCreatorController.predictPrice(attackPoints.getText(), defensePoints.getText(), levelCombo.getValue(), ritualMonster.isSelected());
        if (price == -1) {
            AlertsUtil.showError("Invalid values!");
            return;
        }
        // Check player's balance
        int toReducePrice = price / 10;
        if (MainMenu.loggedInUser.getMoney() < toReducePrice) {
            AlertsUtil.showError("You don't have enough money to create this card!");
            return;
        }
        MainMenu.loggedInUser.decreaseMoney(toReducePrice);
        // Create the card
        if (ritualMonster.isSelected())
            new RitualMonster(nameField.getText(), "", price, levelCombo.getValue(), Integer.parseInt(defensePoints.getText()), Integer.parseInt(attackPoints.getText()), monsterTypeCombo.getValue(), monsterAttributeTypeCombo.getValue());
        else
            new SimpleMonster(nameField.getText(), "", price, levelCombo.getValue(), Integer.parseInt(defensePoints.getText()), Integer.parseInt(attackPoints.getText()), monsterTypeCombo.getValue(), monsterAttributeTypeCombo.getValue());
        AlertsUtil.showSuccess("Card created!");
        SceneChanger.changeScene(MenuNames.IMPORT_EXPORT);
    }

    public void clickedBack(MouseEvent mouseEvent) {
        SceneChanger.changeScene(MenuNames.IMPORT_EXPORT);
    }
}
