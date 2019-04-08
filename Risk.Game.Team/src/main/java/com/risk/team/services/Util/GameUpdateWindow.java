package com.risk.team.services.Util;

import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

import com.risk.team.model.Continent;
import com.risk.team.model.Country;

/**
 * This class provides all the game utilities as to exit window,enable and disable button.
 *
 * @author Dhaval Desai
 */

public class GameUpdateWindow {

    /**
     * This method is used to close the window.
     *
     * @param button button
     */
    public static void exitWindow(Button button) {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

    /**
     * This method is used to disable a complete pane.
     *
     * @param pane window
     */
    public static void disablePane(Pane pane) {
        pane.setVisible(false);
    }

    /**
     * This method is used to enable a complete pane.
     *
     * @param pane window
     */
    public static void enablePane(Pane pane) {
        pane.setVisible(true);
    }

    /**
     * This method provides a warning window with a message.
     *
     * @param head Head of the window
     * @param title Title of the window
     * @param information warning message
     */
    public static void popUpWindow(String head, String title, String information) {
        Alert popUp = new Alert(AlertType.INFORMATION);
        popUp.setHeaderText(head);
        popUp.setTitle(title);
        popUp.setContentText(information);
        popUp.showAndWait();
    }

    /**
     * This method helps to hide a button in the pane.
     *
     * @param controls Any number of inputs
     */
    public static void hideButtonControl(Control... controls) {
        for (Control control : controls) {
            control.setVisible(false);
        }
    }

    /**
     * This method helps to disable button in the pane.
     *
     * @param controls Any number of inputs
     */
    public static void disableButtonControl(Control... controls) {
        for (Control control : controls) {
            control.setDisable(true);
        }
    }

    /**
     * This method helps to enable button in the pane.
     *
     * @param controls Any number of inputs
     */
    public static void enableButtonControl(Control... controls) {
        for (Control control : controls) {
            control.setDisable(false);
        }
    }

    /**
     * Method for making checkbox visible.
     *
     * @param controls any number of checkboxes
     */

    public static void showCheckBox(Control... controls) {
        for (Control control : controls) {
            control.setVisible(true);
        }
    }

    /**
     * This method helps to create a new pane to include the continents
     * and its countries and show the ownership of that particular country.
     *
     * @param continent Continent Object
     * @return pane Window
     */
    public static TitledPane createNewPane(Continent continent) {
        VBox hbox = new VBox();
        for (Country country : continent.getListOfCountries()) {
            Label label1 = new Label();
            if (country.getPlayer() != null) {
                label1.setText(
                        country.getName() + ":-" + country.getNoOfArmies() + "-" + country.getPlayer().getName());
            } else {
                label1.setText(country.getName() + ":-" + country.getNoOfArmies());
            }
            hbox.getChildren().add(label1);
        }
        TitledPane pane = new TitledPane(continent.getName(), hbox);

        return pane;
    }

    /**
     * This method helps to select a map file by providing its extension
     *
     * @return selectedFile Selected file
     */
    public static File showFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a Map File");
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Map File Extensions (*.map or *.MAP)", "*.map", "*.MAP"));
        File selectedFile = fileChooser.showOpenDialog(null);
        return selectedFile;
    }

    /**
     * This method helps to provide checkboxes using Java FXML
     *
     * @param checkBoxes checkBoxes
     */
    public static void unCheckBoxes(CheckBox... checkBoxes) {
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setText("");
            checkBox.setSelected(false);
        }
    }

    /**
     * This method helps to provide checkboxes using Java FXML
     * 
     * @param checkBoxes checkBoxes
     */
    public static void checkCheckBoxes(CheckBox... checkBoxes) {
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setText("");
            checkBox.setIndeterminate(false);
            checkBox.setSelected(true);
        }
    }

    /**
     * Method to select dice
     * 
     * @param controls any number of controls
     */
    public static void selectVisibleDice(Control... controls) {
        for (Control control : controls) {
            if (control.isVisible()) {
                ((CheckBox) control).setSelected(true);
            }
        }
    }

    /**
     * This method helps in taking user input by providing a input box
     *
     * @return numberOfArmies The value provided by the user
     */
    public static String userInput() {
        TextInputDialog inputBox = new TextInputDialog();
        String numberOfArmies = "0";
        inputBox.setTitle("User Input");
        inputBox.setHeaderText("Please enter number of armies.");
        Optional<String> inputFromUser = inputBox.showAndWait();
        if (inputFromUser.isPresent()) {
            numberOfArmies = inputFromUser.get();
        }
        return numberOfArmies;
    }
}
