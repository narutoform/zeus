package cn.chinotan.zeus.system.convert;

import cn.chinotan.zeus.system.entity.SysPermission;
import cn.chinotan.zeus.system.vo.SysPermissionTreeVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * SysPermission类对象书香转换器
/ *
 * @author xingcheng
 * @date 2019-10-26
 **/
@Mapper
public interface SysPermissionConvert {

    SysPermissionConvert INSTANCE = Mappers.getMapper(SysPermissionConvert.class);

    /**
     * SysPermission对象转换成SysPermissionTreeVo对象
     *
     * @param sysPermission
     * @return
     */
    SysPermissionTreeVo permissionToTreeVo(SysPermission sysPermission);

}
