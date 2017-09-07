package util.Redis;

/**
 * Created by qin on 17-9-7.
 */
public class RedisMessage {
    public static String getHead(String str){
        String[] s=str.split("\\|");
        return s[0];
    }
    public static String getBody(String str){
        String[] s=str.split("\\|");
        return s[1];
    }

}
