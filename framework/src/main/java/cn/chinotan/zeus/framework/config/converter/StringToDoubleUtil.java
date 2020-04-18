package cn.chinotan.zeus.framework.config.converter;


import org.apache.commons.lang3.StringUtils;

/**
 * <code>
 * <pre>
 * 空字符串("")转换成Double的null
 *
 * </pre>
 * </code>
 * @author xingcheng
 * @date 2018-11-08
 */
public class StringToDoubleUtil {

	public static Double convert(String source) {
		if (StringUtils.isBlank(source)){
			return null;
		}
		Double d = Double.parseDouble(source);
		return d;
	}
}
