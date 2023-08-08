package CurrencyExchange;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CurrencyDAO {

    @Insert
    public long insertMessage(Currency c);

    @Query("Select * from Currency")
    public List<Currency> getAllMessages();

    @Delete
    void deleteMessage(Currency c);

}
