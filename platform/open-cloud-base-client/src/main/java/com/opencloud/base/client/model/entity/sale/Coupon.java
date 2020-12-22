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

import java.util.List;

/**
 * 优惠券表
 *
 * @author liyueping
 * @date 2020-03-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sale_coupon")
@ApiModel(value="Coupon对象", description="优惠券表")
public class Coupon extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "优惠券id")
    @TableId(value = "coupon_id", type = IdType.ID_WORKER)
    private Long couponId;

    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    @ApiModelProperty(value = "优惠券总数量")
    private Integer couponTotal;

    @ApiModelProperty(value = "优惠券已使用数量")
    private Integer couponUsedTotal;

    @ApiModelProperty(value = "每人限领取数量，0为无限制")
    private Integer couponUserGetTotal;

    @ApiModelProperty(value = "优惠券使用时间json")
    private String couponUseTimeJson;

    @ApiModelProperty(value = "优惠券类型id")
    private Long couponTypeId;

    @ApiModelProperty(value = "优惠券减免条件和金额的json")
    private String couponDiscountJson;

    @ApiModelProperty(value = "创建的管理员id")
    private Long adminId;

    @ApiModelProperty(value = "优惠券小图id")
    private Long imageId;

    @ApiModelProperty(value = "优惠券小图地址")
    private String imageUrl;

    @ApiModelProperty(value = "是否适用于全部商品")
    private Integer allProductPackage;

    @ApiModelProperty(value = "优惠券触发条件id")
    private Long couponTriggerId;

    @ApiModelProperty(value = "优惠券触发条件所需参数")
    private String couponTriggerJson;

    @ApiModelProperty(value = "优惠券客户描述")
    private String couponDesc;

    @ApiModelProperty(value = "优惠券备注信息")
    private String couponRemark;

    @ApiModelProperty(value = "状态:0-下架 1-上架")
    private Integer status;
    
    @TableField(exist = false)
    private String couponTypeName;
    
    @TableField(exist = false)
    private String couponTriggerName;

    /**
     * 适用套餐id
     */
    @TableField(exist = false)
    private List<Long> productPackageIdList;

}
