package utils;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


//jedis连接池
public class JedisPoolUtil {
	private static volatile JedisPool jedisPool = null;

	private JedisPoolUtil() {
	}

	public static JedisPool getJedisPoolInstance() {
		if (null == jedisPool) {
			synchronized (JedisPoolUtil.class) {
				if (null == jedisPool) {
					JedisPoolConfig poolConfig = new JedisPoolConfig();
					poolConfig.setMaxTotal(200);
					poolConfig.setMaxIdle(32);
					poolConfig.setMaxWaitMillis(100*1000);
					poolConfig.setBlockWhenExhausted(true);
					poolConfig.setTestOnBorrow(true);  // ping  PONG

					jedisPool = new JedisPool(poolConfig, "192.168.174.136", 6379, 6000 );
				}
			}
		}
		return jedisPool;
	}

}