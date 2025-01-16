package org.yg.mallchat.common.common.constant;

/**
 * @author yangang
 * @create 2025-01-15-下午6:22
 */
public class RedisKey {
    private static final String BASE_KEY = "mallchat:chat";
    /**
     * 用户token的key
     */
    public static final String USER_TOKEN_STRING = "userToken:uid_%d";

    public static String getKey(String key, Object... o){
        return BASE_KEY + String.format(key, o);
    }
}
