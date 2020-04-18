package cn.chinotan.zeus.system.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 系统角色对象属性转换器
 *
 * @author xingcheng
 * @date 2019-10-05
 **/
@Mapper
public interface SysRoleConvert {

    SysRoleConvert INSTANCE = Mappers.getMapper(SysRoleConvert.class);

}
