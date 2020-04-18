package cn.lcarus.common.service;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.service.BaseService;
import cn.lcarus.admin.param.UserAccountDrawApproveQueryParam;
import cn.lcarus.admin.param.UserAccountDrawApproveSubmitParam;
import cn.lcarus.admin.res.UseAccountDrawApproveVo;
import cn.lcarus.common.entity.UserAccountDrawApprove;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 用户提现审核表 服务类
 *
 * @author xingcheng
 * @since 2020-04-05
 */
public interface BaseUserAccountDrawApproveService extends BaseService<UserAccountDrawApprove> {

    /**
     * 列表展示
     * @param param
     * @return
     */
    ApiResult<Page<UseAccountDrawApproveVo>> showList(UserAccountDrawApproveQueryParam param);

    /**
     * 审核操作
     * @param param
     * @return
     */
    ApiResult<?> approve(UserAccountDrawApproveSubmitParam param);
    
}
