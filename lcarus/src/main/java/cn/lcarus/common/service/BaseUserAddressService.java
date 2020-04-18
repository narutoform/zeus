package cn.lcarus.common.service;

import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.service.BaseService;
import cn.lcarus.common.entity.UserAddress;
import cn.lcarus.guess.web.param.LoginUserVo;
import cn.lcarus.guess.web.param.UserAddressAddParam;
import cn.lcarus.guess.web.res.UserAddressListRes;

import java.util.List;

/**
 * 用户地址表 服务类
 *
 * @author xingcheng
 * @since 2020-03-08
 */
public interface BaseUserAddressService extends BaseService<UserAddress> {

    /**
     * 获取用户地址列表
     *
     * @param userId
     * @return
     */
    List<UserAddress> getUserAddressList(Long userId);

    /**
     * 获取并检测用户地址
     *
     * @param userId
     * @param userAddressId
     * @return
     */
    UserAddress getAndCheckUserAddress(Long userId, Long userAddressId);

    /**
     * 获取唯一地址信息
     *
     * @param userId
     * @param consigneeName
     * @param provinceName
     * @param cityName
     * @param countyName
     * @param detailInfo
     * @param telNumber
     * @return
     */
    UserAddress getUserAddress(Long userId, String consigneeName, String provinceName, String cityName, String countyName, String detailInfo, String telNumber);

    /**
     * 展示用户地址信息
     *
     * @param param
     * @return
     */
    ApiResult<UserAddressListRes> showList(LoginUserVo param);

    /**
     * 添加收货地址
     *
     * @param param
     * @return
     */
    ApiResult<Long> add(UserAddressAddParam param);
}
