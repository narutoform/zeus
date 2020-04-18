package cn.chinotan.zeus.framework.ip.mapper;

import cn.chinotan.zeus.framework.ip.entity.IpAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * IP地址 Mapper 接口
 *
 * @author xingcheng
 * @since 2020-03-25
 */
@Repository
public interface IpAddressMapper extends BaseMapper<IpAddress> {

    /**
     * 通过ip地址获取IP对象
     *
     * @param ip
     * @return
     */
    IpAddress getByIp(@Param("ip") String ip);

}
