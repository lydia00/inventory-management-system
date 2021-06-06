package view_controller;
/**
 * @author lydia00
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** Controller for the Modify Product form. */
public class ModifyProductController implements Initializable {
    public TextField modifyProductID;
    public TextField modifyProductName;
    public TextField modifyProductMax;
    public TextField modifyProductInv;
    public TextField modifyProductPrice;
    public TextField modifyProductMin;
    public TextField allPartsSearch;
    public TableView allPartsTable;
    public TableColumn allPartsIdCol;
    public TableColumn allPartsNameCol;
    public TableColumn allPartsInvCol;
    public TableColumn allPartsPriceCol;
    public Button modifyProductButton;
    public TableView associatedPartsTable;
    public TableColumn assocPartsIdCol;
    public TableColumn assocPartsNameCol;
    public TableColumn assocPartsInvCol;
    public TableColumn assocPartsPriceCol;
    public Button removeAssocPartButton;
    public Button modifyProductSave;
    public Button modifyProductCancel;
    public Label modifyProductMsg;

    private int selectedProductIndex;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /** Initializes the Modify Product screen and populates it with data from the selected product. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Product selectedProduct = MainScreenController.getSelectedProduct();
        selectedProductIndex = MainScreenController.getSelectedProductIndex();

        // Set text fields
        modifyProductID.setText(String.valueOf(selectedProduct.getProductId()));
        modifyProductName.setText(selectedProduct.getProductName());
        modifyProductInv.setText(String.valueOf(selectedProduct.getProductStock()));
        modifyProductPrice.setText(String.valueOf(selectedProduct.getProductPrice()));
        modifyProductMin.setText(String.valueOf(selectedProduct.getProductMin()));
        modifyProductMax.setText(String.valueOf(selectedProduct.getProductMax()));

        // Populate All Parts table
        allPartsTable.setItems(Inventory.getPartsList());
        allPartsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Populate Associated Parts table
        associatedPartsTable.setItems(selectedProduct.getAssociatedParts());
        assocPartsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /** Searches the All Parts table by Part Name or Part ID. Returns an error message if no parts are found. */
    public void allPartsSearchHandler(ActionEvent actionEvent) {
        String searchStr = allPartsSearch.getText();

        // Search by part name and if none found, search by part ID.
        ObservableList<Part> parts = searchByPartName(searchStr);

        if(parts.size() == 0) {
            try {
                int id = Integer.parseInt(searchStr);
                Part part = searchByPartId(id);
                if (part != null) {
                    parts.add(part);
                }
            }
            catch (NumberFormatException e) {
                // ignore exception
            }
        }
        allPartsTable.setItems(parts);

        // UI message indicating the search result
        if (parts.size() == 0) {
            modifyProductMsg.setText("No matching parts!");
        }
        else {
            modifyProductMsg.setText(parts.size() + " part(s) found.");
        }

        // Clear search field
        allPartsSearch.setText("");
    }

    /** Looks for a part name that matches the user's search term. Returns matching parts. */
    private ObservableList<Part> searchByPartName(String searchStr) {
        ObservableList<Part> allParts = Inventory.getPartsList();
        ObservableList<Part> foundParts = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if (part.getName().contains(searchStr)) {
                foundParts.add(part);
            }
        }
        return foundParts;
    }

    /** Looks for a part ID that matches the user's search term. Returns null if no parts are found. */
    private Part searchByPartId(int id) {
        ObservableList<Part> allParts = Inventory.getPartsList();
        for (Part part : allParts) {
            if (part.getId() == id) {
                return part;
            }
        }
        return null;
    }

    /** Adds the selected part to the Associated Parts table. */
    public void addAssocPartButtonHandler(ActionEvent actionEvent) {
        Part selectedPart = (Part) allPartsTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            modifyProductMsg.setText("No part selected!");
        }
        else {
            associatedParts.add(selectedPart);
            associatedPartsTable.setItems(associatedParts);
            associatedPartsTable.getItems();
        }
    }

    /** Removes the selected part from the Associated Parts table. */
    public void removeAssocPartButtonHandler(ActionEvent actionEvent) {
        Part selectedPart = (Part) allPartsTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            modifyProductMsg.setText("No part selected!");
        }
        else {
            associatedParts.remove(selectedPart);
            associatedPartsTable.setItems(associatedParts);
        }
    }

    /** Saves a modified product. Displays an error message if data is invalid. */
    public void modifyProductSaveHandler(ActionEvent actionEvent) {
        try {
            // Get data from the modified product
            int prodId = Integer.parseInt(modifyProductID.getText());
            String prodName = modifyProductName.getText();
            double prodPrice = Double.parseDouble(modifyProductPrice.getText());
            int prodInv = Integer.parseInt(modifyProductInv.getText());
            int prodMin = Integer.parseInt(modifyProductMin.getText());
            int prodMax = Integer.parseInt(modifyProductMax.getText());

            // Test if min, max, and inventory values are valid
            if (validateInventory(prodInv, prodMin, prodMax)) {

                // Save a modified product and notify the user
                Product product = new Product(prodId, prodName, prodPrice, prodInv, prodMin, prodMax);

                product.setProductId(prodId);
                product.setProductPrice(prodPrice);
                product.setProductStock(prodInv);
                product.setProductMin(prodMin);
                product.setProductMax(prodMax);
                product.setAssociatedParts(associatedParts);
                Inventory.getProductsList().set(selectedProductIndex, product);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Product Saved!");
                alert.setContentText("Product successfully modified and saved to inventory.");
                alert.showAndWait();
                toMainScreen(actionEvent);
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
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Product Not Saved");
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
    public void modifyProductCancelHandler(ActionEvent actionEvent) throws IOException {
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
