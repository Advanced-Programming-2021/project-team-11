package view.components;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class AlertsUtil {
    public static void showError(Throwable ex) {
        showError(ex.getMessage());
    }

    public static void showError(String message) {
        showAlert(message, "Error", Alert.AlertType.ERROR);
    }

    public static void showSuccess(String message) {
        showAlert(message, "Success", Alert.AlertType.INFORMATION);
    }

    public static void showSuccess(String message, Runnable afterClose) {
        showAlert(message, "Success", Alert.AlertType.INFORMATION, afterClose);
    }

    public static void showHelp(String message) {
        showAlert(message, "Help", Alert.AlertType.INFORMATION);
    }

    public static void showHelp(String message, Runnable afterClose) {
        showAlert(message, "Help", Alert.AlertType.INFORMATION, afterClose);
    }

    public static void showAlert(String message, String title, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showAlert(String message, String title, Alert.AlertType type, Runnable afterClose) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        if (afterClose != null)
            alert.setOnCloseRequest(x -> afterClose.run());
        alert.show();
    }

    public static boolean confirmAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> clickedButton = alert.showAndWait();
        return clickedButton.isPresent() && clickedButton.get().getButtonData() == ButtonBar.ButtonData.OK_DONE;
    }

    public static void confirmAlert(String message, DialogResultCallback after) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setOnCloseRequest(event -> after.callback(alert.getResult().getButtonData()));
        alert.show();
    }

    /**
     * Shows an {@link TextInputDialog} with a header
     *
     * @param header The header of dialog
     * @return The text which user entered; Returns null if dialog has been canceled
     */
    public static String getTextAlert(String header) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(header);
        Optional<String> text = dialog.showAndWait();
        return text.orElse(null);
    }

    /**
     * Creates a dialog with specified buttons
     * It uses showAndWait
     *
     * @param message Alert message
     * @param title   Alert title
     * @param buttons The button texts
     * @return The index of selected button. Returns -1 if the dialog is dismissed
     */
    public static int customButtonAlert(String message, String title, String... buttons) {
        ButtonType[] buttonTypes = new ButtonType[buttons.length];
        for (int i = 0; i < buttonTypes.length; i++)
            buttonTypes[i] = new ButtonType(buttons[i], ButtonBar.ButtonData.values()[i]);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, buttonTypes);
        alert.setContentText(title);
        Optional<ButtonType> result = alert.showAndWait();
        return result.map(buttonType -> buttonType.getButtonData().ordinal()).orElse(-1);
    }
}
