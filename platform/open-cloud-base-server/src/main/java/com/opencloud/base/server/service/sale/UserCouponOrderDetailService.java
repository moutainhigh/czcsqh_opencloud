package com.opencloud.base.server.service.sale;

import com.opencloud.common.model.ServiceResultBody;
import com.opencloud.common.mybatis.base.service.IBaseService;
import com.opencloud.base.client.model.entity.sale.UserCouponOrderDetail;

import java.math.BigDecimal;

/**
 * 优惠券与订单详情关联表 服务类
 *
 * @author liyueping
 * @date 2020-03-10
 */
public interface UserCouponOrderDetailService extends IBaseService<UserCouponOrderDetail> {
    
    /**
     * 查询优惠券是否可以被使用
     *
     * @param productPackageId 套餐id
     * @param productPackageAmount 套餐总金额
     * @param userCouponId 用户优惠券id
     * @return code:0-成功，1-已被使用，2-已过期, 3-不适用的套餐，4-套餐价格达不到使用要求
     */
    ServiceResultBody<?> getUserCouponStatus(Long productPackageId, BigDecimal productPackageAmount, Long userCouponId);
    
    /**
     * 创建订单时选择了对应的优惠券
     *
     * @param orderDetailId 订单详情id
     * @param userCouponId 用户优惠券id
     * @return code:0-成功，1-已被使用，2-已过期
     */
    ServiceResultBody<?> createOrderUserCoupon(Long orderDetailId, Long userCouponId);
}
