package in.bugr;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;


/**
 * @author BugRui
 * @date 2020/3/28 下午1:26
 **/
public interface CacheHelper<K, V> {
    /**
     * 实现此接口使用cache
     *
     * @return
     */
    RedisTemplate<K, V> redisTemplate();

    /**
     * 存入缓存
     *
     * @param key
     * @param value
     * @return V
     */
    default V set(K key, V value) {
        ValueOperations<K, V> valueOperations = redisTemplate().opsForValue();
        valueOperations.set(key, value);
        return value;

    }

    default V set(K key, V value, Long expire) {
        set(key, value);
        redisTemplate().expire(key, expire, TimeUnit.SECONDS);
        return value;
    }

    default V get(final K key) {
        ValueOperations<K, V> valueOperations = redisTemplate().opsForValue();
        return valueOperations.get(key);
    }

    default boolean exists(final K key) {
        return BooleanUtils.toBoolean(redisTemplate().hasKey(key));
    }

    default boolean remove(final K key) {
        if (exists(key)) {
            return BooleanUtils.toBoolean(redisTemplate().delete(key));
        } else {
            return true;
        }
    }

    default void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }
}
