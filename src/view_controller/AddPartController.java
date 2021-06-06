package view_controller;
/**
 * @author lydia00
 */
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHousePart;
import model.Inventory;
import model.OutsourcedPart;

import java.io.IOException;

/** Controller for the Add Part form. */
public class AddPartController {
    public RadioButton inHouseToggle;
    public ToggleGroup addPartGroup;
    public RadioButton outsourcedToggle;
    public TextField addPartID;
    public TextField addPartName;
    public TextField addPartInv;
    public TextField addPartPrice;
    public TextField addPartMax;
    public TextField addPartMin;
    public TextField addPartOrigin;
    public Button addPartSaveButton;
    public Button addPartCancelButton;
    public Label dynamicFieldLabel;
    private boolean isInHouse = true;

    /** Displays the Machine ID field label when In-House is selected. */
    public void inHouseToggleHandler(ActionEvent actionEvent) {
        dynamicFieldLabel.setText("Machine ID");
        isInHouse = true;
    }

    /** Displays the Company Name field label when Outsourced is selected. */
    public void outsourcedToggleHandler(ActionEvent actionEvent) {
        dynamicFieldLabel.setText("Company Name");
        isInHouse = false;
    }

    /** Adds a new in-house or outsourced part. Displays an error message if data is invalid. */
    public void addPartSaveHandler(ActionEvent actionEvent) throws IOException {
        try {
            // Get user input
            int partId = 0;
            String partName = addPartName.getText();
            double partPrice = Double.parseDouble(addPartPrice.getText());
            int partInv = Integer.parseInt(addPartInv.getText());
            int partMin = Integer.parseInt(addPartMin.getText());
            int partMax = Integer.parseInt(addPartMax.getText());

            // Test if min, max, and inventory values are valid
            if (validateInventory(partInv, partMin, partMax)) {

                // Add an in-house part. If successful, display confirmation and return user to main screen.
                if (isInHouse) {
                    try {
                        int partMachineId = Integer.parseInt(addPartOrigin.getText());
                        InHousePart inHousePart = new InHousePart(partId, partName, partPrice, partInv, partMin, partMax, partMachineId);
                        inHousePart.setId(Inventory.getIncPartId());
                        Inventory.addPart(inHousePart);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText("Part Added!");
                        alert.setContentText("New part successfully added to inventory.");
                        alert.showAndWait();
                        toMainScreen(actionEvent);
                    }

                    // Display an alert if the machine ID is invalid.
                    catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Invalid Input");
                        alert.setHeaderText("Invalid Machine ID");
                        alert.setContentText("Machine ID must contain only numbers.");
                        alert.showAndWait();
                    }
                }

                // Add an outsourced part. If successful, display confirmation and return user to main screen.
                if (!isInHouse) {
                    String partCompanyName = addPartOrigin.getText();
                    OutsourcedPart outsourcedPart = new OutsourcedPart(partId, partName, partPrice, partInv, partMin, partMax, partCompanyName);
                    outsourcedPart.setId(Inventory.getIncPartId());
                    Inventory.addPart(outsourcedPart);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Part Added!");
                    alert.setContentText("New part successfully added to inventory.");
                    alert.showAndWait();
                    toMainScreen(actionEvent);
                }
            }

            // Display an error if Inventory, Min, or Max is invalid
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText("Check Inventory Values");
                alert.setContentText("Min must be less than Max and Inventory must be inside the range of Min and Max.");
                alert.showAndWait();
            }
        }

        // Display an error for invalid data
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Part Not Saved");
            alert.setContentText("Make sure data is valid and all fields are complete.");
            alert.showAndWait();
        }
    }

    /** Validates Inventory, Min, and Max values. */
    private boolean validateInventory(int inv, int min, int max) {
        boolean isValidStock = true;

        if (min > max) {
            isValidStock = false;
        }
        else if (inv < min || inv > max) {
            isValidStock = false;
        }
        return isValidStock;
    }

    /** Returns to the main screen after the user clicks Cancel. */
    public void addPartCancelHandler(ActionEvent actionEvent) throws IOException {
        toMainScreen(actionEvent);
    }

    /** Function to send the user back to the main screen. */
    public void toMainScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view_controller/MainScreen.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }
}

