package pro.evgen.tradeapp.api;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api/transactions/history/uni")
    Observable<List<TradeInfo>> getTrades();
}
