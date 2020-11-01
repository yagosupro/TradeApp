package pro.evgen.tradeapp;

import android.app.Application;
import android.os.AsyncTask;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import pro.evgen.tradeapp.data.Trade;
import pro.evgen.tradeapp.data.TradeDataBase;
import pro.evgen.tradeapp.ws.WsConnection;
import pro.evgen.tradeapp.ws.WsService;

public class TradeViewModel extends AndroidViewModel {
    private static TradeDataBase dataBase;
    private LiveData<List<Trade>> tradeList;

    private double moreThan;

    public TradeViewModel(@NonNull Application application) {
        super(application);
        dataBase = TradeDataBase.getInstance(application);
    }

    public void load() {
        tradeList =  dataBase.tradeInfoDao().getAllTrade(moreThan);
    }



    public void setMoreThan(double moreThan) {
        this.moreThan = moreThan;
    }

    public double getMoreThan() {
        return moreThan;
    }

    public LiveData<List<Trade>> getTradeList() {
        return tradeList;
    }



    public void deleteAllTrade() {
        new DeleteAllTradeTask().execute();
    }

    private static class DeleteAllTradeTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            dataBase.tradeInfoDao().deleteAllEmployees();
            return null;
        }
    }
}

