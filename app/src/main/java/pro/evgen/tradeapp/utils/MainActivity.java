package pro.evgen.tradeapp.utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import pro.evgen.tradeapp.R;
import pro.evgen.tradeapp.adapters.TradeAdapter;
import pro.evgen.tradeapp.data.Trade;
import pro.evgen.tradeapp.ws.WebSocketService;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TradeAdapter tradeAdapter;
    private SeekBar seekBar;
    private TradeViewModel tradeViewModel;
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
        showHash();
        startService(new Intent(this, WebSocketService.class));
    }


//
//    private void startConnect(){
//        wsViewModel = ViewModelProviders.of(MainActivity.this).get(WsViewModel.class);
//    }

    private void initTradeView(double moreThan) {
        tradeViewModel = ViewModelProviders.of(MainActivity.this).get(TradeViewModel.class);
        tradeViewModel.setMoreThan(moreThan);

        tradeViewModel.load();
        tradeViewModel.getTradeList().observe(MainActivity.this, new Observer<List<Trade>>() {
            @Override
            public void onChanged(List<Trade> tradeList) {
                tradeAdapter.setTradeInfoList(tradeList);
                recyclerView.scrollToPosition(tradeAdapter.getItemCount()- 1);
                int size = tradeAdapter.getItemCount()-1;
                Trade trade = tradeList.get(size);
                double d = Math.floor(trade.getLastPrice()*100)/100.0;
                lastPrice.setText(String.valueOf(d));
//
//                if (tradeAdapter.getItemCount()>20){
//                    tradeViewModel.deleteAllTrade();
//                }
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
                initTradeView(seekBar.getProgress());
            }
        });
    }


    private void setAdapter() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tradeAdapter = new TradeAdapter();
        recyclerView.setAdapter(tradeAdapter);
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

}