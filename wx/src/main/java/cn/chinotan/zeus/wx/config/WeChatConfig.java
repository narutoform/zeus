package cn.chinotan.zeus.wx.config;

import cn.binarywang.wx.miniapp.api.*;
import cn.binarywang.wx.miniapp.api.impl.*;
import com.github.binarywang.wxpay.service.EntPayService;
import com.github.binarywang.wxpay.service.RedpackService;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.EntPayServiceImpl;
import com.github.binarywang.wxpay.service.impl.RedpackServiceImpl;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: guess
 * @description: 微信环境配置
 * @author: xingcheng
 * @create: 2019-09-01 15:25
 **/
@Configuration
public class WeChatConfig {

    /**
     * 微信小程序
     */
    @Bean
    public WxMaMsgService wxMaMsgService(WxMaService wxMaService){
        return new WxMaMsgServiceImpl(wxMaService);
    }

    @Bean
    public WxMaUserService wxMaUserService(WxMaService wxMaService){
        return new WxMaUserServiceImpl(wxMaService);
    }

    @Bean
    public WxMaSubscribeService wxMaSubscribeService(WxMaService wxMaService){
        return new WxMaSubscribeServiceImpl(wxMaService);
    }

    @Bean
    public WxMaShareService wxMaShareService(WxMaService wxMaService){
        return new WxMaShareServiceImpl(wxMaService);
    }

    @Bean
    public WxMaSettingService wxMaSettingService(WxMaService wxMaService){
        return new WxMaSettingServiceImpl(wxMaService);
    }

    @Bean
    public WxMaSecCheckService wxMaSecCheckService(WxMaService wxMaService){
        return new WxMaSecCheckServiceImpl(wxMaService);
    }

    @Bean
    public WxMaPluginService wxMaPluginService(WxMaService wxMaService){
        return new WxMaPluginServiceImpl(wxMaService);
    }

    @Bean
    public WxMaCodeService wxMaCodeService(WxMaService wxMaService){
        return new WxMaCodeServiceImpl(wxMaService);
    }

    /**
     * 微信支付
     */
    @Bean
    public EntPayService entPayService(WxPayService wxPayService){
        return new EntPayServiceImpl(wxPayService);
    }

    @Bean
    public RedpackService redpackService(WxPayService wxPayService){
        return new RedpackServiceImpl(wxPayService);
    }
}
