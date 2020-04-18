package cn.chinotan.zeus.framework.log.service.impl;

import cn.chinotan.zeus.framework.common.service.impl.BaseServiceImpl;
import cn.chinotan.zeus.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.chinotan.zeus.framework.core.pagination.Paging;
import cn.chinotan.zeus.framework.log.entity.SysLoginLog;
import cn.chinotan.zeus.framework.log.mapper.SysLoginLogMapper;
import cn.chinotan.zeus.framework.log.param.SysLoginLogPageParam;
import cn.chinotan.zeus.framework.log.service.SysLoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统登录日志 服务实现类
 *
 * @author xingcheng
 * @since 2020-03-24
 */
@Slf4j
@Service
public class SysLoginLogServiceImpl extends BaseServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {

    @Autowired
    private SysLoginLogMapper sysLoginLogMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveSysLoginLog(SysLoginLog sysLoginLog) throws Exception {
        return super.save(sysLoginLog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateSysLoginLog(SysLoginLog sysLoginLog) throws Exception {
        return super.updateById(sysLoginLog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteSysLoginLog(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<SysLoginLog> getSysLoginLogPageList(SysLoginLogPageParam sysLoginLogPageParam) throws Exception {
        Page<SysLoginLog> page = new PageInfo<>(sysLoginLogPageParam,OrderItem.desc(getLambdaColumn(SysLoginLog::getCreateTime)));
        LambdaQueryWrapper<SysLoginLog> wrapper = new LambdaQueryWrapper<>();
        IPage<SysLoginLog> iPage = sysLoginLogMapper.selectPage(page, wrapper);
        return new Paging<SysLoginLog>(iPage);
    }

}
