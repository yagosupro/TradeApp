package pro.evgen.tradeapp.viewModels;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pro.evgen.tradeapp.Constants;
import pro.evgen.tradeapp.api.ApiFactory;
import pro.evgen.tradeapp.api.ApiService;
import pro.evgen.tradeapp.data.Trade;
import pro.evgen.tradeapp.data.TradeDataBase;

public class TradeFromRestViewModel extends AndroidViewModel {
    private static TradeDataBase tradeDataBase;
    private LiveData<List<Trade>> trades;
    private CompositeDisposable compositeDisposable;

    public TradeFromRestViewModel(@NonNull Application application) {
        super(application);
        tradeDataBase = TradeDataBase.getInstance(application);
        trades = tradeDataBase.tradeInfoDao().getAllTrade(0);
    }

    public LiveData<List<Trade>> getTrades() {
        return trades;
    }

    public void insertTradeFromRest(List<Trade> tradeList) {
        new InsertTradeFromRestTask().execute(tradeList);
    }

    private static class InsertTradeFromRestTask extends AsyncTask<List<Trade>, Void, Void> {

        @Override
        protected Void doInBackground(List<Trade>... lists) {
            if (lists != null && lists.length > 0) {
                tradeDataBase.tradeInfoDao().insertTrades(lists[0]);
            }
            return null;
        }
    }

    public void loadDataFromRest() {
        Log.e(Constants.LOG_TAG, "loadDataFromRest");
        compositeDisposable = new CompositeDisposable();
        List<Trade> trades = new ArrayList<>();
        ApiFactory apiFactory = ApiFactory.getInstance();
        ApiService apiService = apiFactory.getApiService();
        Disposable disposable = apiService.getTrades()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Trade>>() {
                    @Override
                    public void accept(List<Trade> tradeList) throws Exception {
                        Log.e(Constants.LOG_TAG, " " + tradeList.size());
                        insertTradeFromRest(tradeList);
                        Log.e(Constants.LOG_TAG, "addTrades");

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(Constants.LOG_TAG, throwable.getMessage());
                    }
                });
        compositeDisposable.add(disposable);
        Log.e(Constants.LOG_TAG, " Insert trade in db");
//        compositeDisposable.dispose();
    }
}
