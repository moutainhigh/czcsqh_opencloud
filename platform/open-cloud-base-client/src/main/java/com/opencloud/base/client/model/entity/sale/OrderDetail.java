package com.opencloud.base.client.model.entity.sale;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.opencloud.common.mybatis.base.entity.AbstractEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 订单明细表
 *
 * @author liyueping
 * @date 2019-11-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sale_order_detail")
@ApiModel(value="OrderDetail对象", description="订单明细表")
public class OrderDetail extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "订单明细id")
    @TableId(value = "order_detail_id", type = IdType.ID_WORKER)
    private Long orderDetailId;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "产品套餐id")
    private Long productPackageId;

    @ApiModelProperty(value = "产品套餐名称")
    private String productPackageName;
    
    @ApiModelProperty(value = "套餐封面图")
    private String coverImageUrl;

    @ApiModelProperty(value = "产品套餐数量")
    private Integer productPackageNum;

    @ApiModelProperty(value = "订单总原价")
    private BigDecimal originalPrice;

    @ApiModelProperty(value = "优惠券抵扣金额")
    private BigDecimal couponAmount;

    @ApiModelProperty(value = "优惠后金额")
    private BigDecimal discountedAmount = BigDecimal.ZERO;

    @ApiModelProperty(value = "状态:0-无效 1-有效")
    private Integer status;
    
    @TableField(exist = false)
    private Integer totalPaperCount;
}
