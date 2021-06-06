package model;
/**
 * @author lydia00
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Class to model the complete inventory of parts and products. */
public class Inventory {
    private static ObservableList<Product> productsList = FXCollections.observableArrayList();
    private static ObservableList<Part> partsList = FXCollections.observableArrayList();
    private static int incPartId = 0;
    private static int incProdId = 0;

    // Parts
    /** Getter for the complete list of parts. */
    public static ObservableList<Part> getPartsList(){
        return partsList;
    }

    /** Getter for the incrementing part ID. */
    public static int getIncPartId() {
        return ++incPartId;
    }

    /** Adds a part to the parts list. */
    public static void addPart(Part part){
        partsList.add(part);
    }

    /** Deletes a part from the parts list. */
    public static void deletePart(Part part){
        partsList.remove(part);
    }

    // Products
    /** Getter for the complete list of products. */
    public static ObservableList<Product> getProductsList() {
        return productsList;
    }

    /** Getter for the incrementing product ID. */
    public static int getIncProdId() {
        return ++incProdId;
    }

    /** Adds a product to the products list. */
    public static void addProduct(Product product) {
        productsList.add(product);
    }

    /** Deletes a product to the products list. */
    public static void deleteProduct(Product product) {
        productsList.remove(product);
    }
}
