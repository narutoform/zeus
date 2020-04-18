package cn.lcarus.common.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.chinotan.zeus.framework.common.api.ApiCode;
import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.service.impl.BaseServiceImpl;
import cn.chinotan.zeus.framework.util.ValidatorUtils;
import cn.chinotan.zeus.redislock.annotation.DistributeLock;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.lcarus.admin.param.UserQueryParam;
import cn.lcarus.admin.res.UserInfoVo;
import cn.lcarus.common.entity.User;
import cn.lcarus.common.entity.UserAccount;
import cn.lcarus.common.entity.UserThirdInfo;
import cn.lcarus.common.mapper.UserMapper;
import cn.lcarus.common.service.*;
import cn.lcarus.guess.web.param.LoginUserVo;
import cn.lcarus.guess.web.param.UserAddMobileParam;
import cn.lcarus.guess.web.res.UserSelfCenterInfoRes;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author xingcheng
 * @since 2019-08-24
 */
@Slf4j
@Service
public class BaseUserServiceImpl extends BaseServiceImpl<UserMapper, User> implements BaseUserService {

    @Autowired
    @Lazy
    private BaseUserService baseUserService;

    @Autowired
    private BaseUserThirdInfoService baseUserThirdInfoService;

    @Autowired
    private BaseUserLotteryRecordService baseUserLotteryRecordService;

    @Autowired
    protected WxMaUserService wxMaUserService;
    
    @Autowired
    private BaseUserAccountService baseUserAccountService;
    
    @Autowired
    private BaseUserAccountRecordService baseUserAccountRecordService;

    @Value("${wx.miniapp.appid}")
    private String miniAppAppId;

    @Value("${wx.mp.appId}")
    private String mpAppId;

    @Override
    public ApiResult<UserSelfCenterInfoRes> showSelfCenterInfo(LoginUserVo param) {
        Long userId = param.getUserId();
        Assert.notNull(userId, "userId");
        // 查询用户基本信息
        User user = baseUserService.getUser(userId);
        if (Objects.isNull(user)) {
            return ApiResult.fail(ApiCode.ERROR_6400);
        }
        UserSelfCenterInfoRes userSelfCenterInfoRes = new UserSelfCenterInfoRes();
        if (StrUtil.isNotBlank(user.getMobile())) {
            userSelfCenterInfoRes.setHasMobileFlag(true);
        }
        // 查询用户三方信息
        UserThirdInfo userThirdInfo = baseUserThirdInfoService.getUserThirdInfo(userId, miniAppAppId);
        BeanUtil.copyProperties(userThirdInfo, userSelfCenterInfoRes);
        // 抽奖记录
        Integer joinNum = baseUserLotteryRecordService.countUserJoin(userId);
        Integer winNum = baseUserLotteryRecordService.countUserWin(userId);
        userSelfCenterInfoRes.setJoinNum(joinNum);
        userSelfCenterInfoRes.setWinNum(winNum);
        return ApiResult.ok(userSelfCenterInfoRes);
    }

    @Override
    public User getUser(Long userId) {
        Assert.notNull(userId, "userId");
        return getById(userId);
    }

    @Override
    @DistributeLock(value = "USER:ADD_MOBILE", key = "#param.userId", expire = 360, timeout = 0, errMsg = "正在添加手机号中，请不要频繁点击哦！")
    public ApiResult<?> addMobile(UserAddMobileParam param) {
        ValidatorUtils.validateModel(param);
        String appId = param.getAppId();
        Long userId = param.getUserId();
        Assert.notNull(ApiErrorCode.FAILED, appId, userId);
        if (Objects.equals(appId, miniAppAppId)) {
            // 小程序
            String sessionKey = baseUserThirdInfoService.getMiNiAppUserSessionKey(userId, appId);
            if (StrUtil.isBlank(sessionKey) || StrUtil.isBlank(param.getIv()) || StrUtil.isBlank(param.getEncryptedData())) {
                return ApiResult.fail(ApiCode.ERROR_6411);
            }
            WxMaPhoneNumberInfo phoneNoInfo = wxMaUserService.getPhoneNoInfo(sessionKey, param.getEncryptedData(), param.getIv());
            String purePhoneNumber = phoneNoInfo.getPurePhoneNumber();
            if (StrUtil.isBlank(purePhoneNumber)) {
                return ApiResult.fail(ApiCode.ERROR_6412);
            }
            User user = baseUserService.getUser(userId);
            if (Objects.isNull(user)) {
                return ApiResult.fail(ApiCode.ERROR_6400);
            }
            User userUpdate = new User();
            userUpdate.setId(user.getId());
            userUpdate.setMobile(purePhoneNumber);
            userUpdate.setUpdateTime(DateUtil.date());
            boolean update = updateById(userUpdate);
            Assert.isTrue(update);
            return ApiResult.ok();
        } else {
            return ApiResult.fail(ApiCode.ERROR_6404);
        }
    }

    @Override
    public ApiResult<Page<UserInfoVo>> showList(UserQueryParam param) {
        IPage<User> page = lambdaQuery()
                .eq(StrUtil.isNotBlank(param.getMobile()), User::getMobile, param.getMobile())
                .ge(Objects.nonNull(param.getRegStart()), User::getCreateTime, param.getRegStart())
                .le(Objects.nonNull(param.getRegEnd()), User::getCreateTime, param.getRegEnd())
                .le(Objects.nonNull(param.getUserId()), User::getId, param.getUserId())
                .page(param);
        List<User> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return ApiResult.ok();
        }
        List<UserInfoVo> collect = records
                .stream()
                .map(v -> {
                    Long userId = v.getId();
                    UserInfoVo userInfoVo = BeanUtil.toBean(v, UserInfoVo.class);
                    userInfoVo.setUserId(userId);
                    // 查询三方用户信息库
                    UserThirdInfo userThirdInfo = baseUserThirdInfoService.getUserThirdInfo(userId, mpAppId);
                    if (Objects.nonNull(userThirdInfo)) {
                        userInfoVo.setNickName(userThirdInfo.getNickName());
                    }
                    // 查询用户抽奖信息
                    Integer joinNum = baseUserLotteryRecordService.countUserJoin(userId);
                    userInfoVo.setJoinNum(joinNum);
                    Integer wimNum = baseUserLotteryRecordService.countUserWin(userId);
                    userInfoVo.setWimNum(wimNum);
                    // 查询用户账户信息
                    UserAccount userAccount = baseUserAccountService.getUserAccount(userId);
                    if (Objects.nonNull(userAccount)) {
                        userInfoVo.setAccountAmount(userAccount.getAmount());
                    }
                    BigDecimal userWithDrewAccountAmount = baseUserAccountRecordService.getUserWithDrewAccountAmount(userId);
                    userInfoVo.setWithDrewAmount(userWithDrewAccountAmount);
                    return userInfoVo;
                })
                .collect(Collectors.toList());
        Page<UserInfoVo> pageRes = new Page<>();
        pageRes.setTotal(page.getTotal());
        pageRes.setRecords(collect);
        return ApiResult.ok(pageRes);
    }

    @Override
    public List<User> getUserByMobile(String mobile) {
        if (StrUtil.isBlank(mobile)) {
            return Collections.emptyList();
        }
        return lambdaQuery()
                .eq(User::getMobile, mobile)
                .list();
    }
}
