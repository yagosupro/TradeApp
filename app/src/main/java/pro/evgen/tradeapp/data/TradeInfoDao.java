package pro.evgen.tradeapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import pro.evgen.tradeapp.pojo.Trade;


@Dao
public interface TradeInfoDao {
    @Query("SELECT * FROM info WHERE amount > :moreThen ORDER BY blockDate")
    LiveData<List<Trade>> getAllTrade(double moreThen);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTrade(Trade trade);

    @Query("DELETE FROM info")
    void deleteAllEmployees();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTrades(List<Trade>tradeList);

    @Query("SELECT* FROM info ORDER BY blockDate DESC LIMIT 1")
    LiveData<Trade> getLastTrade();

}
