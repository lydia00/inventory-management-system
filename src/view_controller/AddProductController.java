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

/** Controller for the Add Product form. */
public class AddProductController implements Initializable {
    public TextField addProductID;
    public TextField addProductName;
    public TextField addProductMax;
    public TextField addProductInv;
    public TextField addProductPrice;
    public TextField addProductMin;
    public TextField allPartsSearch;
    public TableView allPartsTable;
    public TableColumn allPartsIdCol;
    public TableColumn allPartsNameCol;
    public TableColumn allPartsInvCol;
    public TableColumn allPartsPriceCol;
    public Button addProductButton;
    public TableView associatedPartsTable;
    public TableColumn assocPartsIdCol;
    public TableColumn assocPartsNameCol;
    public TableColumn assocPartsInvCol;
    public TableColumn assocPartsPriceCol;
    public Button removeAssocPartButton;
    public Button addProductSave;
    public Button addProductCancel;
    public Label addProductMsg;

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /** Initializes the Add Product screen and populates the All Parts table. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Product product = new Product(0,"",0,0,0,0);

        allPartsTable.setItems(Inventory.getPartsList());
        allPartsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartsTable.setItems(product.getAssociatedParts());
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
            addProductMsg.setText("No matching parts!");
        }
        else {
            addProductMsg.setText(parts.size() + " part(s) found.");
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
            addProductMsg.setText("No part selected!");
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
            addProductMsg.setText("No part selected!");
        }
        else {
            associatedParts.remove(selectedPart);
            associatedPartsTable.setItems(associatedParts);
            associatedPartsTable.getItems();
        }
    }

    /** Adds a new product. Displays an error message if data is invalid. */
    public void addProductSaveHandler(ActionEvent actionEvent) {
        try {
            // Get user input
            int productId = 0;
            String productName = addProductName.getText();
            double productPrice = Double.parseDouble(addProductPrice.getText());
            int productInv = Integer.parseInt(addProductInv.getText());
            int productMin = Integer.parseInt(addProductMin.getText());
            int productMax = Integer.parseInt(addProductMax.getText());

            // If fields are valid, create a new product
            if (validateInventory(productInv, productMin, productMax)) {
                Product product = new Product(productId, productName, productPrice, productInv, productMin, productMax);

                product.setProductId(Inventory.getIncProdId());
                product.setProductPrice(productPrice);
                product.setProductStock(productInv);
                product.setProductMin(productMin);
                product.setProductMax(productMax);
                product.setAssociatedParts(associatedParts);
                Inventory.addProduct(product);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Product Added!");
                alert.setContentText("New product successfully added to inventory.");
                alert.showAndWait();
                toMainScreen(actionEvent);
            }

            // Display alert if inventory, min, or max are invalid
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText("Check Inventory Values");
                alert.setContentText("Min must be less than Max and Inventory must be inside the range of Min and Max.");
                alert.showAndWait();
            }
        }

        // Display an alert if fields are invalid
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

    /** Returns user to the main screen when the user clicks Cancel. */
    public void addProductCancelHandler(ActionEvent actionEvent) throws IOException {
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
