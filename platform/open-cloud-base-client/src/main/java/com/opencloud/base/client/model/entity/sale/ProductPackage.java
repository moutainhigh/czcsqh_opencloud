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
 * 产品套餐表
 *
 * @author liyueping
 * @date 2019-11-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sale_product_package")
@ApiModel(value="ProductPackage对象", description="产品套餐表")
public class ProductPackage extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "产品套餐id")
    @TableId(value = "product_package_id", type = IdType.ID_WORKER)
    private Long productPackageId;

    @ApiModelProperty(value = "套餐名称")
    private String productPackageName;

    @ApiModelProperty(value = "套餐原价")
    private BigDecimal originalPrice;

    @ApiModelProperty(value = "套餐价格")
    private BigDecimal packagePrice;

    @ApiModelProperty(value = "排序")
    private Integer productPackageSort;

    @ApiModelProperty(value = "活动标签")
    private String activityTag;

    @ApiModelProperty(value = "套餐详细说明")
    private String productPackageDesc;

    @ApiModelProperty(value = "套餐备注")
    private String productPackageRemark;

    @ApiModelProperty(value = "套餐详情")
    private String productPackageDetail;

    @ApiModelProperty(value = "是否上架中,1-上架中，0-未上架")
    private Integer onShelves;

    @ApiModelProperty(value = "套餐单位名称")
    private String productPackageUnit;

    @ApiModelProperty(value = "产品套餐封面地址")
    private String coverImageUrl;

    @ApiModelProperty(value = "产品套餐大图id")
    private Long largeImageId;

    @ApiModelProperty(value = "产品套餐大图地址")
    private String largeImageUrl;

    @ApiModelProperty(value = "产品套餐中图id")
    private Long mediumImageId;

    @ApiModelProperty(value = "产品套餐中图地址")
    private String mediumImageUrl;

    @ApiModelProperty(value = "产品套餐小图id")
    private Long smallImageId;

    @ApiModelProperty(value = "产品套餐小图地址")
    private String smallImageUrl;

    @ApiModelProperty(value = "状态:0-无效 1-有效")
    private Integer status;

    @TableField(exist = false)
    private Integer salesVolume;

    @TableField(exist = false)
    private Integer stock;

    //优惠券后支付金额
    @TableField(exist = false)
    private BigDecimal discountedAmount;

    //2020-04-22添加，该字段目前用于免费领取的产品，记录剩余数值
    @TableField(exist = false)
    private Integer remain;
}
