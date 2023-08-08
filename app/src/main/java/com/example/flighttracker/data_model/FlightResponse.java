package com.example.flighttracker.data_model;

import java.util.ArrayList;

/**
 * data class represent the top object response by server
 */
public class FlightResponse {
    private Pagination pagination;
    private ArrayList<FlightTrack> data;

    /**
     * Gets pagination.
     *
     * @return the pagination
     */
    public Pagination getPagination() {
        return pagination;
    }

    /**
     * Sets pagination.
     *
     * @param pagination the pagination
     */
    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public ArrayList<FlightTrack> getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(ArrayList<FlightTrack> data) {
        this.data = data;
    }
}
