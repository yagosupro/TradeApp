package pro.evgen.tradeapp.ws;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pro.evgen.tradeapp.data.Trade;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;

public class WsConnection {
    private ObjectMapper mapper;

    private static final String LOG_TAG = "TradeAppTAG";
    private CompositeDisposable compositeDisposable;

    public BlockingQueue<Trade> initConnection() {
        BlockingQueue<Trade> queue = new ArrayBlockingQueue<>(1000);
        connect(topicMessage -> {
            String s = topicMessage.getPayload();
            mapper = new ObjectMapper();
            queue.add(mapper.readValue(s, Trade.class));
        });
        return queue;

    }


    private void connect(Consumer<StompMessage> messageHandler) {
        StompClient mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "http://192.168.1.68:4140//stomp/websocket");
//        StompClient mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP , "https://api.farmdashboard.xyz:4140/stomp/websocket");
        resetSubscriptions();

        Disposable dispLifecycle = mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.e(LOG_TAG, "Stomp connection open");
                            break;
                        case ERROR:
                            Log.e(LOG_TAG, "Stomp connection error", lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            Log.e(LOG_TAG, "Stomp connection closed");
                            resetSubscriptions();
                            break;
                    }
                });

        compositeDisposable.add(dispLifecycle);

        // Receive greetings
        Disposable dispTopic = mStompClient.topic("/topic/transactions")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messageHandler, throwable -> {
                    Log.e(LOG_TAG, "Error on subscribe topic", throwable);
                });

        compositeDisposable.add(dispTopic);
        mStompClient.connect();
    }

    private void showToast(String stomp_connection_error) {
        System.out.println(stomp_connection_error);
    }

    private void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }


}
