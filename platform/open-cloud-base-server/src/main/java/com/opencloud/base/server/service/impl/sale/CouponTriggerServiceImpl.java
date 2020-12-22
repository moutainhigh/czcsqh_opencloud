package com.opencloud.base.server.service.impl.sale;

import com.opencloud.base.server.mapper.sale.CouponTriggerMapper;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.base.client.model.entity.sale.CouponTrigger;
import com.opencloud.base.server.service.sale.CouponTriggerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *  服务实现类
 *
 * @author liyueping
 * @date 2020-03-09
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CouponTriggerServiceImpl extends BaseServiceImpl<CouponTriggerMapper, CouponTrigger> implements CouponTriggerService {

}
