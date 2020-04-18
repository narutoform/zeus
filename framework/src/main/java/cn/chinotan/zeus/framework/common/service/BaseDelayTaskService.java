package cn.chinotan.zeus.framework.common.service;

import cn.chinotan.zeus.framework.common.vo.Task;

/**
 * 延时任务service
 *
 * @author xincheng
 * @date 2018-11-08
 */
public interface BaseDelayTaskService {

    /**
     * 发送延迟任务
     *
     * @param task
     */
    void sendDelayTask(Task<?> task);

}
