package cn.lcarus.common.service.impl;

import cn.chinotan.zeus.config.constant.CacheKey;
import cn.chinotan.zeus.framework.common.api.ApiCode;
import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.service.impl.BaseServiceImpl;
import cn.chinotan.zeus.redislock.annotation.DistributeLock;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.lcarus.admin.param.ConfigUpdateParam;
import cn.lcarus.common.entity.Config;
import cn.lcarus.common.mapper.ConfigMapper;
import cn.lcarus.common.service.BaseConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * 配置表 服务实现类
 *
 * @author xingcheng
 * @since 2020-04-01
 */
@Service
@Slf4j
public class BaseConfigServiceImpl extends BaseServiceImpl<ConfigMapper, Config> implements BaseConfigService {

    @Autowired
    @Lazy
    private BaseConfigService baseConfigService;

    @Override
    @Cacheable(value = CacheKey.CONFIG, key = "#key", unless = "#result == null")
    public Config getByKey(String key) {
        Assert.notBlank(key, "key");
        return lambdaQuery()
                .eq(Config::getKey, key)
                .eq(Config::getEnable, true)
                .one();
    }

    @Override
    public String getValue(String key) {
        Config config = baseConfigService.getByKey(key);
        if (config == null) {
            return null;
        }
        return config.getValue();
    }

    @Override
    public Long getLongValue(String key) {
        Config config = baseConfigService.getByKey(key);
        if (config == null) {
            return null;
        }
        return Long.valueOf(config.getValue());
    }

    @Override
    public Boolean getBooleanValue(String key) {
        Config config = baseConfigService.getByKey(key);
        if (config == null) {
            return null;
        }
        return Boolean.valueOf(config.getValue());
    }

    @Override
    public Integer getIntegerValue(String key) {
        Config config = baseConfigService.getByKey(key);
        if (config == null) {
            return null;
        }
        return Integer.valueOf(config.getValue());
    }

    @Override
    public Date getDateValue(String key) {
        Config Config = baseConfigService.getByKey(key);
        if (Config == null) {
            return null;
        }
        return DateUtil.parse(Config.getValue(), DatePattern.NORM_DATETIME_PATTERN);
    }

    @Override
    @DistributeLock(value = "CONFIG:UPDATE", expire = 360, timeout = 0, errMsg = "正在更新config中，请不要频繁点击哦！")
    @CacheEvict(value = CacheKey.CONFIG, key = "#param.key")
    public ApiResult<?> updateConfig(ConfigUpdateParam param) {
        String key = param.getKey();
        Config config = getConfig(key);
        if (Objects.isNull(config)) {
            return ApiResult.fail(ApiCode.CODE_100500);
        }
        Config configUpdate = BeanUtil.toBean(param, Config.class);
        configUpdate.setId(config.getId());
        configUpdate.setVersion(config.getVersion());
        configUpdate.setUpdateTime(DateUtil.date());
        boolean update = updateById(configUpdate);
        Assert.isTrue(update);
        return ApiResult.ok();
    }

    @Override
    public Config getConfig(String key) {
        Assert.notBlank(key, "key");
        return lambdaQuery()
                .eq(Config::getKey, key)
                .one();
    }

}
