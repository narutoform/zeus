package cn.chinotan.zeus.framework.common.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.chinotan.zeus.framework.common.service.BaseService;
import cn.chinotan.zeus.framework.util.LambdaColumn;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 公共Service父类
 *
 * @author xingcheng
 * @date 2018-11-08
 */
public abstract class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    /**
     * 实体类型
     */
    private Class<?> entityClass;

    {
        Class<?> clazz = this.getClass();
        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] p = ((ParameterizedType) type).getActualTypeArguments();
            this.entityClass = (Class<T>) p[1];
        }
    }

    /**
     * 获取对应字段的数据表列名称
     *
     * @param func
     * @return
     */
    public String getLambdaColumn(SFunction<T, ?> func) {
        return new LambdaColumn<T>().get(func);
    }


}
