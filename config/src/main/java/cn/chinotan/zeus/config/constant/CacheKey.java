package cn.chinotan.zeus.config.constant;

/**
 * 可排序查询参数对象
 *
 * @author xingcheng
 * @since 2019-08-08
 */
public interface CacheKey {
    
    String SP = ":";

    String DI = ".";

    String DEFAULT_CACHE = "guess";

    /**
     * 抽奖活动
     */
    String ACTIVITY_LOTTERY = "activity:lottery";

    /**
     * 配置
     */
    String CONFIG = "config";

    /**
     * 三方sessionKey
     */
    String USER_SESSION_KEY = DEFAULT_CACHE + ":user:sessionKey:";
    
}
