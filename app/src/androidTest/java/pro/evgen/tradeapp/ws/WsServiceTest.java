package pro.evgen.tradeapp.ws;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

public class WsServiceTest {
    WsService wsService = new WsService();
    @Test
    public void name() throws InterruptedException {
        wsService.connect(topicMessage -> {
        });

        while (true) {



            Thread.sleep(100);
        }
    }

}