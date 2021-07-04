package view.menus;

import controller.menucontrollers.DeckMenuController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import model.exceptions.DeckDoesNotExistsException;
import model.exceptions.DeckExistsException;
import model.results.DeckListTableResult;
import view.components.AlertsUtil;
import view.components.TableButton;

import java.net.URL;
import java.util.ResourceBundle;

public class DeckMenu implements Initializable {
    public VBox tableContainer;
    private boolean initialized = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
    }

    private void setupTable() {
        if (initialized) // remove old table
            tableContainer.getChildren().remove(0);
        initialized = true;
        // Get the decks
        TableView<DeckListTableResult> table = new TableView<>();
        ObservableList<DeckListTableResult> decks = MainMenu.loggedInUser.getDecksForTable();
        // Setup columns
        TableColumn<DeckListTableResult, String> deckNameColumn = new TableColumn<>("Deck Name");
        deckNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        deckNameColumn.setPrefWidth(160);
        TableColumn<DeckListTableResult, Integer> mainDeckSizeColumn = new TableColumn<>("Main Deck Size");
        mainDeckSizeColumn.setCellValueFactory(new PropertyValueFactory<>("mainDeckSize"));
        mainDeckSizeColumn.setPrefWidth(160);
        TableColumn<DeckListTableResult, Integer> sideDeckSizeColumn = new TableColumn<>("Side Deck Size");
        sideDeckSizeColumn.setCellValueFactory(new PropertyValueFactory<>("sideDeckSize"));
        sideDeckSizeColumn.setPrefWidth(160);
        TableColumn<DeckListTableResult, String> deleteColumn = new TableColumn<>("Delete");
        deleteColumn.setPrefWidth(50);
        deleteColumn.setCellValueFactory(cellData -> new SimpleStringProperty(""));
        deleteColumn.setCellFactory(TableButton.createTableButton("❌", this::handleRemoveDeck, TableButton.Color.RED));
        TableColumn<DeckListTableResult, String> activateColumn = new TableColumn<>("Activate");
        activateColumn.setPrefWidth(70);
        activateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(""));
        activateColumn.setCellFactory(TableButton.createTableButton("✅", this::handleActiveDeck, TableButton.Color.GREEN));
        TableColumn<DeckListTableResult, String> editColumn = new TableColumn<>("Edit");
        editColumn.setPrefWidth(50);
        editColumn.setCellValueFactory(cellData -> new SimpleStringProperty(""));
        editColumn.setCellFactory(TableButton.createTableButton("✏", this::handleEditDeck, TableButton.Color.BLUE));
        TableColumn<DeckListTableResult, Boolean> validColumn = new TableColumn<>("Valid");
        validColumn.setPrefWidth(50);
        validColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isValid()));
        validColumn.setCellFactory(col -> new TableCell<DeckListTableResult, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setAlignment(Pos.CENTER);
                setText(empty ? null : (item ? "✅" : "❌"));
            }
        });
        TableColumn<DeckListTableResult, String> sneakPeakColumn = new TableColumn<>("Sneak Peak");
        sneakPeakColumn.setPrefWidth(100);
        sneakPeakColumn.setCellValueFactory(cellData -> new SimpleStringProperty(""));
        sneakPeakColumn.setCellFactory(TableButton.createTableTooltipForDeck("👁"));
        // Setup the table
        table.setRowFactory(tv -> new TableRow<DeckListTableResult>() {
            @Override
            public void updateItem(DeckListTableResult item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && item.isActive())
                    setStyle(getStyle() + "; -fx-background-color: #81C784;");
            }
        });
        table.setItems(decks);
        table.setSelectionModel(null);
        table.getColumns().add(deckNameColumn);
        table.getColumns().add(mainDeckSizeColumn);
        table.getColumns().add(sideDeckSizeColumn);
        table.getColumns().add(deleteColumn);
        table.getColumns().add(activateColumn);
        table.getColumns().add(editColumn);
        table.getColumns().add(validColumn);
        table.getColumns().add(sneakPeakColumn);
        // Show it
        table.setPrefSize(600, 600);
        tableContainer.getChildren().add(0, table);
    }

    private void handleRemoveDeck(DeckListTableResult deck) {
        try {
            DeckMenuController.deleteDeck(MainMenu.loggedInUser, deck.getName());
        } catch (DeckDoesNotExistsException ignored) {
        }
        setupTable();
    }

    private void handleActiveDeck(DeckListTableResult deck) {
        try {
            DeckMenuController.setActiveDeck(MainMenu.loggedInUser, deck.getName());
        } catch (DeckDoesNotExistsException ignored) {
        }
        setupTable();
    }

    private void handleEditDeck(DeckListTableResult deck) {
        DeckDetailsMenu.toEditDeck = MainMenu.loggedInUser.getDecks().get(deck.getName());
        DeckDetailsMenu.toEditDeckName = deck.getName();
        SceneChanger.changeScene(MenuNames.DECK_DETAILS);
    }

    public void clickedBackButton(MouseEvent mouseEvent) {
        SceneChanger.changeScene(MenuNames.MAIN);
    }

    public void clickedNewDeckButton(MouseEvent mouseEvent) {
        String deckName = AlertsUtil.getTextAlert("Enter your deck name");
        try {
            DeckMenuController.addDeck(MainMenu.loggedInUser, deckName);
            setupTable();
            AlertsUtil.showSuccess("Deck added!");
        } catch (DeckExistsException ex) {
            AlertsUtil.showError(ex);
        }
    }
}
