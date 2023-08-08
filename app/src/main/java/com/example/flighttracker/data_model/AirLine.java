package com.example.flighttracker.data_model;

import java.io.Serializable;

/**
 * data model class for airline
 */
public class AirLine implements Serializable {
    private String name;

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }
}
