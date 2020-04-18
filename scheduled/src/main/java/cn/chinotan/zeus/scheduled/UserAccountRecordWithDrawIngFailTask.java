package cn.chinotan.zeus.scheduled;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.lcarus.common.entity.UserAccountRecord;
import cn.lcarus.common.enums.UserAccountRecordStateEnum;
import cn.lcarus.common.service.BaseUserAccountRecordService;
import cn.lcarus.common.service.BaseUserAccountService;
import cn.lcarus.common.service.BaseWxService;
import com.alibaba.fastjson.JSON;
import com.github.binarywang.wxpay.service.RedpackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: lcarus
 * @description: 用户抽奖记录提现 进行中（需等待）定时补偿
 * @author: xingcheng
 * @create: 2020-03-14 17:56
 **/
@Slf4j
@Component
public class UserAccountRecordWithDrawIngFailTask {

    @Autowired
    private BaseWxService baseWxService;

    @Autowired
    private RedpackService redpackService;

    @Autowired
    private BaseUserAccountRecordService userAccountRecordService;

    @Autowired
    private BaseUserAccountService userAccountService;

    @Value("${wx.mp.appId}")
    private String mpAppId;

    /**
     * 每3分支查询一次
     */
    @Scheduled(cron = "0 0/3 * * * ? ")
    public void userAccountRecordWithDrawIngFailTask() {
        List<UserAccountRecord> userAccountRecordList = userAccountRecordService
                .lambdaQuery()
                .gt(UserAccountRecord::getCreateTime, DateUtil.offsetDay(DateUtil.date(), -1))
                .eq(UserAccountRecord::getState, UserAccountRecordStateEnum.ING_FAIL.getCode())
                .list();
        if (CollUtil.isEmpty(userAccountRecordList)) {
            return;
        }
        for (UserAccountRecord userAccountRecord : userAccountRecordList) {
            try {
                baseWxService.reSendRealAmount(userAccountRecord);
            } catch (Exception e) {
                log.error("UserAccountRecordWithDrawIngFailTask-Exception userAccountRecord: {}", JSON.toJSONString(userAccountRecord), e);
            }
        }
    }

}
