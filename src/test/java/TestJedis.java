import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import utils.JedisPoolUtil;

/**
 * @author yhw
 * @version 1.0
 **/
public class TestJedis {

    //测试一下能不能连接上redis服务器
    @Test
    public void test1(){
        JedisPool jedisPool = JedisPoolUtil.getJedisPoolInstance();
        Jedis jedis = jedisPool.getResource();
        System.out.println(        jedis.ping());
    }
}
