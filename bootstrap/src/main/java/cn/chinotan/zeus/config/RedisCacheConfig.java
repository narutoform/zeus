package cn.chinotan.zeus.config;

import cn.chinotan.zeus.config.constant.CacheKey;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.*;

/**
 * <p>
 *     Redis Cache配置
 * </p>
 * @author xingcheng
 * @date 2018-11-08
 */
@Configuration
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuffer redisKey = new StringBuffer();
            redisKey.append(target.getClass().getName()).append("-");
            redisKey.append(method.getName());
//            if (params.length > 0) {
//                redisKey.append("-").append(Arrays.deepToString(params));
//            }
            return redisKey.toString();
        };
    }


    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        // 生成一个默认配置，通过config对象即可对缓存进行自定义配置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        // 设置缓存的默认过期时间，也是使用Duration设置
        // 过期时间5分钟
        config = config.entryTtl(Duration.ofMinutes(5));

        // 设置一个初始化的缓存空间set集合
        Set<String> cacheNames = new HashSet<>();
        cacheNames.add(CacheKey.ACTIVITY_LOTTERY);
        cacheNames.add(CacheKey.DEFAULT_CACHE);
        cacheNames.add(CacheKey.CONFIG);

        // 对每个缓存空间应用不同的配置
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        configMap.put(CacheKey.ACTIVITY_LOTTERY, config);
        configMap.put(CacheKey.CONFIG, config);
        configMap.put(CacheKey.DEFAULT_CACHE, config.entryTtl(Duration.ofMinutes(60)));


        // 使用自定义的缓存配置初始化一个cacheManager
        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                // 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
                .initialCacheNames(cacheNames)
                .withInitialCacheConfigurations(configMap)
                .build();

        return cacheManager;
    }

}
