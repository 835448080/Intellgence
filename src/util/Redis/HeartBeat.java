package util.Redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by qin on 17-9-7.
 */
public class HeartBeat {
    /*心跳是给Ｔｃｐ的Linster的，检测它是否活着，
    通过给通信管道发心跳，来判断
    定时发固定格式HEARTBEAT|HEARTBEAT
    并且在HeartBeat中设置CountDownLatch
    如果linster收到HEARTBEAT，则活着,并且使HeartBeat的latch通过，使心跳重新定时发送


    若没收到就杀死现有的Linster,
    重新启动一个linster
    * */
    public static String HEARTBEAT="";
    private static String MESSAGE="HEARTBEAT|HEARTBEAT";
    private CountDownLatch latch=new CountDownLatch(1);
    public void countDown(){latch.countDown();}
    public void Heartbeat(String channel, JedisPubSub sub) throws InterruptedException {

        while (true){
            try{
                HEARTBEAT="";
                CountDownLatch latch=new CountDownLatch(1);
                Redis.publish(channel,MESSAGE);//channel 是队列名　　message 是数据
                latch.await(300, TimeUnit.MILLISECONDS);//等３００ｍｓ　没收到数据继续往下走ｉｆ　　　如果收到也继续往下走　　　３００是为了等数据来
                if (HEARTBEAT.equals("")){
                    if (sub!=null){
                        sub.unsubscribe();//销毁ｌｉｓtｅｎｅｒ
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            Jedis jedis=new Jedis("127.0.0.1",6379);
                            jedis.subscribe(sub,channel);//建立listener
                        }
                    }).start();
                }

            }catch (Exception e){
                Thread.sleep(500);//为了防止redis断掉，一旦断掉，走ｃａｔｃｈ，重新publish
                continue;
            }

        }
    }
}
