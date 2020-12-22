package com.opencloud.base.client.model;

import java.io.Serializable;

/**
   * 积分分页查询参数
 * @author liyueping
 *
 */

public class UserCouponParam implements Serializable {
    
    private static final long serialVersionUID = 3474271304324863160L;
    
    private Long couponTriggerId;
    
    private Long productPackageId;
    
    private Long couponId;
    
    private Long userId;

    public Long getCouponTriggerId() {
        return couponTriggerId;
    }

    public void setCouponTriggerId(Long couponTriggerId) {
        this.couponTriggerId = couponTriggerId;
    }

    public Long getProductPackageId() {
        return productPackageId;
    }

    public void setProductPackageId(Long productPackageId) {
        this.productPackageId = productPackageId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
