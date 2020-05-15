package com.brilliant.fury.core.base;

/**
 * 因为依赖Redis集群，所以需要对TaskCoordinater有自行的实现
 *
 * 例如：
 * @Resource private JedisPool jedisPool;
 *
 * public boolean isFirst(String key, int expireAt) {
 * try (Jedis jedis = jedisPool.getResource()) {
 *      long first = jedis.incrBy(key, 1);
 *      if (first == 1) {
 *          jedis.expire(key, expireAt);
 *          return true;
 *      } else {
 *          return false;
 *      }
 * }
 * @author fury.
 * version 2017/9/8.
 */
@FunctionalInterface
public interface TaskCoordinater {

    boolean isFirst(String key, int expireAt);

}
