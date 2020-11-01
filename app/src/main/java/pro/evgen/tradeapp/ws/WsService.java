package pro.evgen.tradeapp.ws;

import android.util.Log;

import io.reactivex.functions.Consumer;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pro.evgen.tradeapp.data.Trade;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;

public class WsService {
    private CompositeDisposable compositeDisposable;
    public void connect(Consumer<StompMessage> messageHandler) {
//        StompClient mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP , "http://192.168.1.68:4140//stomp/websocket");
        StompClient mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP , "http://52.213.152.238:4141/stomp/websocket");
//        mStompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);
        resetSubscriptions();

        Disposable dispLifecycle = mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            showToast("Stomp connection opened");
                            break;
                        case ERROR:
                            Log.e("TAG", "Stomp connection error", lifecycleEvent.getException());
                            showToast("Stomp connection error");
                            break;
                        case CLOSED:
                            showToast("Stomp connection closed");
                            resetSubscriptions();
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            showToast("Stomp failed server heartbeat");
                            break;
                    }
                });

        compositeDisposable.add(dispLifecycle);

        // Receive greetings
        Disposable dispTopic = mStompClient.topic("/topic/transactions")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messageHandler, throwable -> {
                    Log.e("TAG", "Error on subscribe topic", throwable);
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
