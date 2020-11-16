package pro.evgen.tradeapp.utils;

import android.app.Application;
import android.os.AsyncTask;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import java.util.List;

import pro.evgen.tradeapp.data.Trade;
import pro.evgen.tradeapp.data.TradeDataBase;

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


//    public void insertTrade(Trade trade) {
//        new InsertTradeTask().execute(trade);
//    }
//
//    private static class InsertTradeTask extends AsyncTask<Trade, Void, Void> {
//
//
//        @Override
//        protected Void doInBackground(Trade... trades) {
//            if (trades != null) {
//                dataBase.tradeInfoDao().insertTrade(trades[0]);
//            }
//            return null;
//        }
//    }



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

