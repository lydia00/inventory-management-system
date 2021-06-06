package view_controller;
/**
 * @author lydia00
 */
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHousePart;
import model.Inventory;
import model.OutsourcedPart;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** Controller for the Modify Part form. */
public class ModifyPartController implements Initializable {
    public RadioButton inHouseToggle;
    public ToggleGroup modifyPartGroup;
    public RadioButton outsourcedToggle;
    public TextField modifyPartID;
    public TextField modifyPartName;
    public TextField modifyPartInv;
    public TextField modifyPartPrice;
    public TextField modifyPartMax;
    public TextField modifyPartOrigin;
    public TextField modifyPartMin;
    public Button modifyPartSaveButton;
    public Button modifyPartCancelButton;
    public Label dynamicFieldLabel;

    private boolean isInHouse;
    private int selectedPartIndex;

    /** Initializes the Modify Part form and populates it with data from the selected part. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Part selectedPart = MainScreenController.getSelectedPart();
        selectedPartIndex = MainScreenController.getSelectedPartIndex();

        if (selectedPart instanceof InHousePart) {
            inHouseToggle.setSelected(true);
            dynamicFieldLabel.setText("Machine ID");
            modifyPartID.setText(String.valueOf(selectedPart.getId()));
            modifyPartName.setText(selectedPart.getName());
            modifyPartInv.setText(String.valueOf(selectedPart.getStock()));
            modifyPartPrice.setText(String.valueOf(selectedPart.getPrice()));
            modifyPartMin.setText(String.valueOf(selectedPart.getMin()));
            modifyPartMax.setText(String.valueOf(selectedPart.getMax()));
            modifyPartOrigin.setText(String.valueOf(((InHousePart) selectedPart).getMachineId()));
        }
        else if (selectedPart instanceof OutsourcedPart) {
            outsourcedToggle.setSelected(true);
            dynamicFieldLabel.setText("Company Name");
            modifyPartID.setText(String.valueOf(selectedPart.getId()));
            modifyPartName.setText(selectedPart.getName());
            modifyPartInv.setText(String.valueOf(selectedPart.getStock()));
            modifyPartPrice.setText(String.valueOf(selectedPart.getPrice()));
            modifyPartMin.setText(String.valueOf(selectedPart.getMin()));
            modifyPartMax.setText(String.valueOf(selectedPart.getMax()));
            modifyPartOrigin.setText(String.valueOf(((OutsourcedPart) selectedPart).getCompanyName()));
        }

    }

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

    /** Saves the modified part. Displays an error if data is invalid. */
    public void modifyPartSaveHandler(ActionEvent actionEvent) {
        try {
            // Get data from the modified part
            int partId = Integer.parseInt(modifyPartID.getText());
            String partName = modifyPartName.getText();
            double partPrice = Double.parseDouble(modifyPartPrice.getText());
            int partInv = Integer.parseInt(modifyPartInv.getText());
            int partMin = Integer.parseInt(modifyPartMin.getText());
            int partMax = Integer.parseInt(modifyPartMax.getText());

            // Test if min, max, and inventory values are valid
            if (validateInventory(partInv, partMin, partMax)) {

                // Save a modified in-house part. Display notification and return user to main screen.
                if (isInHouse) {
                    try {
                        int partMachineId = Integer.parseInt(modifyPartOrigin.getText());
                        InHousePart inHousePart = new InHousePart(partId, partName, partPrice, partInv, partMin, partMax, partMachineId);
                        Inventory.getPartsList().set(selectedPartIndex, inHousePart);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText("Part Saved!");
                        alert.setContentText("Part successfully modified and saved to inventory.");
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

                // Save a modified outsourced part. If successful, display confirmation and return user to main screen.
                if (!isInHouse) {
                    String partCompanyName = modifyPartOrigin.getText();
                    OutsourcedPart outsourcedPart = new OutsourcedPart(partId, partName, partPrice, partInv, partMin, partMax, partCompanyName);
                    Inventory.getPartsList().set(selectedPartIndex, outsourcedPart);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Part Saved!");
                    alert.setContentText("Part successfully modified and saved to inventory.");
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
    public void modifyPartCancelHandler(ActionEvent actionEvent) throws IOException {
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
