package model;
/**
 * @author lydia00
 */

/** Class to model an outsourced part that has the company name field. */
public class OutsourcedPart extends Part {
    private String companyName;

    /** Constructor for an outsourced part. */
    public OutsourcedPart(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /** Company name getter. */
    public String getCompanyName() {
        return companyName;
    }

    /** Company name setter. */
    public void setCompanyName() {
        this.companyName = companyName;
    }
}
