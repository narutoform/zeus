package cn.lcarus.common.service;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.service.BaseService;
import cn.lcarus.admin.param.EcpmAddParam;
import cn.lcarus.admin.param.EcpmListParam;
import cn.lcarus.admin.res.EcpmListVo;
import cn.lcarus.common.entity.Ecpm;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.math.BigDecimal;

/**
 * ecpm配置表 服务类
 *
 * @author xingcheng
 * @since 2020-03-04
 */
public interface BaseEcpmService extends BaseService<Ecpm> {

    BigDecimal DEFAULT_ECPM = BigDecimal.valueOf(23);

    Integer DEFAULT_HOUR = 10;

    /**
     * 列表展示
     * @param param
     * @return
     */
    ApiResult<Page<EcpmListVo>> showList(EcpmListParam param);

    /**
     * 添加
     * @param param
     * @return
     */
    ApiResult<Long> add(EcpmAddParam param);

    /**
     * 编辑
     * @param ecpmId
     * @param param
     * @return
     */
    ApiResult<?> edit(Long ecpmId, EcpmAddParam param);

    /**
     * 获取ecpm值
     * @return
     */
    BigDecimal getEcpmValue();
    
}
