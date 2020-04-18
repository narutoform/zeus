package cn.lcarus.common.service;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.service.BaseService;
import cn.lcarus.admin.param.UserQueryParam;
import cn.lcarus.admin.res.UserInfoVo;
import cn.lcarus.common.entity.User;
import cn.lcarus.guess.web.param.LoginUserVo;
import cn.lcarus.guess.web.param.UserAddMobileParam;
import cn.lcarus.guess.web.res.UserSelfCenterInfoRes;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author xingcheng
 * @since 2019-08-24
 */
public interface BaseUserService extends BaseService<User> {

    /**
     * 查询用户信息
     *
     * @param param
     * @return
     */
    ApiResult<UserSelfCenterInfoRes> showSelfCenterInfo(LoginUserVo param);

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    User getUser(Long userId);

    /**
     * 添加手机号
     *
     * @param param
     * @return
     */
    ApiResult<?> addMobile(UserAddMobileParam param);

    /**
     * 用户列表查询
     * @param param
     * @return
     */
    ApiResult<Page<UserInfoVo>> showList(UserQueryParam param);

    /**
     * 通过手机号获取用户id列表
     * @param mobile
     * @return
     */
    List<User> getUserByMobile(String mobile);
}
