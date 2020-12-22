package com.opencloud.base.server.service.sale;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.model.ServiceResultBody;
import com.opencloud.common.mybatis.base.service.IBaseService;
import com.opencloud.base.client.model.entity.sale.Coupon;

/**
 * 优惠券表 服务类
 *
 * @author liyueping
 * @date 2020-03-09
 */
public interface CouponService extends IBaseService<Coupon> {
    
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<Coupon> findListPage(PageParams pageParams);

    /**
     * 发放优惠券
     */
     ServiceResultBody giveCoupon (Long couponId,String userIdListStr);
}
