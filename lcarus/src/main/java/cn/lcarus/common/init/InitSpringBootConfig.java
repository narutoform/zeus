package cn.lcarus.common.init;

import cn.chinotan.zeus.config.constant.CommonConstant;
import cn.chinotan.zeus.framework.util.UUIDUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.lcarus.common.entity.Config;
import cn.lcarus.common.enums.ConfigEnum;
import cn.lcarus.common.service.BaseConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @description: SysCode系统初始化配置
 * @author: xingcheng
 * @create: 2019-08-23 14:04
 **/
@Slf4j
@Configuration
@ConditionalOnProperty(name = "guess.config.enable", havingValue = "true", matchIfMissing = false)
public class InitSpringBootConfig implements CommandLineRunner {

    public final static String AUTO_INIT_SYS_CODE_LOCK = "GUESS:AUTO_INIT_SYS_CODE_LOCK";

    @Value("${spring.profiles.active}")
    String profile;

    @Autowired
    private BaseConfigService baseConfigService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 码表初始化
     */
    @Override
    public void run(String... args) {
        log.info("InitSpringBootConfig - init config - profile:{}", profile);
        String uniqueId = UUIDUtil.getUuid();
        try {
            boolean lockResult = redisTemplate.opsForValue().setIfAbsent(AUTO_INIT_SYS_CODE_LOCK, uniqueId, CommonConstant.DEFAULT_5, TimeUnit.MINUTES);
            // 禁止重复执行
            if (!lockResult) {
                log.warn("InitSpringBootConfig - init config - not get lock");
                return;
            }
        } catch (Exception e) {
            log.error("InitSpringBootConfig - init config - tryLock lock Exception", e);
        }

        try {
            ConfigEnum[] configEnums = ConfigEnum.values();
            if (configEnums.length == 0) {
                return;
            }
            // 获取所有的配置
            List<Config> sysCodeList = baseConfigService.list();
            Set<String> existKeySet = sysCodeList.stream().map(Config::getKey).collect(Collectors.toSet());
            String key;
            String value;
            String description;
            Config configSave;
            for (int i = 0; i < configEnums.length; i++) {
                key = configEnums[i].key;
                value = configEnums[i].value;
                description = configEnums[i].desc;
                if (!existKeySet.contains(key)) {
                    configSave = new Config();
                    configSave.setKey(key);
                    configSave.setValue(value);
                    configSave.setDesc(description);
                    configSave.setEnable(true);
                    configSave.setCreateTime(DateUtil.date());
                    configSave.setUpdateTime(configSave.getCreateTime());
                    boolean save = baseConfigService.save(configSave);
                    if (save) {
                        log.info("InitSpringBootConfig init config {} success", key);
                    } else {
                        log.error("InitSpringBootConfig init config {} save fail", key);
                    }
                }
            }
        } catch (Throwable e) {
            log.error("InitSpringBootConfig init config Exception e:{}", e);
        } finally {
            try {
                String value = redisTemplate.opsForValue().get(AUTO_INIT_SYS_CODE_LOCK);
                if (Objects.equals(value, uniqueId)) {
                    Boolean delete = redisTemplate.delete(AUTO_INIT_SYS_CODE_LOCK);
                    log.info("InitSpringBootConfig - init config - unlock lock end time: {}, delete: {}", DateUtil.format(DateUtil.date(), DatePattern.NORM_DATETIME_MS_PATTERN), delete);
                }
            } catch (Exception e) {
                log.error("InitSpringBootConfig - init config - unlock lock Exception", e);
            }
        }
    }
}
