package pro.evgen.tradeapp.ws;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import pro.evgen.tradeapp.data.Trade;

public class WsConnection {
    private ObjectMapper mapper;
    private WsService wsService = new WsService();


    public BlockingQueue<Trade> loadTrade() {
        BlockingQueue<Trade> queue = new ArrayBlockingQueue<>(1000);
        wsService.connect(topicMessage -> {
            String s = topicMessage.getPayload();
            mapper = new ObjectMapper();
            queue.add(mapper.readValue(s, Trade.class));
        });
        return queue;

    }
}
