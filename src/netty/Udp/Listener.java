package netty.Udp;

import redis.clients.jedis.JedisPubSub;
import util.Redis.HeartBeat;
import util.Redis.RedisMessage;

/**
 * Created by qin on 17-9-7.
 */
public class Listener extends JedisPubSub {
    private final String HEARTBEAT="HEARTBEAT";
    private final String MESSAGE="MESSAGE";

    @Override
    public void onMessage(String channel, String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (RedisMessage.getHead(message)){
                    case HEARTBEAT:
                        HeartBeat.HEARTBEAT=RedisMessage.getBody(message);
                        new HeartBeat().countDown();
                        break;
                    case MESSAGE:
                        String[] temp=RedisMessage.getBody(message).split("/");
                        String[] var=temp[0].split("=");
                        String borg=var[1];
                        new LongConnServer().sendMessage(borg,temp[1]);//给ＴＣＰ发数据
                        break;
                }
            }
        }).start();
    }
}
