package cn.chinotan.zeus.framework.common.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: lcarus
 * @description: 任务
 * @author: xingcheng
 * @create: 2020-03-06 20:17
 **/
@Data
@NoArgsConstructor
@Builder
public class Task<T> implements Serializable {
    
    private static final long serialVersionUID = -4847996542199644946L;

    /**
     * 类型
     */
    private String type;

    /**
     * 负载
     */
    private T payLoad;

    /**
     * 延时时间 未来时间戳 毫秒
     */
    private Long delayTime;

    public Task(T payLoad) {
        this.payLoad = payLoad;
    }

    public Task(T payLoad, Long delayTime) {
        this.payLoad = payLoad;
        this.delayTime = delayTime;
    }

    public Task(String type, T payLoad, Long delayTime) {
        this.type = type;
        this.payLoad = payLoad;
        this.delayTime = delayTime;
    }
}
