package cn.chinotan.zeus.framework.shiro.convert;

import cn.chinotan.zeus.framework.shiro.jwt.JwtToken;
import cn.chinotan.zeus.framework.shiro.vo.JwtTokenRedisVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Shiro包下使用mapstruct对象属性复制转换器
 *
 * @author xingcheng
 * @date 2019-09-30
 * @since 1.3.0.RELEASE
 **/
@Mapper
public interface ShiroMapstructConvert {

    ShiroMapstructConvert INSTANCE = Mappers.getMapper(ShiroMapstructConvert.class);

    /**
     * JwtToken对象转换成JwtTokenRedisVo
     *
     * @param jwtToken
     * @return
     */
    JwtTokenRedisVo jwtTokenToJwtTokenRedisVo(JwtToken jwtToken);

}
