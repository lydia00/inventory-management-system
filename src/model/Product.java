package model;
/**
 * @author lydia00
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Class to model a product. */
public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int productId;
    private String productName;
    private double productPrice;
    private int productStock;
    private int productMin;
    private int productMax;

    /** Constructor for a product. */
    public Product(int id, String name, double price, int stock, int min, int max){
        this.productId = id;
        this.productName = name;
        this.productPrice = price;
        this.productStock = stock;
        this.productMin = min;
        this.productMax = max;
    }

    // Getters
    /** Gets the product ID. */
    public int getProductId() {
        return productId;
    }

    /** Gets the product name. */
    public String getProductName() { return productName; }

    /** Gets the product price. */
    public double getProductPrice() { return productPrice; }

    /** Gets the product stock. */
    public int getProductStock() {
        return productStock;
    }

    /** Gets the product min. */
    public int getProductMin() {
        return productMin;
    }

    /** Gets the product max. */
    public int getProductMax() {
        return productMax;
    }

    /** Gets the product's associated parts. */
    public ObservableList<Part> getAssociatedParts() {
        return associatedParts;
    }

    // Setters
    /** Sets the product ID. */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /** Sets the product name. */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /** Sets the product price. */
    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    /** Sets the product stock. */
    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    /** Sets the product min. */
    public void setProductMin(int productMin) {
        this.productMin = productMin;
    }

    /** Sets the product max. */
    public void setProductMax(int productMax) {
        this.productMax = productMax;
    }

    /** Sets the product's associated parts. */
    public void setAssociatedParts(ObservableList<Part> parts){
        this.associatedParts = parts;
    }
}
