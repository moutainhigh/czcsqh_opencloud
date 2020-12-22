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

/**
 * 用户产品表
 *
 * @author liyueping
 * @date 2019-11-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sale_user_product_item")
@ApiModel(value="UserProductItem对象", description="用户产品表")
public class UserProductItem extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户产品")
    @TableId(value = "user_product_item_id", type = IdType.ID_WORKER)
    private Long userProductItemId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "产品id")
    private Long productItemId;

    @ApiModelProperty(value = "设备id")
    private Long deviceId;

    @ApiModelProperty(value = "上级id")
    private Long parentId;

    @ApiModelProperty(value = "产品数量,正数表示增加，负数表示减少")
    private Integer productItemNum;

    @ApiModelProperty(value = "领取状态：1-未领取完，0-领取完")
    private Integer numStatus;

    @ApiModelProperty(value = "操作人：user-用户，system-系统。默认为空")
    private String operator;

    @ApiModelProperty(value = "订单id，operator为system时该字段值为空")
    private Long orderId;

    @ApiModelProperty(value = "状态:0-无效 1-有效")
    private Integer status;

    @TableField(exist = false)
    private Integer count;
    
    @TableField(exist = false)
    private String deviceCode;
}
