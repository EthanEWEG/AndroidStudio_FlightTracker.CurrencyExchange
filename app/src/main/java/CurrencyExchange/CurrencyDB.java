package CurrencyExchange;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * A Room database class that defines the database and provides access to the DAO.
 */
@Database(entities = {Currency.class}, version = 1,exportSchema = false)
public abstract class CurrencyDB extends RoomDatabase {

    /**
     * Retrieves the CurrencyDAO instance to access the database operations.
     *
     * @return The CurrencyDAO instance.
     */
    public abstract CurrencyDAO currencyDAO();

}