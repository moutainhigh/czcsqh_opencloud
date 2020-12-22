package com.opencloud.base.server.service.impl.sale;

import com.opencloud.base.server.mapper.sale.CouponProductPackageMapper;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.base.client.model.entity.sale.CouponProductPackage;
import com.opencloud.base.server.service.sale.CouponProductPackageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 优惠券套餐关联表 服务实现类
 *
 * @author liyueping
 * @date 2020-03-09
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CouponProductPackageServiceImpl extends BaseServiceImpl<CouponProductPackageMapper, CouponProductPackage> implements CouponProductPackageService {

}
