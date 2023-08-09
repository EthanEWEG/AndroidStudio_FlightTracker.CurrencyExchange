package Aviation.data_model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * Data model class for flight search result
 */
public class FlightTrack implements Serializable {
    @SerializedName("flight_date")
    private String flightDate;

    @SerializedName("flight_status")
    private String flightStatus;

    private Departure departure;
    private Arrival arrival;

    private AirLine airline;
    private Flight flight;

    @SerializedName("aircraft")
    private Aircraft aircraft;

    /**
     * Gets flight date.
     *
     * @return the flight date
     */
    public String getFlightDate() {
        return flightDate;
    }

    /**
     * Sets flight date.
     *
     * @param flightDate the flight date
     */
    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }

    /**
     * Gets flight status.
     *
     * @return the flight status
     */
    public String getFlightStatus() {
        return flightStatus;
    }

    /**
     * Sets flight status.
     *
     * @param flightStatus the flight status
     */
    public void setFlightStatus(String flightStatus) {
        this.flightStatus = flightStatus;
    }

    /**
     * Gets departure.
     *
     * @return the departure
     */
    public Departure getDeparture() {
        return departure;
    }

    /**
     * Sets departure.
     *
     * @param departure the departure
     */
    public void setDeparture(Departure departure) {
        this.departure = departure;
    }

    /**
     * Gets arrival.
     *
     * @return the arrival
     */
    public Arrival getArrival() {
        return arrival;
    }

    /**
     * Sets arrival.
     *
     * @param arrival the arrival
     */
    public void setArrival(Arrival arrival) {
        this.arrival = arrival;
    }

    /**
     * Gets airline.
     *
     * @return the airline
     */
    public AirLine getAirline() {
        return airline;
    }

    /**
     * Sets airline.
     *
     * @param airline the airline
     */
    public void setAirline(AirLine airline) {
        this.airline = airline;
    }

    /**
     * Gets flight.
     *
     * @return the flight
     */
    public Flight getFlight() {
        return flight;
    }

    /**
     * Sets flight.
     *
     * @param flight the flight
     */
    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    /**
     * Gets aircraft.
     *
     * @return the aircraft
     */
    public Aircraft getAircraft() {
        return aircraft;
    }

    /**
     * Sets aircraft.
     *
     * @param aircraft the aircraft
     */
    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }
}
