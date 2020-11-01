package pro.evgen.tradeapp.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;


@Dao
public interface TradeInfoDao {
    @Query("SELECT * FROM info WHERE amount > :moreThen")
    LiveData<List<Trade>> getAllTrade(double moreThen);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTrade(Trade trade);

    @Query("DELETE FROM info")
    void deleteAllEmployees();
}
