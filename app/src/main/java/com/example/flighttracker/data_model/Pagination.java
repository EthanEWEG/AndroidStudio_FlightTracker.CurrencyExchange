package com.example.flighttracker.data_model;

/**
 * data class of pagination response from server
 */
public class Pagination {
    private int offset;

    /**
     * Gets offset.
     *
     * @return the offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Sets offset.
     *
     * @param offset the offset
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

}
