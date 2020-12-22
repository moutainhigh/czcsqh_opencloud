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

import java.util.Date;

/**
 * 
 *
 * @author liyueping
 * @date 2019-11-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sale_user_coupon")
@ApiModel(value="UserCoupon对象", description="")
public class UserCoupon extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户优惠券id")
    @TableId(value = "user_coupon_id", type = IdType.ID_WORKER)
    private Long userCouponId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "优惠券id")
    private Long couponId;

    @ApiModelProperty(value = "优惠券编号")
    private String couponNo;

    @ApiModelProperty(value = "优惠券数量")
    private Integer couponNum;

    @ApiModelProperty(value = "有效期起始时间")
    private Date startTime;

    @ApiModelProperty(value = "有效期截止时间")
    private Date endTime;

    @ApiModelProperty(value = "状态:0-无效 1-有效")
    private Integer status;

    @TableField(exist = false)
    private String couponName;

    @TableField(exist = false)
    private String imageUrl;

    @TableField(exist = false)
    private String couponDesc;

    @TableField(exist = false)
    private String couponDiscountJson;
}
