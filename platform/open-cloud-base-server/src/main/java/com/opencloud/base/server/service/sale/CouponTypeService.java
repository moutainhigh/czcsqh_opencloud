package com.opencloud.base.server.service.sale;

import com.opencloud.common.mybatis.base.service.IBaseService;
import com.opencloud.base.client.model.entity.sale.CouponType;

import java.util.List;

/**
 * 优惠券类型表 服务类
 *
 * @author liyueping
 * @date 2020-03-09
 */
public interface CouponTypeService extends IBaseService<CouponType> {
    
    /**
     * 获取所有优惠券类型
     *
     * @param orderDetail
     * @return
     */
    List<CouponType> getAllCouponType();
}
