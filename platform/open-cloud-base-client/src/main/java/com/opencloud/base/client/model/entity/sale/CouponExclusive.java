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
 * 优惠券互斥表
 *
 * @author liyueping
 * @date 2020-03-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sale_coupon_exclusive")
@ApiModel(value="CouponExclusive对象", description="优惠券互斥表")
public class CouponExclusive extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "优惠券互斥关系id")
    @TableId(value = "coupon_exclusive_id", type = IdType.ID_WORKER)
    private Long couponExclusiveId;

    @ApiModelProperty(value = "互斥优惠券A的id")
    private Long couponExclusiveAId;

    @ApiModelProperty(value = "互斥优惠券B的id")
    private Long couponExclusiveBId;


}
