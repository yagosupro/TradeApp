package pro.evgen.tradeapp.data;

import android.util.Log;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pro.evgen.tradeapp.Constants;
import pro.evgen.tradeapp.api.ApiFactory;
import pro.evgen.tradeapp.api.ApiService;
import pro.evgen.tradeapp.pojo.Trade;

public class LoadTradeFromRest {
    private TradeDataBase tradeDataBase;
    private CompositeDisposable compositeDisposable;
    private ApiService apiService;
    private ApiFactory apiFactory;

    public LoadTradeFromRest(TradeDataBase tradeDataBase) {
        this.tradeDataBase = tradeDataBase;
    }

    public void loadDataFromRest() {
        resetSubscriptions();
        Log.e(Constants.LOG_TAG, "loadDataFromRest");
        apiFactory = ApiFactory.getInstance();
        apiService = apiFactory.getApiService();
        Disposable disposable = apiService.getTrades()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<List<Trade>>() {
                    @Override
                    public void accept(List<Trade> tradeList) throws Exception {
                        tradeDataBase.tradeInfoDao().insertTrades(tradeList);
                        Log.e(Constants.LOG_TAG, " Insert trade in db");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(Constants.LOG_TAG, "LoadDataFromRest " + throwable.getMessage());
                        resetSubscriptions();
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }
}
