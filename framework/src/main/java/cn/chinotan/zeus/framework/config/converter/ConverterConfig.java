package cn.chinotan.zeus.framework.config.converter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * 转换器配置
 * @author xingcheng
 * @date 2018-11-08
 */
@Configuration
public class ConverterConfig {

    @Bean
    public Converter<String, Date> stringToDateConverter() {
        return new StringToDateConverter();
    }

    @Bean
    public Converter<String, Integer> stringToIntegerConverter() {
        return new StringToIntegerConverter();
    }

    @Bean
    public Converter<String, Double> stringToDoubleConverter() {
        return new StringToDoubleConverter();
    }

}
