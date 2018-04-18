package wangzhongqiu.ordercenter.util.utils.cache;

import org.apache.ibatis.cache.Cache;
import org.perf4j.aop.Profiled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MybatisRedisCache implements Cache {
    private static final Logger log = LoggerFactory.getLogger(MybatisRedisCache.class);
    /**
     * The ReadWriteLock.
     */
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private String id;//namespace

    public MybatisRedisCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("必须传入ID");
        }
        log.debug("MybatisRedisCache:id=" + id);
        this.id = id;
    }

    /**
     * 1. 获取Jedis实例需要从JedisPool中获取
     * 2. 用完Jedis实例需要还给JedisPool
     * 3. 如果Jedis在使用过程中出错，则也需要还给JedisPool
     */

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int getSize() {
        Jedis jedis = null;
        JedisPool jedisPool = null;
        int result = 0;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = CachePool.getInstance().getJedis();
            jedisPool = CachePool.getInstance().getJedisPool();
            result = Integer.valueOf(jedis.dbSize().toString());
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null)
                jedisPool.returnBrokenResource(jedis); //释放redis对象,A Jedis object represents a connection to Redis. It becomes unusable when the physical connection is broken, or when the synchronization between the client and server is lost.
        } finally {
            if (borrowOrOprSuccess)
                jedisPool.returnResource(jedis); //返还到连接池,应该放在finally块中，否则每次发生异常将导致一个jedis对象没有被回收
        }
        return result;

    }
    @Profiled(el = true, logger = "rediesTimingLogger", tag = "MybatisRedisCache_putObject", timeThreshold = 10, normalAndSlowSuffixesEnabled = true)
    @Override
    public void putObject(Object key, Object value) {
        //转换KEY，从SQL转换为好记的唯一标识，目前考虑适用namespace代替，为了刷新缓存方便
        //key = this.id;
        if (log.isDebugEnabled())
            log.debug("putObject:" + key.hashCode() + "=" + value);
        if (log.isDebugEnabled())
            log.debug("put to redis sql :" + key.toString());
        Jedis jedis = null;
        JedisPool jedisPool = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = CachePool.getInstance().getJedis();
            jedisPool = CachePool.getInstance().getJedisPool();
            jedis.setex(SerializeUtil.serialize(key.hashCode()), 60, SerializeUtil.serialize(value));
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null)
                jedisPool.returnBrokenResource(jedis);
        } finally {
            if (borrowOrOprSuccess)
                jedisPool.returnResource(jedis);
        }

    }

    @Profiled(el = true, logger = "rediesTimingLogger", tag = "redies_getObject", timeThreshold = 10, normalAndSlowSuffixesEnabled = true)
    @Override
    public Object getObject(Object key) {
        //转换KEY，从SQL转换为好记的唯一标识，目前考虑适用namespace代替，为了刷新缓存方便
//        key = this.id;
        Jedis jedis = null;
        JedisPool jedisPool = null;
        Object value = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = CachePool.getInstance().getJedis();
            jedisPool = CachePool.getInstance().getJedisPool();
            value = SerializeUtil.unserialize(jedis.get(SerializeUtil.serialize(key.hashCode())));
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null)
                jedisPool.returnBrokenResource(jedis);
        } finally {
            if (borrowOrOprSuccess)
                jedisPool.returnResource(jedis);
        }
        if (log.isDebugEnabled())
            log.debug("getObject:" + key.hashCode() + "=" + value);
        return value;
    }

    @Override
    public Object removeObject(Object key) {
        Jedis jedis = null;
        JedisPool jedisPool = null;
        Object value = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = CachePool.getInstance().getJedis();
            jedisPool = CachePool.getInstance().getJedisPool();
            value = jedis.expire(SerializeUtil.serialize(key.hashCode()), 0);
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null)
                jedisPool.returnBrokenResource(jedis);
        } finally {
            if (borrowOrOprSuccess)
                jedisPool.returnResource(jedis);
        }
        if (log.isDebugEnabled())
            log.debug("getObject:" + key.hashCode() + "=" + value);
        return value;
    }

    @Override
    public void clear() {
        Jedis jedis = null;
        JedisPool jedisPool = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = CachePool.getInstance().getJedis();
            jedisPool = CachePool.getInstance().getJedisPool();
            jedis.flushDB();
            jedis.flushAll();
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null)
                jedisPool.returnBrokenResource(jedis);
        } finally {
            if (borrowOrOprSuccess)
                jedisPool.returnResource(jedis);
        }
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

}
