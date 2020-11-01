package pro.evgen.tradeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.AtomicDouble;

import java.util.ArrayList;
import java.util.List;

import pro.evgen.tradeapp.adapters.TradeAdapter;
import pro.evgen.tradeapp.data.Trade;
import pro.evgen.tradeapp.utils.ClientListView;
import pro.evgen.tradeapp.ws.WsConnection;
import pro.evgen.tradeapp.ws.WsViewModel;

public class MainActivity extends AppCompatActivity implements ClientListView {
    private RecyclerView recyclerView;
    private TradeAdapter tradeAdapter;
    private SeekBar seekBar;
    private TradeViewModel tradeViewModel;
    private WsViewModel wsViewModel;
    private static final String HASH_URL = "https://etherscan.io/tx/%s";
    private TextView lastPrice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lastPrice = findViewById(R.id.textViewLastPrice);

        setAdapter();
        initSeekBar();
        initTradeView(0);
        startConnect();
        showHash();
    }



    private void startConnect(){
        wsViewModel = ViewModelProviders.of(MainActivity.this).get(WsViewModel.class);
    }

    private void initTradeView(double moreThan) {
        tradeViewModel = ViewModelProviders.of(MainActivity.this).get(TradeViewModel.class);
        tradeViewModel.setMoreThan(moreThan);
        tradeViewModel.load();
        tradeViewModel.getTradeList().observe(MainActivity.this, new Observer<List<Trade>>() {
            @Override
            public void onChanged(List<Trade> tradeList) {
                tradeAdapter.setTradeInfoList(tradeList);
//                int size = tradeAdapter.getItemCount()-1;
//                Trade trade = tradeList.get(size);
//                lastPrice.setText(String.valueOf(trade.getLastPrice()));
//                Log.e("Trade", ""+ trade.getAmount());
            }
        });
        tradeViewModel.load();



    }

    private void initSeekBar() {
        seekBar = findViewById(R.id.seekBar3);
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
//                tradeViewModel.setMoreThan(seekBar.getProgress());
                initTradeView(seekBar.getProgress());
            }
        });
    }


    private void setAdapter() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tradeAdapter = new TradeAdapter();
        recyclerView.setAdapter(tradeAdapter);
//        if (tradeInfoList.size()>2) {
//        recyclerView.scrollToPosition(tradeInfoList.size() - 1);
//        }
    }


    @Override
    public void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void showHash(){
        tradeAdapter.setOnTradeInfoClickListener(position ->{
            Trade trade = tradeAdapter.getTradeInfoList().get(position);
            String address = String.format(HASH_URL, trade.getHash());
            Log.d("HASH" , ""+ address);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
            startActivity(intent);
        });
    }


//   private double getMinValue() {
//        double max = Double.MAX_VALUE;
//        for (int i = 0; i < tradeAdapter.getTradeInfoList().size(); i++) {
//            Trade tradeInfo = tradeAdapter.getTradeInfoList.get(i);
//            double value = tradeInfo.getAmount();
//            if (value < max) {
//                max = value;
//            }
//        }
//        return max;
//    }
//
//    private double getMaxValue() {
//        double min = Double.MIN_VALUE;
//        for (int i = 0; i < tradeInfoList.size(); i++) {
//            Trade tradeInfo = tradeInfoList.get(i);
//            double value = tradeInfo.getAmount();
//            if (value > min) {
//                min = value;
//            }
//        }
//        return min;
//    }


