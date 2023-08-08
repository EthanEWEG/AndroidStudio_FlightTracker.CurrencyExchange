package CurrencyExchange;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Currency {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;
    @ColumnInfo(name="From")
    protected String from;
    @ColumnInfo(name="To")
    protected String to;
    @ColumnInfo(name="AmountFrom")
    protected double amountFrom;
    @ColumnInfo(name="AmountTo")
    protected double amountTo;
    @ColumnInfo(name="Date")
    protected String date;

    public Currency(int id, String from, String to, double amountFrom, double amountTo, String date)
    {
        this.id = id;
        this.from = from;
        this.to = to;
        this.amountFrom = amountFrom;
        this.amountTo = amountTo;
        this.date = date;
    }

    public String getFrom(){ return from; }

    public String getTo(){
        return to;
    }

    public double getAmountFrom(){ return amountFrom; }

    public double getAmountTo(){ return amountTo; }

    public int getId(){
        return id;
    }

    public void setFrom(String from){
        this.from = from;
    }

    public void setTo(String to){
        this.to = to;
    }

    public void setAmountFrom(double amountFrom){
        this.amountFrom = amountFrom;
    }
    public void setAmountTo(double amountTo){
        this.amountTo = amountTo;
    }

    public void setDate(String date){
        this.date = date;
    }


    public String getDate(){ return date; }

}
