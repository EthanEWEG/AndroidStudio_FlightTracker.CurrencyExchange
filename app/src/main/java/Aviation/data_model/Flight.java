package Aviation.data_model;

import java.io.Serializable;

/**
 * data model class for a flight
 */
public class Flight implements Serializable {
    private String number;

    /**
     * Gets number.
     *
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets number.
     *
     * @param number the number
     */
    public void setNumber(String number) {
        this.number = number;
    }
}
