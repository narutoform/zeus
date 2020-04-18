package cn.chinotan.zeus;

import cn.chinotan.zeus.framework.util.PrintApplicationInfo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * zeus 项目启动入口
 *
 * @author xingcheng
 * @since 2018-11-08
 */
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@EnableConfigurationProperties
@ServletComponentScan
@MapperScan({"cn.chinotan.zeus.**.mapper", "cn.lcarus.**.mapper"})
@SpringBootApplication(scanBasePackages = {"cn.chinotan.zeus", "cn.lcarus"})
public class ZeusApplication {

    public static void main(String[] args) {
        // 启动zeus
        ConfigurableApplicationContext context = SpringApplication.run(ZeusApplication.class, args);
        // 打印项目信息
        PrintApplicationInfo.print(context);
        // 打印项目提示
        PrintApplicationInfo.printTip(context);
    }

}
