package com.opencloud.base.client.model.entity.sale;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.opencloud.common.mybatis.base.entity.AbstractEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 优惠券与订单详情关联表
 *
 * @author liyueping
 * @date 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sale_user_coupon_order_detail")
@ApiModel(value="UserCouponOrderDetail对象", description="优惠券与订单详情关联表")
public class UserCouponOrderDetail extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "优惠券与订单详情关联id")
    @TableId(value = "user_coupon_order_detail_id", type = IdType.ID_WORKER)
    private Long userCouponOrderDetailId;

    @ApiModelProperty(value = "用户优惠券id")
    private Long userCouponId;

    @ApiModelProperty(value = "订单明细id")
    private Long orderDetailId;


}
