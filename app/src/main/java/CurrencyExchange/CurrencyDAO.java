package CurrencyExchange;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object (DAO) interface for the Currency entity.
 * This interface provides methods to interact with the Currency table in the database.
 */
@Dao
public interface CurrencyDAO {

    /**
     * Inserts a new Currency object into the database.
     *
     * @param c The Currency object to be inserted.
     * @return The row ID of the newly inserted Currency object.
     */
    @Insert
    long insertMessage(Currency c);

    /**
     * Retrieves all Currency objects from the database.
     *
     * @return A List of all Currency objects in the database.
     */
    @Query("SELECT * FROM Currency")
    List<Currency> getAllMessages();

    /**
     * Deletes a Currency object from the database.
     *
     * @param c The Currency object to be deleted.
     */
    @Delete
    void deleteMessage(Currency c);
}
