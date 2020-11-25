package pro.evgen.tradeapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


import pro.evgen.tradeapp.R;

public class MoreInfoActivity extends AppCompatActivity {
    private TextView textViewInfoMoreInfo;
    private TextView textViewBlockDataMoreInfo;
    private TextView textViewLastGasMoreInfo;
    private TextView textViewLastPriceMoreInfo;
    private Button gotoetherscanButton;
    private String hash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_more_info);
        init();
        setValue();
    }

    private void init(){
        textViewInfoMoreInfo = findViewById(R.id.textViewInfo);
        textViewBlockDataMoreInfo = findViewById(R.id.textViewBlockDate);
        textViewLastGasMoreInfo = findViewById(R.id.textViewLastGas);
        textViewLastPriceMoreInfo = findViewById(R.id.textViewLastPriceDetale);
        gotoetherscanButton = findViewById(R.id.buttonGoToEtherscan);
    }

    private void setValue(){
        Intent intent = getIntent();
        String info = intent.getStringExtra("info");
        String blockDate = intent.getStringExtra("blockDate");
        String lastGas = String.valueOf(intent.getDoubleExtra("lastGas", 0));
        String lasPrice = String.valueOf(intent.getDoubleExtra("lastPrice", 0));
        hash = intent.getStringExtra("hash");

        textViewInfoMoreInfo.setText(info);
        textViewBlockDataMoreInfo.setText(blockDate);
        textViewLastGasMoreInfo.setText(lastGas);
        textViewLastPriceMoreInfo.setText(lasPrice);

    }

    public void onClickGoToEtherscan(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(hash));
        Log.e("HASH",  hash);
        startActivity(intent);
    }
}