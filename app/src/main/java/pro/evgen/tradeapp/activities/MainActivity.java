package pro.evgen.tradeapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pro.evgen.tradeapp.Constants;
import pro.evgen.tradeapp.service.WebSocketService;
import pro.evgen.tradeapp.viewModels.CardViewModel;
import pro.evgen.tradeapp.viewModels.TradeFromRestViewModel;
import pro.evgen.tradeapp.R;
import pro.evgen.tradeapp.adapters.TradeAdapter;
import pro.evgen.tradeapp.data.Trade;
import pro.evgen.tradeapp.network.NetworkUtils;
import pro.evgen.tradeapp.viewModels.TradeViewModel;

import static pro.evgen.tradeapp.R.string.no_internet_connection;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TradeAdapter tradeAdapter;
    private SeekBar seekBar;
    private static final String HASH_URL = "https://etherscan.io/tx/%s";
    private TextView lastPriceMain;
    private TradeFromRestViewModel tradeFromRestViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();
        setAdapter();
        initSeekBar();
        initTradeView(0);
        loadDataFromRest();
        showMore();
        setCardFarmPriceInfo();

        Intent ws = new Intent(this, WebSocketService.class);
        startService(ws);
    }

    private void setCardFarmPriceInfo() {
        CardViewModel cardViewModel = ViewModelProviders.of(MainActivity.this).get(CardViewModel.class);
        cardViewModel.getTradeLiveData().observe(this, new Observer<Trade>() {
            @Override
            public void onChanged(Trade trade) {
                lastPriceMain.setText(String.valueOf(Math.floor(trade.getLastPrice() * 100) / 100.0));
                Log.e(Constants.LOG_TAG, String.valueOf(trade.getAmount()));
            }
        });


    }

    public void loadDataFromRest() {
        try {
            if (NetworkUtils.checkNetwork(getApplicationContext())) {
                tradeFromRestViewModel = ViewModelProviders.of(this).get(TradeFromRestViewModel.class);
                tradeFromRestViewModel.getTrades().observe(this, new Observer<List<Trade>>() {
                    @Override
                    public void onChanged(List<Trade> tradeList) {
                        tradeAdapter.setTradeInfoList(tradeList);
                    }
                });
            } else {
                Toast.makeText(this, no_internet_connection, Toast.LENGTH_SHORT).show();
            }
            tradeFromRestViewModel.loadDataFromRest();
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, e.getMessage());
        }
    }

    private void initTradeView(double moreThan) {
        TradeViewModel tradeViewModel = ViewModelProviders.of(MainActivity.this).get(TradeViewModel.class);
        tradeViewModel.setMoreThan(moreThan);
        tradeViewModel.load();
        tradeViewModel.getTradeList().observe(MainActivity.this, new Observer<List<Trade>>() {
            @Override
            public void onChanged(List<Trade> tradeList) {
                tradeAdapter.setTradeInfoList(tradeList);
                recyclerView.scrollToPosition(tradeAdapter.getItemCount() - 1);
                Trade trade = tradeList.get(tradeAdapter.getItemCount() - 1);

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
            intent.putExtra(getString(R.string.info_label), info);
            intent.putExtra(getString(R.string.blockdate_label), getDate(trade.getBlockDate()));
            intent.putExtra(getString(R.string.lastgas_label), Math.floor(trade.getLastGas() * 100) / 100.0);
            intent.putExtra(getString(R.string.lastprice_label), Math.floor(trade.getLastPrice() * 100) / 100.0);
            intent.putExtra(getString(R.string.hash_label), getHash(trade.getHash()));
            startActivity(intent);
        });
    }

    private String getHash(String hash) {
        return String.format(HASH_URL, hash);
    }

    private String getDate(long i) {
        Date date = new Date(i * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.sdf_parser));
        return sdf.format(date);
    }
}