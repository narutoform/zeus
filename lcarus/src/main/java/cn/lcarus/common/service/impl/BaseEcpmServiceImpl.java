package cn.lcarus.common.service.impl;

import cn.chinotan.zeus.framework.common.api.ApiCode;
import cn.chinotan.zeus.framework.common.api.ApiResult;
import cn.chinotan.zeus.framework.common.service.impl.BaseServiceImpl;
import cn.chinotan.zeus.redislock.annotation.DistributeLock;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.lcarus.admin.param.EcpmAddParam;
import cn.lcarus.admin.param.EcpmListParam;
import cn.lcarus.admin.res.EcpmListVo;
import cn.lcarus.common.entity.Ecpm;
import cn.lcarus.common.mapper.EcpmMapper;
import cn.lcarus.common.service.BaseEcpmService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ecpm配置表 服务实现类
 *
 * @author xingcheng
 * @since 2020-03-04
 */
@Service
@Slf4j
public class BaseEcpmServiceImpl extends BaseServiceImpl<EcpmMapper, Ecpm> implements BaseEcpmService {

    @Override
    public ApiResult<Page<EcpmListVo>> showList(EcpmListParam param) {
        IPage<Ecpm> page = lambdaQuery()
                .eq(Objects.nonNull(param.getEcpm()), Ecpm::getEcpm, param.getEcpm())
                .eq(Objects.nonNull(param.getEcpmId()), Ecpm::getId, param.getEcpmId())
                .ge(Objects.nonNull(param.getDateStart()), Ecpm::getTriggerDay, param.getDateStart())
                .le(Objects.nonNull(param.getDateEnd()), Ecpm::getTriggerDay, param.getDateEnd())
                .orderByDesc(Ecpm::getId)
                .page(param);
        List<Ecpm> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return ApiResult.ok();
        }
        DateTime now = DateUtil.date();
        DateTime nowDate = DateUtil.beginOfDay(now);
        List<EcpmListVo> collect = records
                .stream()
                .map(v -> {
                    EcpmListVo ecpmListVo = BeanUtil.toBean(v, EcpmListVo.class);
                    ecpmListVo.setTriggerDay(DateUtil.format(v.getTriggerDay(), DatePattern.NORM_DATE_PATTERN));
                    if (DateUtil.compare(now, DateUtil.offsetHour(nowDate, DEFAULT_HOUR)) < 0) {
                        ecpmListVo.setState(0);
                    } else {
                        ecpmListVo.setState(1);
                    }
                    return ecpmListVo;
                })
                .collect(Collectors.toList());
        Page<EcpmListVo> ecpmListVoPage = new Page<>();
        ecpmListVoPage.setTotal(page.getTotal());
        ecpmListVoPage.setRecords(collect);
        ecpmListVoPage.setPages(page.getSize());
        return ApiResult.ok(ecpmListVoPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributeLock(value = "ECPM_ADMIN:ADD", expire = 360, timeout = 0, errMsg = "正在创建ecpm中，请不要频繁点击哦！")
    public ApiResult<Long> add(EcpmAddParam param) {
        Ecpm ecpmSave = BeanUtil.toBean(param, Ecpm.class);
        DateTime now = DateUtil.date();
        ecpmSave.setTriggerDay(DateUtil.beginOfDay(param.getTriggerDay()));
        ecpmSave.setCreateTime(now);
        ecpmSave.setUpdateTime(ecpmSave.getCreateTime());
        boolean save = save(ecpmSave);
        Assert.isTrue(save);
        return ApiResult.ok(ecpmSave.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributeLock(value = "ECPM_ADMIN:EDIT", key = "#ecpmId", expire = 360, timeout = 0, errMsg = "正在编辑ecpm中，请不要频繁点击哦！")
    public ApiResult<?> edit(Long ecpmId, EcpmAddParam param) {
        Assert.notNull(ecpmId, "ecpmId");
        Assert.notNull(param, "param");
        DateTime now = DateUtil.date();
        DateTime nowDate = DateUtil.beginOfDay(now);
        if (DateUtil.compare(now, DateUtil.offsetHour(nowDate, DEFAULT_HOUR)) >= 0) {
            return ApiResult.fail(ApiCode.CODE_100101);
        }
        Ecpm ecpm = getById(ecpmId);
        if (Objects.isNull(ecpm)) {
            return ApiResult.fail(ApiCode.CODE_100100);
        }
        Ecpm ecpmUpdate = BeanUtil.toBean(param, Ecpm.class);
        ecpmUpdate.setId(ecpm.getId());
        ecpmUpdate.setVersion(ecpm.getVersion());
        ecpmUpdate.setUpdateTime(now);
        boolean update = updateById(ecpmUpdate);
        Assert.isTrue(update);
        return ApiResult.ok();
    }

    @Override
    public BigDecimal getEcpmValue() {
        DateTime now = DateUtil.date();
        DateTime nowDate = DateUtil.beginOfDay(now);
        if (DateUtil.compare(now, DateUtil.offsetHour(nowDate, DEFAULT_HOUR)) < 0) {
            log.warn("EcpmServiceImpl-getEcpmValue now time is before default_hour so return default_ecpm 23 {}", DateUtil.format(now, DatePattern.NORM_DATETIME_MS_PATTERN));
            return DEFAULT_ECPM;
        }
        Ecpm ecpm = lambdaQuery()
                .eq(Ecpm::getTriggerDay, nowDate)
                .one();
        if (Objects.isNull(ecpm)) {
            log.warn("EcpmServiceImpl-getEcpmValue ecpm is null so return default_ecpm 23 {}", DateUtil.format(now, DatePattern.NORM_DATETIME_MS_PATTERN));
            return DEFAULT_ECPM;
        }
        return ecpm.getEcpm();
    }

}
