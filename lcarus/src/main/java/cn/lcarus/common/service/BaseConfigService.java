package cn.lcarus.common.service;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.service.BaseService;
import cn.lcarus.admin.param.ConfigUpdateParam;
import cn.lcarus.common.entity.Config;

import java.util.Date;

/**
 * 配置表 服务类
 *
 * @author xingcheng
 * @since 2020-04-01
 */
public interface BaseConfigService extends BaseService<Config> {

    /**
     * 根据key获取Config
     *
     * @param key
     * @return
     */
    Config getByKey(String key);

    /**
     * (根据key获取 value值)
     *
     * @param key
     * @return
     */
    String getValue(String key);

    /**
     * (获取long类型数据)
     *
     * @param key
     * @return
     */
    Long getLongValue(String key);

    /**
     * (获取Boolean类型数据)
     *
     * @param key
     * @return
     */
    Boolean getBooleanValue(String key);

    /**
     * (获取Integer类型数据)
     *
     * @param key
     * @return
     */
    Integer getIntegerValue(String key);

    /**
     * (获取Date类型数据)
     *
     * @param key
     * @return
     */
    Date getDateValue(String key);

    /**
     * 更新
     * @param param
     * @return
     */
    ApiResult<?> updateConfig(ConfigUpdateParam param);

    /**
     * get
     * @param key
     * @return
     */
    Config getConfig(String key);
}
