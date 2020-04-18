package cn.lcarus.common.service.impl;

import cn.chinotan.zeus.framework.common.api.ApiCode;
import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.service.impl.BaseServiceImpl;
import cn.chinotan.zeus.framework.util.ValidatorUtils;
import cn.chinotan.zeus.redislock.annotation.DistributeLock;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.lcarus.common.entity.UserAddress;
import cn.lcarus.common.mapper.UserAddressMapper;
import cn.lcarus.common.service.BaseUserAddressService;
import cn.lcarus.guess.web.param.LoginUserVo;
import cn.lcarus.guess.web.param.UserAddressAddParam;
import cn.lcarus.guess.web.res.UserAddressListRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户地址表 服务实现类
 *
 * @author xingcheng
 * @since 2020-03-08
 */
@Service
@Slf4j
public class BaseUserAddressServiceImpl extends BaseServiceImpl<UserAddressMapper, UserAddress> implements BaseUserAddressService {

    @Override
    public List<UserAddress> getUserAddressList(Long userId) {
        Assert.notNull(userId, "userId");
        return lambdaQuery()
                .eq(UserAddress::getUserId, userId)
                .list();
    }

    @Override
    public UserAddress getAndCheckUserAddress(Long userId, Long userAddressId) {
        Assert.notNull(userId, "userId");
        Assert.notNull(userAddressId, "userAddressId");
        return lambdaQuery()
                .eq(UserAddress::getUserId, userId)
                .eq(UserAddress::getId, userAddressId)
                .one();
    }

    @Override
    public UserAddress getUserAddress(Long userId, String consigneeName, String provinceName, String cityName, String countyName, String detailInfo, String telNumber) {
        Assert.notNull(userId, "userId");
        Assert.notBlank(consigneeName, "consigneeName");
        Assert.notBlank(provinceName, "provinceName");
        Assert.notBlank(cityName, "cityName");
        Assert.notBlank(countyName, "countyName");
        Assert.notBlank(detailInfo, "detailInfo");
        Assert.notBlank(telNumber, "telNumber");
        return lambdaQuery()
                .eq(UserAddress::getTelNumber, telNumber)
                .eq(UserAddress::getConsigneeName, consigneeName)
                .eq(UserAddress::getProvinceName, provinceName)
                .eq(UserAddress::getCityName, cityName)
                .eq(UserAddress::getCountyName, countyName)
                .eq(UserAddress::getDetailInfo, detailInfo)
                .eq(UserAddress::getUserId, userId)
                .one();
    }

    @Override
    public ApiResult<UserAddressListRes> showList(LoginUserVo param) {
        Long userId = param.getUserId();
        Assert.notNull(userId, "userId");
        List<UserAddress> userAddressList = getUserAddressList(userId);
        if (CollUtil.isEmpty(userAddressList)) {
            return ApiResult.ok(Collections.EMPTY_LIST);
        }
        List<UserAddressListRes> collect = userAddressList
                .stream()
                .map(v -> {
                    UserAddressListRes userAddressListRes = BeanUtil.toBean(v, UserAddressListRes.class);
                    return userAddressListRes;
                })
                .collect(Collectors.toList());
        return ApiResult.ok(collect);
    }

    @Override
    @DistributeLock(value = "USER_ADDRESS:ADD", key = "#param.userId", expire = 360, timeout = 0, errMsg = "正在保存地址中，请不要频繁点击哦！")
    public ApiResult<Long> add(UserAddressAddParam param) {
        ValidatorUtils.validateModel(param);
        Long userAddressId = param.getUserAddressId();
        Long userId = param.getUserId();
        UserAddress userAddressOpt = BeanUtil.toBean(param, UserAddress.class);
        DateTime now = DateUtil.date();
        if (Objects.isNull(userAddressId)) {
            // 判断该地址是否保存过
            UserAddress userAddress = getUserAddress(userId, param.getConsigneeName(), param.getProvinceName(), param.getCityName(), param.getCountyName(), param.getDetailInfo(), param.getTelNumber());
            if (Objects.isNull(userAddress)) {
                // 保存
                userAddressOpt.setCreateTime(now);
                userAddressOpt.setUpdateTime(now);
                boolean save = save(userAddressOpt);
                Assert.isTrue(save);
                userAddressId = userAddressOpt.getId();
            } else {
                userAddressId = userAddress.getId();
            }
        } else {
            // 编辑
            // 查询是否存在
            UserAddress userAddressDb = getAndCheckUserAddress(userId, userAddressId);
            if (Objects.isNull(userAddressDb)) {
                return ApiResult.fail(ApiCode.ERROR_6500);
            }
            userAddressOpt.setId(userAddressDb.getId());
            userAddressOpt.setVersion(userAddressDb.getVersion());
            userAddressOpt.setUpdateTime(now);
            boolean update = updateById(userAddressOpt);
            Assert.isTrue(update);
        }
        return ApiResult.ok(userAddressId);
    }

}
