package cn.chinotan.zeus.framework.shiro.convert;

import cn.chinotan.zeus.framework.shiro.vo.LoginSysUserRedisVo;
import cn.chinotan.zeus.framework.shiro.vo.LoginSysUserVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 登录系统用户VO对象属性复制转换器
 *
 * @author xingcheng
 * @date 2020/3/24
 **/
@Mapper
public interface LoginSysUserVoConvert {

    LoginSysUserVoConvert INSTANCE = Mappers.getMapper(LoginSysUserVoConvert.class);

    /**
     * LoginSysUserVo对象转换成LoginSysUserRedisVo
     *
     * @param loginSysUserVo
     * @return
     */
    LoginSysUserRedisVo voToRedisVo(LoginSysUserVo loginSysUserVo);

}
