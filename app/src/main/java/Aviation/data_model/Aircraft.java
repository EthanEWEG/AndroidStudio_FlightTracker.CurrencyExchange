package Aviation.data_model;

import com.google.gson.annotations.SerializedName;

/**
 * data model class aircraft information
 */
public class Aircraft {
    @SerializedName("iata")
    private String iata;

    /**
     * Gets iata.
     *
     * @return the iata
     */
    public String getIata() {
        return iata;
    }

    /**
     * Sets iata.
     *
     * @param iata the iata
     */
    public void setIata(String iata) {
        this.iata = iata;
    }
}
