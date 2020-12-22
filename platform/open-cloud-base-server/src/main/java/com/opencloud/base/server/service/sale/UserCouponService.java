package com.opencloud.base.server.service.sale;

import com.opencloud.common.mybatis.base.service.IBaseService;
import com.opencloud.base.client.model.UserCouponParam;
import com.opencloud.base.client.model.entity.sale.UserCoupon;

import java.util.List;

/**
 *  服务类
 *
 * @author liyueping
 * @date 2019-11-29
 */
public interface UserCouponService extends IBaseService<UserCoupon> {
    
    /**
     * 用户获取优惠券
     * @param userCouponParam
     * @return
     */
    void saveCouponToUser(UserCouponParam userCouponParam);
    
    /**
     * 查询用户的优惠券列表
     * @param productPackageId 套餐id
     * @param userCouponStatus 优惠券可使用状态，0-全部，1-可使用，2-已使用，3-已过期
     * @return
     */
    List<UserCoupon> listUserCoupon(Long productPackageId, Integer userCouponStatus);
}
