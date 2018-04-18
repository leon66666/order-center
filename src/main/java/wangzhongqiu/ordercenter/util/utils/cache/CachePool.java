package wangzhongqiu.ordercenter.util.utils.cache;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *
 */
public class CachePool {

    JedisPool pool;
    private static final CachePool cachePool = new CachePool();

    public static CachePool getInstance() {
        return cachePool;
    }

    private CachePool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(1024);
        config.setMaxIdle(200);
        config.setMaxWaitMillis(1000l);
        PropertiesConfiguration redisConfig = null;
        try {
            redisConfig = new PropertiesConfiguration("config-order.properties");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        String url = redisConfig.getString("redis.url");
        int port = Integer.parseInt(redisConfig.getString("redis.port"));
        String password = redisConfig.getString("redis.auth");
        int timeout = Integer.parseInt(redisConfig.getString("redis.timeout"));
        //String url = ResourceBundle.getBundle("config.properties").getString("pay.redis.url");
        //String url = "10.13.215.227";

        pool = new JedisPool(config, url, port, timeout, password);
    }


    public Jedis getJedis() {
        Jedis jedis = pool.getResource(); //Exception交由具体调用者去catch
        return jedis;
    }

    public JedisPool getJedisPool() {
        return this.pool;
    }
}
