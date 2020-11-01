package pro.evgen.tradeapp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Trade.class}, version = 2, exportSchema = false)
public abstract class TradeDataBase extends RoomDatabase {
    private static final String DB_NAME = "info.db";
    private static TradeDataBase tradeDataBase;
    private static final Object LOCK = new Object();

    public static TradeDataBase getInstance(Context context) {
        synchronized (LOCK) {
            if (tradeDataBase == null) {
                tradeDataBase = Room.databaseBuilder(context, TradeDataBase.class, DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return tradeDataBase;
        }
    }
    public abstract TradeInfoDao tradeInfoDao();
}
