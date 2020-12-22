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
 * 优惠券套餐关联表
 *
 * @author liyueping
 * @date 2020-03-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sale_coupon_product_package")
@ApiModel(value="CouponProductPackage对象", description="优惠券套餐关联表")
public class CouponProductPackage extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "优惠券套餐关联id")
    @TableId(value = "coupon_product_package_id", type = IdType.ID_WORKER)
    private Long couponProductPackageId;

    @ApiModelProperty(value = "优惠券id")
    private Long couponId;

    @ApiModelProperty(value = "套餐id")
    private Long productPackageId;


}
