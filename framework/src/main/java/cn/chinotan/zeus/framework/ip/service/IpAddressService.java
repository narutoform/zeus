package cn.chinotan.zeus.framework.ip.service;

import cn.chinotan.zeus.framework.common.service.BaseService;
import cn.chinotan.zeus.framework.ip.entity.IpAddress;

/**
 * IP地址 服务类
 *
 * @author xingcheng
 * @since 2020-03-25
 */
public interface IpAddressService extends BaseService<IpAddress> {

    /**
     * 通过ip地址获取IP对象
     *
     * @param ip
     * @return
     */
    IpAddress getByIp(String ip);

    /**
     * 通过ip地址获取区域
     *
     * @param ip
     * @return
     */
    String getAreaByIp(String ip);

    /**
     * 通过ip地址获取运营商
     *
     * @param ip
     * @return
     */
    String getOperatorByIp(String ip);

}
