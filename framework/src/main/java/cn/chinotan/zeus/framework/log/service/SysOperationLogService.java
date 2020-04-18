package cn.chinotan.zeus.framework.log.service;

import cn.chinotan.zeus.framework.common.service.BaseService;
import cn.chinotan.zeus.framework.core.pagination.Paging;
import cn.chinotan.zeus.framework.log.entity.SysOperationLog;
import cn.chinotan.zeus.framework.log.param.SysOperationLogPageParam;

/**
 * 系统操作日志 服务类
 *
 * @author xingcheng
 * @since 2020-03-19
 */
public interface SysOperationLogService extends BaseService<SysOperationLog> {

    /**
     * 保存
     *
     * @param sysOperationLog
     * @return
     * @throws Exception
     */
    boolean saveSysOperationLog(SysOperationLog sysOperationLog) throws Exception;

    /**
     * 修改
     *
     * @param sysOperationLog
     * @return
     * @throws Exception
     */
    boolean updateSysOperationLog(SysOperationLog sysOperationLog) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteSysOperationLog(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param sysOperationLogQueryParam
     * @return
     * @throws Exception
     */
    Paging<SysOperationLog> getSysOperationLogPageList(SysOperationLogPageParam sysOperationLogPageParam) throws Exception;

}
