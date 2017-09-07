package redis;

import redis.clients.jedis.Jedis;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by qin on 17-9-6.
 */
public class JedisPool {
    private static String ip="127.0.0.1";
    private static int port=6379;
    JedisPool(){initepool();}
    private static ArrayBlockingQueue<Jedis> pool =new ArrayBlockingQueue<Jedis>(30);
    private static void initepool() {
        for (int i=0;i<10;i++){
            try {
                pool.put(new Jedis(ip,port));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        JedisPool.initepool();
    }
    public static Jedis getJedis(){
        try {
            return pool.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void returnJedis(Jedis jedis){
        try {
            pool.put(jedis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
