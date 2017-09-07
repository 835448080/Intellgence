package netty.Http;

import io.netty.channel.*;
import io.netty.util.concurrent.EventExecutorGroup;
import redis.SJedisPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

import java.util.List;
import java.util.Set;

/**
 * Created by qin on 17-8-29.
 */
public class HttpInboundHandler extends ChannelHandlerAdapter {//最原始的handler

    ShardedJedis shardedJedis = null;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String[] message=((String) msg).split("\\|");
        if("SCAN".equals(message[0])){// SCAN|uid,lockid
            String[] id=message[1].split(",");
            Jedis jedis=new Jedis("127.0.0.1",6379);

//            if (shardedJedis.exists(id[0])){
//                String money=shardedJedis.hget("shopping",id[1]);
//                shardedJedis.hset(id[0],id[1],money);//uid,lockid,money
//
//            }else {
//                String money=shardedJedis.hget("shopping",id[1]);
//                shardedJedis.hset(id[0],id[1],money);
//            }
            String money=shardedJedis.hget("shopping",id[1]);
            shardedJedis.hset(id[0],id[1],money);
        }
        if ("PAY".equals(message[0])){//PAY|uid
//            String[] id=message[1].split(",");
            List<String> moneyall=shardedJedis.hvals(message[1]);
//            for (int i=0;i<moneyall.size();i++){
//            }
            int sum = 0;
            for(String str:moneyall){
                sum += Integer.parseInt(str);
            }
            int sum1=50;//支付宝订单的总金额
            if (sum==sum1){

            }else {
                Jedis jedis=new Jedis("127.0.0.1",6379);
                Set<String> lockidall=shardedJedis.hkeys(message[1]);//set不能重复，且不顺序
//                jedis.publish(lockidall);

            }

        }
//        ctx.writeAndFlush(结果);
    }

}
