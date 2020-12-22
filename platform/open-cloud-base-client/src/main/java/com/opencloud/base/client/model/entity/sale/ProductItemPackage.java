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
 * 产品套餐关联表
 *
 * @author liyueping
 * @date 2019-11-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sale_product_item_package")
@ApiModel(value="ProductItemPackage对象", description="产品套餐关联表")
public class ProductItemPackage extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "产品套餐关联id")
    @TableId(value = "product_item_package_id", type = IdType.ID_WORKER)
    private Long productItemPackageId;

    @ApiModelProperty(value = "产品套餐id")
    private Long productPackageId;

    @ApiModelProperty(value = "产品id")
    private Long productItemId;

    @ApiModelProperty(value = "产品个数")
    private Integer productItemNum;

    @ApiModelProperty(value = "初始发放产品个数")
    private Integer productItemInitNum;
}
