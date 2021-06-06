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
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** Controller for the main screen of the application. */
public class MainScreenController implements Initializable {
    public Label mainScreenLabel;
    public TableView mainPartsTable;
    public TableColumn mainPartsIdCol;
    public TableColumn mainPartsNameCol;
    public TableColumn mainPartsInventoryCol;
    public TableColumn mainPartsPriceCol;
    public Label mainPartsLabel;
    public TextField mainPartsSearch;
    public Button mainPartsAddButton;
    public Button mainPartsModifyButton;
    public Button mainPartsDeleteButton;
    public TableView mainProductsTable;
    public TableColumn mainProductsIdCol;
    public TableColumn mainProductsNameCol;
    public TableColumn mainProductsInventoryCol;
    public TableColumn mainProductsPriceCol;
    public Label mainProductsLabel;
    public TextField mainProductsSearch;
    public Button mainProductsAddButton;
    public Button mainProductsModifyButton;
    public Button mainProductsDeleteButton;
    public Button mainExitButton;
    public Label partsTableMsg;
    public Label productsTableMsg;

    private static Part selectedPart;
    private static int selectedPartIndex;
    private static Product selectedProduct;
    private static int selectedProductIndex;

    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Initializes the main screen and populates the Parts and Products tables.
     * <p>
     * RUNTIME ERROR: Program failed because of a ClassCastException. The JavaFX TableColumn class cannot be cast to TableColumn$CellDataFeatures. I corrected this by replacing the function property setCellFactory with setCellValueFactory in the functions that populate the tables. setCellFactory controls how cell data renders while setCellValueFactory sets the data that the the cell contains.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Parts table
        mainPartsTable.setItems(Inventory.getPartsList());
        mainPartsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainPartsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Products table
        mainProductsTable.setItems(Inventory.getProductsList());
        mainProductsIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        mainProductsNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        mainProductsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("productStock"));
        mainProductsPriceCol.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
    }

    // Part functions start here

    /** Searches the Parts table by Part Name or Part ID. Returns an error message if no parts are found. */
    public void mainPartsSearchHandler(ActionEvent actionEvent) {
        String searchStr = mainPartsSearch.getText();

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
        mainPartsTable.setItems(parts);

        if (parts.size() == 0) {
            partsTableMsg.setText("No matching parts!");
        }
        else {
            partsTableMsg.setText(parts.size() + " part(s) found.");
        }
        mainPartsSearch.setText("");
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

    /**  Opens the Add Part form. */
    public void mainPartsAddHandler(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view_controller/AddPart.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    /** Gets the selected part. */
    public static Part getSelectedPart() {
        return selectedPart;
    }

    /** Gets the index for the selected part. */
    public static int getSelectedPartIndex() { return selectedPartIndex; }

    /** Opens the Modify Part form with data from the selected part. */
    public void mainPartsModifyHandler(ActionEvent actionEvent) throws IOException {
        selectedPart = (Part) mainPartsTable.getSelectionModel().getSelectedItem();
        selectedPartIndex = mainPartsTable.getSelectionModel().getSelectedIndex();

        if (selectedPart == null) {
            partsTableMsg.setText("No part selected!");
        }
        else {
            Parent root = FXMLLoader.load(getClass().getResource("/view_controller/ModifyPart.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Modify Part");
            stage.setScene(scene);
            stage.show();
        }
    }

    /** Deletes the selected part. Displays a message in the UI if no parts are selected. */
    public void mainPartsDeleteHandler(ActionEvent actionEvent) {
        Part partToDelete = (Part) mainPartsTable.getSelectionModel().getSelectedItem();

        if (partToDelete == null) {
            partsTableMsg.setText("Cannot delete. No part selected.");
        }
        else {
            Inventory.deletePart(partToDelete);
            partsTableMsg.setText("Part deleted!");
        }
    }

    // Product functions start here

    /** Searches the Products table by Product Name or Product ID. Returns an error message if no products are found. */
    public void mainProductsSearchHandler(ActionEvent actionEvent) {
        String searchStr = mainProductsSearch.getText();

        // Search by part name and if none found, search by part ID.
        ObservableList<Product> products = searchByProductName(searchStr);

        if(products.size() == 0) {
            try {
                int id = Integer.parseInt(searchStr);
                Product product = searchByProductId(id);
                if (product != null) {
                    products.add(product);
                }
            }
            catch (NumberFormatException e) {
                // ignore exception
            }
        }
        mainProductsTable.setItems(products);

        if (products.size() == 0) {
            productsTableMsg.setText("No matching products!");
        }
        else {
            productsTableMsg.setText(products.size() + " product(s) found.");
        }
        mainProductsSearch.setText("");
    }

    /** Looks for a product name that matches the user's search term. Returns matching products. */
    private ObservableList<Product> searchByProductName(String searchStr) {
        ObservableList<Product> allProducts = Inventory.getProductsList();
        ObservableList<Product> foundProducts = FXCollections.observableArrayList();
        for (Product product : allProducts) {
            if (product.getProductName().contains(searchStr)) {
                foundProducts.add(product);
            }
        }
        return foundProducts;
    }

    /** Looks for a product ID that matches the user's search term. Returns null if no products are found. */
    private Product searchByProductId(int id) {
        ObservableList<Product> allProducts = Inventory.getProductsList();
        for (Product product : allProducts) {
            if (product.getProductId() == id) {
                return product;
            }
        }
        return null;
    }

    /** Opens the Add Product form. */
    public void mainProductsAddHandler(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view_controller/AddProduct.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }

    /** Gets the selected product. */
    public static Product getSelectedProduct() {
        return selectedProduct;
    }

    /** Gets the index for the selected product. */
    public static int getSelectedProductIndex() { return selectedProductIndex; }

    /** Opens the Modify Product form with data from the selected product. */
    public void mainProductsModifyHandler(ActionEvent actionEvent) throws IOException {
        selectedProduct = (Product) mainProductsTable.getSelectionModel().getSelectedItem();
        selectedProductIndex = mainProductsTable.getSelectionModel().getSelectedIndex();

        if (selectedProduct == null) {
            productsTableMsg.setText("No product selected!");
        }
        else {
            Parent root = FXMLLoader.load(getClass().getResource("/view_controller/ModifyProduct.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Modify Product");
            stage.setScene(scene);
            stage.show();
        }
    }

    /** Deletes the selected product. Displays a message in the UI if no product is selected. Displays an alert window if the product has associated parts. */
    public void mainProductsDeleteHandler(ActionEvent actionEvent) {
        Product productToDelete = (Product) mainProductsTable.getSelectionModel().getSelectedItem();

        if (productToDelete == null) {
            productsTableMsg.setText("Cannot delete. No product selected.");
        }
        else {
            ObservableList<Part> partsInProduct = productToDelete.getAssociatedParts();
            if (partsInProduct.size() >= 1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Product has associated parts.");
                alert.setContentText("You must remove associated parts from a product before you can delete the product.");
                alert.showAndWait();
            }
            else {
                Inventory.deleteProduct(productToDelete);
                productsTableMsg.setText("Product deleted!");
            }
        }
    }

    /** Exits the application. */
    public void mainExitHandler(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}


