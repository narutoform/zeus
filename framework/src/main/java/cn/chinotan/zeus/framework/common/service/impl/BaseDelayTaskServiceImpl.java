package cn.chinotan.zeus.framework.common.service.impl;

import cn.chinotan.zeus.config.constant.CommonConstant;
import cn.chinotan.zeus.framework.common.service.BaseDelayTaskService;
import cn.chinotan.zeus.framework.common.vo.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 延时任务service
 *
 * @author xingcheng
 * @date 2018-11-08
 */
@Service
public class BaseDelayTaskServiceImpl implements BaseDelayTaskService {

    @Autowired
    private RedisTemplate<String, Task> redisTaskTemplate;

    @Override
    public void sendDelayTask(Task<?> task) {
        redisTaskTemplate.opsForZSet().add(CommonConstant.REDIS_DELAY, task, task.getDelayTime() / 1000);
    }

}
