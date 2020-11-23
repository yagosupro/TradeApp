package pro.evgen.tradeapp.utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pro.evgen.tradeapp.api.ApiFactory;
import pro.evgen.tradeapp.api.ApiService;
import pro.evgen.tradeapp.api.TradeInfo;
import pro.evgen.tradeapp.R;
import pro.evgen.tradeapp.adapters.TradeAdapter;
import pro.evgen.tradeapp.data.Trade;
import pro.evgen.tradeapp.service.WebSocketService;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MyTradeLog";
    private RecyclerView recyclerView;
    private TradeAdapter tradeAdapter;
    private SeekBar seekBar;
    private TradeViewModel tradeViewModel;
    private static final String HASH_URL = "https://etherscan.io/tx/%s";
    private TextView lastPriceMain;
    private float x1, x2, y1, y2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();
        setAdapter();
        initSeekBar();
        initTradeView(0);
        startService(new Intent(this, WebSocketService.class));
        showMore();
        ifWebsocketConnectionClose();


    }

    private void ifWebsocketConnectionClose() {
        List<TradeInfo> tradeInfos2 = new ArrayList<>();
        ApiFactory apiFactory = ApiFactory.getInstance();
        ApiService apiService = apiFactory.getApiService();
        apiService.getTrades()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TradeInfo>>() {
                    @Override
                    public void accept(List<TradeInfo> tradeInfos) throws Exception {
                        tradeInfos2.addAll(tradeInfos);
                        Log.e(LOG_TAG, ""+tradeInfos2.size());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(LOG_TAG, throwable.getMessage());
                    }
                });
    }


    private void initTradeView(double moreThan) {
        tradeViewModel = ViewModelProviders.of(MainActivity.this).get(TradeViewModel.class);
        tradeViewModel.setMoreThan(moreThan);
        tradeViewModel.load();
        tradeViewModel.getTradeList().observe(MainActivity.this, new Observer<List<Trade>>() {
            @Override
            public void onChanged(List<Trade> tradeList) {
                tradeAdapter.setTradeInfoList(tradeList);
                recyclerView.scrollToPosition(tradeAdapter.getItemCount() - 1);
                int size = tradeAdapter.getItemCount() - 1;
                Trade trade = tradeList.get(size);
                double d = Math.floor(trade.getLastPrice() * 100) / 100.0;
                lastPriceMain.setText(String.valueOf(d));
            }
        });
        tradeViewModel.load();
    }

    private void initSeekBar() {

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this, "" + seekBar.getProgress(), Toast.LENGTH_SHORT).show();
                initTradeView(seekBar.getProgress());
            }
        });
    }


    private void setAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tradeAdapter = new TradeAdapter();
        recyclerView.setAdapter(tradeAdapter);

    }

    private void init() {
        lastPriceMain = findViewById(R.id.textViewLastPrice);
        recyclerView = findViewById(R.id.recyclerView);
        seekBar = findViewById(R.id.seekBar3);
    }

    private void showMore() {
        tradeAdapter.setOnShowMoreClickListener(position -> {
            Intent intent = new Intent(MainActivity.this, MoreInfoActivity.class);
            Trade trade = tradeAdapter.getTradeInfoList().get(position);
            String info = String.format("%s %s for %s %s", Math.floor(trade.getAmount() * 100) / 100.0, trade.getCoin(),
                    trade.getOtherCoin(), Math.floor(trade.getOtherAmount() * 100) / 100.0);
            intent.putExtra("info", info);
            intent.putExtra("blockDate", getDate(trade.getBlockDate()));
            intent.putExtra("lastGas", Math.floor(trade.getLastGas() * 100) / 100.0);
            intent.putExtra("lastPrice", Math.floor(trade.getLastPrice() * 100) / 100.0);
            intent.putExtra("hash", getHash(trade.getHash()));
            Log.e(LOG_TAG, "" + trade.getLastGas());
            Log.e(LOG_TAG, "" + trade.getLastPrice());
            startActivity(intent);
        });
    }

    private String getHash(String hash) {
        return HASH_URL + hash;
    }

    private String getDate(int i) {
        Date date = new Date(i);
        SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy hh:mm:ss");
        return sdf.format(date);
    }
}