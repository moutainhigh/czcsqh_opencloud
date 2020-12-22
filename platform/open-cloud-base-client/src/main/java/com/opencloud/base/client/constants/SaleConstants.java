package com.opencloud.base.client.constants;

import java.math.BigDecimal;

/**
 * @author: liyueping
 * @date: 2019/11/28 9:30
 * @description:
 */
public class SaleConstants {
    
    /**
     * 服务名称
     */
    public static final String SALE_SERVICE = "open-cloud-sale-server";
    
    /**
     * 订单状态-已取消
     */
    public final static int ORDER_STATUS_CANCEL = 0;
    /**
     * 订单状态-待付款
     */
    public final static int ORDER_STATUS_WAIT_PAY = 100;
    /**
     * 订单状态-待发货
     */
    public final static int ORDER_STATUS_WAIT_DISPATCH = 200;
    /**
     * 订单状态-待收货
     */
    public final static int ORDER_STATUS_WAIT_RECEIVE = 300;
    /**
     * 订单状态-已完成
     */
    public final static int ORDER_STATUS_DONE = 400;
    /**
     * 订单状态-退货中
     */
    public final static int ORDER_STATUS_RETURNING = 500;
    /**
     * 订单状态-已退货
     */
    public final static int ORDER_STATUS_RETURNED = 600;

    /**
     * 提现账号类型
     */
    public static final Long WITHDRAWALS_ACCOUNT_TYPE_ALIPAY = 1L;
    public static final Long WITHDRAWALS_ACCOUNT_TYPE_BANK = 2L;

    /**
     * 提现最大、最小金额
     */
    public static final BigDecimal WITHDRAWALS_APPLY_MAX = new BigDecimal(5000);
    public static final BigDecimal WITHDRAWALS_APPLY_MIN = new BigDecimal(100);
    
    /**
     * 优惠券获得触发条件
     */
    public static final Long USER_COUPON_GET = 1L;
    public static final Long USER_COUPON_BUY = 2L;
    public static final Long USER_COUPON_REGISTER = 3L;
}
