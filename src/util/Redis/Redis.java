package util.Redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * Created by qin on 17-9-7.
 */
public class Redis {
    public static void publish(String channel, String message){
        Jedis jedis=new Jedis("127.0.0.1",6379);
        jedis.publish(channel,message);
        jedis.close();
    }
}
