package cn.chinotan.zeus.framework.config.converter;

import org.apache.commons.lang3.StringUtils;

/**
 * <code>
 * <pre>
 * 空字符串("")转换成Integer的null
 *
 * </pre>
 * </code>
 * @author xingcheng
 * @date 2018-11-08
 */
public class StringToIntegerUtil {

	public static Integer convert(String source) {
		if (StringUtils.isBlank(source)){
			return null;
		}
		Integer i = Integer.parseInt(source);
		return i;
	}
}
