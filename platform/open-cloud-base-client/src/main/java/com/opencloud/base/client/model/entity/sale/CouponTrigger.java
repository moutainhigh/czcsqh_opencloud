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
 * 
 *
 * @author liyueping
 * @date 2020-03-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sale_coupon_trigger")
@ApiModel(value="CouponTrigger对象", description="")
public class CouponTrigger extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "优惠券触发条件id")
    @TableId(value = "coupon_trigger_id", type = IdType.ID_WORKER)
    private Long couponTriggerId;

    @ApiModelProperty(value = "优惠券触发条件")
    private String couponTriggerName;

    @ApiModelProperty(value = "优惠券触发条件详细描述")
    private String couponTriggerDescription;


}
