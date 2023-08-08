package CurrencyExchange;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents a currency entity with conversion details.
 */
@Entity
public class Currency {

    /**
     * The unique identifier for the currency entry.
     */
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;

    /**
     * The currency code representing the source currency.
     */
    @ColumnInfo(name="From")
    protected String from;

    /**
     * The currency code representing the target currency.
     */
    @ColumnInfo(name="To")
    protected String to;

    /**
     * The amount of currency to convert from.
     */
    @ColumnInfo(name="AmountFrom")
    protected double amountFrom;

    /**
     * The converted amount of currency in the target currency.
     */
    @ColumnInfo(name="AmountTo")
    protected double amountTo;

    /**
     * The date of the currency conversion.
     */
    @ColumnInfo(name="Date")
    protected String date;

    /**
     * Creates a new Currency instance.
     *
     * @param id The unique identifier for the currency entry.
     * @param from The currency code representing the source currency.
     * @param to The currency code representing the target currency.
     * @param amountFrom The amount of currency to convert from.
     * @param amountTo The converted amount of currency in the target currency.
     * @param date The date of the currency conversion.
     */
    public Currency(int id, String from, String to, double amountFrom, double amountTo, String date) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.amountFrom = amountFrom;
        this.amountTo = amountTo;
        this.date = date;
    }

    /**
     * Retrieves the currency code representing the source currency.
     *
     * @return The currency code representing the source currency.
     */
    public String getFrom(){ return from; }

    /**
     * Retrieves the currency code representing the target currency.
     *
     * @return The currency code representing the target currency.
     */
    public String getTo(){
        return to;
    }

    /**
     * Retrieves the amount of currency to convert from.
     *
     * @return The amount of currency to convert from.
     */
    public double getAmountFrom(){ return amountFrom; }

    /**
     * Retrieves the converted amount of currency in the target currency.
     *
     * @return The converted amount of currency in the target currency.
     */
    public double getAmountTo(){ return amountTo; }

    /**
     * Retrieves the unique identifier for the currency entry.
     *
     * @return The unique identifier for the currency entry.
     */
    public int getId(){
        return id;
    }

    /**
     * Sets the currency code representing the source currency.
     *
     * @param from The currency code representing the source currency.
     */
    public void setFrom(String from){
        this.from = from;
    }

    /**
     * Sets the currency code representing the target currency.
     *
     * @param to The currency code representing the target currency.
     */
    public void setTo(String to){
        this.to = to;
    }

    /**
     * Sets the amount of currency to convert from.
     *
     * @param amountFrom The amount of currency to convert from.
     */
    public void setAmountFrom(double amountFrom){
        this.amountFrom = amountFrom;
    }

    /**
     * Sets the converted amount of currency in the target currency.
     *
     * @param amountTo The converted amount of currency in the target currency.
     */
    public void setAmountTo(double amountTo){
        this.amountTo = amountTo;
    }

    /**
     * Sets the date of the currency conversion.
     *
     * @param date The date of the currency conversion.
     */
    public void setDate(String date){
        this.date = date;
    }

    /**
     * Retrieves the date of the currency conversion.
     *
     * @return The date of the currency conversion.
     */
    public String getDate(){ return date; }

}