package pro.evgen.tradeapp.ws;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import pro.evgen.tradeapp.Constants;
import pro.evgen.tradeapp.data.Trade;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;

public class WsConnection {
    private ObjectMapper mapper;
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
        StompClient mStompClient = null;
        try {
            mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP,
                    "https://api.farmdashboard.xyz:4140/stomp/websocket",
                    null,
                    createHttpClient());
            resetSubscriptions();

            Disposable dispLifecycle = mStompClient.lifecycle()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(lifecycleEvent -> {
                        switch (lifecycleEvent.getType()) {
                            case OPENED:
                                Log.e(Constants.LOG_TAG, "Stomp connection open");
                                break;
                            case ERROR:
                                Log.e(Constants.LOG_TAG, "Stomp connection error", lifecycleEvent.getException());
                                break;
                            case CLOSED:
                                Log.e(Constants.LOG_TAG, "Stomp connection closed");
                                resetSubscriptions();
                                break;
                        }
                    });
            compositeDisposable.add(dispLifecycle);
        }catch (Exception e){
            Log.e(Constants.LOG_TAG, e.getMessage());
        }


        if (mStompClient != null) {
            Disposable dispTopic = mStompClient.topic("/topic/transactions")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(messageHandler, throwable -> {
                        Log.e(Constants.LOG_TAG, "Error on subscribe topic", throwable);
                    });

            compositeDisposable.add(dispTopic);
            mStompClient.connect();
        }
    }


    private OkHttpClient createHttpClient() {
        try {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS);
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    X509Certificate[] cArrr = new X509Certificate[0];
                    return cArrr;
                }

                @Override
                public void checkServerTrusted(final X509Certificate[] chain,
                                               final String authType) throws CertificateException {
                }

                @Override
                public void checkClientTrusted(final X509Certificate[] chain,
                                               final String authType) throws CertificateException {
                }
            }};

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            clientBuilder.sslSocketFactory(sslContext.getSocketFactory());
            HostnameVerifier hostnameVerifier = (hostname, session) -> true;
            clientBuilder.hostnameVerifier(hostnameVerifier);
            return clientBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }
}
