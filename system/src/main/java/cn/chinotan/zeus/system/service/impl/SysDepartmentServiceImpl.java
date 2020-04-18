package cn.chinotan.zeus.system.service.impl;

import cn.chinotan.zeus.system.convert.SysDepartmentConvert;
import cn.chinotan.zeus.system.entity.SysDepartment;
import cn.chinotan.zeus.system.mapper.SysDepartmentMapper;
import cn.chinotan.zeus.system.param.SysDepartmentPageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.chinotan.zeus.framework.common.service.impl.BaseServiceImpl;
import cn.chinotan.zeus.framework.core.pagination.PageInfo;
import cn.chinotan.zeus.framework.core.pagination.Paging;
import cn.chinotan.zeus.system.enums.StateEnum;
import cn.chinotan.zeus.system.service.SysDepartmentService;
import cn.chinotan.zeus.system.vo.SysDepartmentQueryVo;
import cn.chinotan.zeus.system.vo.SysDepartmentTreeVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <pre>
 * 部门 服务实现类
 * </pre>
 *
 * @author xingcheng
 * @since 2019-10-24
 */
@Slf4j
@Service
public class SysDepartmentServiceImpl extends BaseServiceImpl<SysDepartmentMapper, SysDepartment> implements SysDepartmentService {

    @Autowired
    private SysDepartmentMapper sysDepartmentMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveSysDepartment(SysDepartment sysDepartment) throws Exception {
        sysDepartment.setId(null);
        return super.save(sysDepartment);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateSysDepartment(SysDepartment sysDepartment) throws Exception {
        return super.updateById(sysDepartment);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteSysDepartment(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public SysDepartmentQueryVo getSysDepartmentById(Serializable id) throws Exception {
        return sysDepartmentMapper.getSysDepartmentById(id);
    }

    @Override
    public Paging<SysDepartmentQueryVo> getSysDepartmentPageList(SysDepartmentPageParam sysDepartmentPageParam) throws Exception {
        Page<SysDepartmentQueryVo> page = new PageInfo<>(sysDepartmentPageParam, OrderItem.desc(getLambdaColumn(SysDepartment::getCreateTime)));
        IPage<SysDepartmentQueryVo> iPage = sysDepartmentMapper.getSysDepartmentPageList(page, sysDepartmentPageParam);
        return new Paging(iPage);
    }

    @Override
    public boolean isEnableSysDepartment(Long id) throws Exception {
        SysDepartment sysDepartment = new SysDepartment()
                .setId(id)
                .setState(StateEnum.ENABLE.getCode());
        int count = sysDepartmentMapper.selectCount(new QueryWrapper<>(sysDepartment));
        return count > 0;
    }

    @Override
    public List<SysDepartment> getAllDepartmentList() {
        SysDepartment sysDepartment = new SysDepartment().setState(StateEnum.ENABLE.getCode());
        // 获取所有已启用的部门列表
        return sysDepartmentMapper.selectList(new QueryWrapper(sysDepartment));
    }

    @Override
    public List<SysDepartmentTreeVo> getDepartmentTree() {
        List<SysDepartment> sysDepartmentList = getAllDepartmentList();
        if (CollectionUtils.isEmpty(sysDepartmentList)) {
            throw new IllegalArgumentException("SysDepartment列表不能为空");
        }
        List<SysDepartmentTreeVo> list = SysDepartmentConvert.INSTANCE.listToTreeVoList(sysDepartmentList);
        List<SysDepartmentTreeVo> treeVos = new ArrayList<>();
        for (SysDepartmentTreeVo treeVo : list) {
            if (treeVo.getParentId() == null) {
                treeVos.add(findChildren(treeVo, list));
            }
        }
        return treeVos;
    }

    /**
     * 递归获取树形结果列表
     *
     * @param tree
     * @param list
     * @return
     */
    public SysDepartmentTreeVo findChildren(SysDepartmentTreeVo tree, List<SysDepartmentTreeVo> list) {
        for (SysDepartmentTreeVo vo : list) {
            if (tree.getId().equals(vo.getParentId())) {
                if (tree.getChildren() == null) {
                    tree.setChildren(new ArrayList<>());
                }
                tree.getChildren().add(findChildren(vo, list));
            }
        }
        return tree;
    }


}
