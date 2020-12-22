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
 * 产品表
 *
 * @author liyueping
 * @date 2019-11-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sale_product_item")
@ApiModel(value="ProductItem对象", description="产品表")
public class ProductItem extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "产品id")
    @TableId(value = "product_item_id", type = IdType.ID_WORKER)
    private Long productItemId;

    @ApiModelProperty(value = "产品名称")
    private String productItemName;

    @ApiModelProperty(value = "是否上架中,1-上架中，0-未上架")
    private Integer onShelves;

    @ApiModelProperty(value = "排序")
    private Integer productItemSort;

    @ApiModelProperty(value = "产品基础单位")
    private String productItemUnit;

    @ApiModelProperty(value = "产品详细描述")
    private String productItemDesc;

    @ApiModelProperty(value = "产品代码")
    private String productItemCode;
    
    @ApiModelProperty(value = "产品类型，1-设备，2-耗材")
    private Integer productItemType;

    @ApiModelProperty(value = "产品大图id")
    private Long largeImageId;

    @ApiModelProperty(value = "产品大图地址")
    private String largeImageUrl;

    @ApiModelProperty(value = "产品中图id")
    private Long mediumImageId;

    @ApiModelProperty(value = "产品中图地址")
    private String mediumImageUrl;

    @ApiModelProperty(value = "产品小图id")
    private Long smallImageId;

    @ApiModelProperty(value = "产品小图地址")
    private String smallImageUrl;

    @ApiModelProperty(value = "状态:0-无效 1-有效")
    private Integer status;


}
