package cn.chinotan.zeus.scheduled;

import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.lcarus.common.service.BaseWxService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: lcarus
 * @description: 抽奖活动提醒
 * @author: xingcheng
 * @create: 2020-03-14 17:56
 **/
@Slf4j
@Component
public class LotteryActivityRemindTask {

    @Autowired
    private BaseWxService baseWxService;

    @Value("${lcarus.wx.miniapp.index-page}")
    private String indexPage;

    /**
     * 每天10点半
     */
    @Scheduled(cron = "0 30 10 * * ? ")
    public void activityRemind() {
        List<WxMaSubscribeMessage.Data> dataList = Lists.newArrayList();
        WxMaSubscribeMessage.Data data = new WxMaSubscribeMessage.Data();
        // 活动名称
        data.setName("thing1");
        data.setValue("抽奖");
        dataList.add(data);
        // 备注
        data = new WxMaSubscribeMessage.Data();
        data.setName("thing4");
        data.setValue("您预约的抽奖即将开始，马上参与抽奖吧～");
        dataList.add(data);
        WxMaSubscribeMessage wxMaSubscribeMessage = WxMaSubscribeMessage
                .builder()
                .templateId("C44s4DPFSUUnmTQpLP2fFc4rV9A8O4GsLGb9FG_30vM")
                .page(indexPage)
                .data(dataList)
                .build();
        baseWxService.sendAllUserThirdNoticeWATemplate(wxMaSubscribeMessage);
    }
}
