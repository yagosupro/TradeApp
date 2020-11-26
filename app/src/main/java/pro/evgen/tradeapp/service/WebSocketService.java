package pro.evgen.tradeapp.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.concurrent.BlockingQueue;

import io.reactivex.disposables.CompositeDisposable;
import pro.evgen.tradeapp.data.LoadTradeFromRest;
import pro.evgen.tradeapp.R;
import pro.evgen.tradeapp.pojo.Trade;
import pro.evgen.tradeapp.data.TradeDataBase;
import pro.evgen.tradeapp.activities.MainActivity;
//import pro.evgen.tradeapp.viewModels.TradeFromRestViewModel;
import pro.evgen.tradeapp.ws.WsConnection;
import ua.naiksoftware.stomp.StompClient;

public class WebSocketService extends Service {
    private static int NOTIFY_ID = 1;
    private static final String CHANNEL_ID = "MyChanel11";
    private LoadTradeFromRest loadTradeFromRest;

    private WsConnection wsConnection;
    private TradeDataBase dataBase;
    private PendingIntent contentIntent;
    private Uri ringURI;
    private Intent notificationIntent;
    private long[] vibrate;
    public static StompClient stompClient;
    private CompositeDisposable compositeDisposable;

    private void isConnectedWs() {
        new Thread(() -> {
            while (true) {
                if (!stompClient.isConnected()) {
                    wsConnection.resetSubscriptions();
                    stompClient.disconnect();
                    try {
                        startLoad();
                        Thread.sleep(1500);


                    } catch (Exception e) {
//                        Log.e(Constants.LOG_TAG, e.getMessage());
                    }
                }
            }
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        vibrate = new long[]{500, 500, 500};
        dataBase = TradeDataBase.getInstance(getApplicationContext());
        wsConnection = new WsConnection();
        loadTradeFromRest = new LoadTradeFromRest(dataBase);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startLoad();
        isConnectedWs();
        return super.onStartCommand(intent, flags, startId);
    }


    public void startLoad() {
        BlockingQueue<Trade> queue = wsConnection.initConnection();
        new Thread(() -> {
            loadTradeFromRest.loadDataFromRest();
            while (true) {
                Trade trade = null;
                String type = null;
                String coin = null;
                double amount = 0.0;
                String otherCoin = null;
                double otherAmount = 0.0;
                try {
                    trade = queue.take();
                    type = trade.getType();
                    coin = trade.getCoin();
                    amount = Math.floor(trade.getAmount() * 100) / 100.0;
                    otherCoin = trade.getOtherCoin();
                    otherAmount = Math.floor(trade.getOtherAmount() * 100) / 100.0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (trade != null) {
                    dataBase.tradeInfoDao().insertTrade(trade);
                    sendNotification(type, coin, amount, otherCoin, otherAmount);
                }
            }
        }).start();
    }

    void sendNotification(String type, String coin, double amount, String otherCoin, double otherAmount) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.chanel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        ringURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationIntent = new Intent(this, MainActivity.class);
        contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Notification builder =
                new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.traktor)
                        .setContentTitle(type)
                        .setContentText(amount + " " + coin + " for " + otherCoin + " " + otherAmount)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(contentIntent)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL))
                        .setVibrate(vibrate)
                        .setPriority(NotificationCompat.PRIORITY_HIGH).build();


        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFY_ID, builder);
        if (NOTIFY_ID == 100) {
            NOTIFY_ID = 0;
        }
        NOTIFY_ID++;
    }
}
