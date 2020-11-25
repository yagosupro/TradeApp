package pro.evgen.tradeapp.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import pro.evgen.tradeapp.data.Trade;
import pro.evgen.tradeapp.data.TradeDataBase;

public class CardViewModel extends AndroidViewModel {
    private  TradeDataBase tradeDataBase;
    private  LiveData<Trade> tradeLiveData;
    public CardViewModel(@NonNull Application application) {
        super(application);
        tradeDataBase = TradeDataBase.getInstance(application);

    }



    public  LiveData<Trade> getTradeLiveData() {
        tradeLiveData = tradeDataBase.tradeInfoDao().getLastTrade();
        return tradeLiveData;
    }
}
