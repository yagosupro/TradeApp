package pro.evgen.tradeapp.ws;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.concurrent.BlockingQueue;

import pro.evgen.tradeapp.TradeViewModel;
import pro.evgen.tradeapp.data.Trade;
import pro.evgen.tradeapp.data.TradeDataBase;

public class WsViewModel extends AndroidViewModel {
    private WsConnection wsConnection = new WsConnection();
    private static TradeDataBase dataBase;


    public WsViewModel(@NonNull Application application) {
        super(application);
        dataBase = TradeDataBase.getInstance(application);
        startLoad();
    }

    public void startLoad(){
        BlockingQueue<Trade> queue = wsConnection.loadTrade();
        new Thread(() -> {
            try {
                insertTrade(queue.take());
            } catch (InterruptedException ignored) { }
        }).start();
    }
    public void insertTrade(Trade trade) {
        new InsertTradeTask().execute(trade);
    }

    private static class InsertTradeTask extends AsyncTask<Trade, Void, Void> {


        @Override
        protected Void doInBackground(Trade... trades) {
            if (trades != null) {
                dataBase.tradeInfoDao().insertTrade(trades[0]);
            }
            return null;
        }
    }

}