//    private List<TradeInfo> methodOfSort(int progress) {
//        List<TradeInfo> sortTrade = null;
//        double min = getMinValue();
//        double max = getMaxValue();
//        double diff = max - min;
//        double result = diff / 10;
//
//        if (progress == 0) {
//          sortTrade = new ArrayList<>();
//          sortTrade.addAll(tradeInfoList);
//        }
//
//        if (progress == 1) {
//            sortTrade = new ArrayList<>(tradeInfoList.size());
//            for (int i = 0; i < tradeInfoList.size(); i++) {
//                TradeInfo tradeInfo = tradeInfoList.get(i);
//                double value = Double.parseDouble(tradeInfo.getAmount());
//                if (value >= max - (result * 9)) {
//                    sortTrade.add(tradeInfo);
//                }
//            }
//        }
//        if (progress == 2) {
//            sortTrade = new ArrayList<>(tradeInfoList.size());
//            for (int i = 0; i < tradeInfoList.size(); i++) {
//                TradeInfo tradeInfo = tradeInfoList.get(i);
//                double value = Double.parseDouble(tradeInfo.getAmount());
//                if (value >= max - (result * 8)) {
//                    sortTrade.add(tradeInfo);
//                }
//            }
//        }
//        if (progress == 3) {
//            sortTrade = new ArrayList<>(tradeInfoList.size());
//            for (int i = 0; i < tradeInfoList.size(); i++) {
//                TradeInfo tradeInfo = tradeInfoList.get(i);
//                double value = Double.parseDouble(tradeInfo.getAmount());
//                if (value >= max - (result * 7)) {
//                    sortTrade.add(tradeInfo);
//                }
//            }
//        }
//        if (progress == 4) {
//            sortTrade = new ArrayList<>(tradeInfoList.size());
//            for (int i = 0; i < tradeInfoList.size(); i++) {
//                TradeInfo tradeInfo = tradeInfoList.get(i);
//                double value = Double.parseDouble(tradeInfo.getAmount());
//                if (value >= max - (result * 7)) {
//                    sortTrade.add(tradeInfo);
//                }
//            }
//        }
//        if (progress == 5) {
//            sortTrade = new ArrayList<>(tradeInfoList.size());
//            for (int i = 0; i < tradeInfoList.size(); i++) {
//                TradeInfo tradeInfo = tradeInfoList.get(i);
//                double value = Double.parseDouble(tradeInfo.getAmount());
//                if (value >= max - (result * 6)) {
//                    sortTrade.add(tradeInfo);
//                }
//            }
//        }
//        if (progress == 6) {
//            sortTrade = new ArrayList<>(tradeInfoList.size());
//            for (int i = 0; i < tradeInfoList.size(); i++) {
//                TradeInfo tradeInfo = tradeInfoList.get(i);
//                double value = Double.parseDouble(tradeInfo.getAmount());
//                if (value >= max - (result * 5)) {
//                    sortTrade.add(tradeInfo);
//                }
//            }
//        }
//        if (progress == 7) {
//            sortTrade = new ArrayList<>(tradeInfoList.size());
//            for (int i = 0; i < tradeInfoList.size(); i++) {
//                TradeInfo tradeInfo = tradeInfoList.get(i);
//                double value = Double.parseDouble(tradeInfo.getAmount());
//                if (value >= max - (result * 4)) {
//                    sortTrade.add(tradeInfo);
//                }
//            }
//        }
//        if (progress == 8) {
//            sortTrade = new ArrayList<>(tradeInfoList.size());
//            for (int i = 0; i < tradeInfoList.size(); i++) {
//                TradeInfo tradeInfo = tradeInfoList.get(i);
//                double value = Double.parseDouble(tradeInfo.getAmount());
//                if (value > max - (result * 3)) {
//                    sortTrade.add(tradeInfo);
//                }
//            }
//        }
//        if (progress == 9) {
//            sortTrade = new ArrayList<>(tradeInfoList.size());
//            for (int i = 0; i < tradeInfoList.size(); i++) {
//                TradeInfo tradeInfo = tradeInfoList.get(i);
//                double value = Double.parseDouble(tradeInfo.getAmount());
//                if (value >= max - (result * 2)) {
//                    sortTrade.add(tradeInfo);
//                }
//            }
//        }
//        if (progress == 10) {
//            sortTrade = new ArrayList<>(tradeInfoList.size());
//            for (int i = 0; i < tradeInfoList.size(); i++) {
//                TradeInfo tradeInfo = tradeInfoList.get(i);
//                double value = Double.parseDouble(tradeInfo.getAmount());
//                if (value >= max - (result * 1)) {
//                    sortTrade.add(tradeInfo);
//                }
//            }
//        }
//        return sortTrade;
//    }
}