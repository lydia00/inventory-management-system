package model;
/**
 * @author lydia00
 */

/** Class to model an in-house part that has the machine ID field. */
public class InHousePart extends Part {
    private int machineId;

    /** Constructor for an in-house part. */
    public InHousePart(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /** Machine ID getter. */
    public int getMachineId() {
        return machineId;
    }

    /** Machine ID setter. */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
