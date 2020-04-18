package cn.chinotan.zeus.framework.config.converter;

import org.springframework.core.convert.converter.Converter;

/**
 * <code>
 *
 * </code>
 * @author xingcheng
 * @date 2018-11-08
 */
public class StringToDoubleConverter implements Converter<String, Double> {

	@Override
	public Double convert(String source) {
		return StringToDoubleUtil.convert(source);
	}
}
