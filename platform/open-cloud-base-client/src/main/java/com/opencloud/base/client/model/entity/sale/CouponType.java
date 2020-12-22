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
 * 优惠券类型表
 *
 * @author liyueping
 * @date 2020-03-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sale_coupon_type")
@ApiModel(value="CouponType对象", description="优惠券类型表")
public class CouponType extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "优惠券类型id")
    @TableId(value = "coupon_type_id", type = IdType.ID_WORKER)
    private Long couponTypeId;

    @ApiModelProperty(value = "优惠券类型名称")
    private String couponTypeName;


}
