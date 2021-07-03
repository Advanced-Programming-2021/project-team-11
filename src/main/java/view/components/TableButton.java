package view.components;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import model.results.DeckListTableResult;
import view.menus.SceneChanger;

public class TableButton {
    public interface TableButtonCallback<T> {
        void callback(T object);
    }

    public enum Color {
        GREEN("jfx-button-buy-ok"),
        RED("jfx-button-buy-not-ok"),
        BLUE("jfx-button-raised"),
        FLAT("jfx-button-flat");
        private final String styleName;

        Color(String styleName) {
            this.styleName = styleName;
        }

        public String getStyleName() {
            return styleName;
        }
    }

    public static <T> Callback<TableColumn<T, String>, TableCell<T, String>> createTableButton(String text, TableButtonCallback<T> callback, Color color) {
        return createTableButton(text, callback, color, "");
    }

    public static Callback<TableColumn<DeckListTableResult, String>, TableCell<DeckListTableResult, String>> createTableTooltipForDeck(String text) {
        return new Callback<TableColumn<DeckListTableResult, String>, TableCell<DeckListTableResult, String>>() {
            @Override
            public TableCell<DeckListTableResult, String> call(TableColumn<DeckListTableResult, String> param) {
                return new TableCell<DeckListTableResult, String>() {
                    final JFXButton btn = new JFXButton(text);

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            btn.getStyleClass().add(Color.FLAT.styleName);
                            btn.setTooltip(new Tooltip(getTableView().getItems().get(getIndex()).getDeckSummery()));
                            setGraphic(centered(btn));
                        }
                        setText(null);
                    }
                };
            }
        };
    }

    /**
     * Creates a button in table
     * https://stackoverflow.com/a/29490190/4213397
     *
     * @param text     The text of button
     * @param callback Callback of button
     * @param color    The color of button
     * @param tooltip  Tooltip of the button
     * @param <T>      Type of table data
     * @return A callback which must be set with setCellFactory
     */
    public static <T> Callback<TableColumn<T, String>, TableCell<T, String>> createTableButton(String text, TableButtonCallback<T> callback, Color color, String tooltip) {
        return new Callback<TableColumn<T, String>, TableCell<T, String>>() {
            @Override
            public TableCell<T, String> call(TableColumn<T, String> param) {
                return new TableCell<T, String>() {
                    final JFXButton btn = new JFXButton(text);

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            if (callback != null)
                                btn.setOnAction(event -> callback.callback(getTableView().getItems().get(getIndex())));
                            btn.getStyleClass().add(color.getStyleName());
                            btn.setOnMouseEntered(x -> SceneChanger.getScene().setCursor(Cursor.HAND));
                            btn.setOnMouseExited(x -> SceneChanger.getScene().setCursor(Cursor.DEFAULT));
                            if (tooltip != null && !tooltip.equals(""))
                                btn.setTooltip(new Tooltip(tooltip));
                            setGraphic(centered(btn));
                        }
                        setText(null);
                    }
                };
            }
        };
    }

    private static HBox centered(Node node) {
        HBox hBox = new HBox();
        hBox.getChildren().add(node);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }
}
