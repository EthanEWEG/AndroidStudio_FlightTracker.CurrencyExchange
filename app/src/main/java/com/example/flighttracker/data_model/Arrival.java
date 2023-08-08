package com.example.flighttracker.data_model;

import java.io.Serializable;

/**
 * data model class for arrival
 */
public class Arrival implements Serializable {
    private String airport;
    private String terminal;
    private int delay;

    private String gate;

    /**
     * Gets airport.
     *
     * @return the airport
     */
    public String getAirport() {
        return airport;
    }

    /**
     * Sets airport.
     *
     * @param airport the airport
     */
    public void setAirport(String airport) {
        this.airport = airport;
    }

    /**
     * Gets terminal.
     *
     * @return the terminal
     */
    public String getTerminal() {
        return terminal;
    }

    /**
     * Sets terminal.
     *
     * @param terminal the terminal
     */
    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    /**
     * Gets delay.
     *
     * @return the delay
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Sets delay.
     *
     * @param delay the delay
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * Gets gate.
     *
     * @return the gate
     */
    public String getGate() {
        return gate;
    }

    /**
     * Sets gate.
     *
     * @param gate the gate
     */
    public void setGate(String gate) {
        this.gate = gate;
    }
}
