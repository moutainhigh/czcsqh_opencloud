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
import java.util.Date;

/**
 * 订单表
 *
 * @author liyueping
 * @date 2019-11-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sale_order")
@ApiModel(value="Order对象", description="订单表")
public class Order extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "订单id")
    @TableId(value = "order_id", type = IdType.ID_WORKER)
    private Long orderId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户真实姓名")
    private String userRealName;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "订单总原价")
    private BigDecimal originalPrice;

    @ApiModelProperty(value = "优惠券抵扣金额")
    private BigDecimal couponAmount;

    @ApiModelProperty(value = "订单支付金额")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "订单状态，0-已取消，100-待付款，200-待发货，300-待收货，400-已完成，500-退货中，600-已退货")
    private Integer orderStatus;

    @ApiModelProperty(value = "订单支付时间")
    private Date payTime;

    @ApiModelProperty(value = "订单取消时间")
    private Date cancelTime;

    @ApiModelProperty(value = "订单支付方式id,1-微信支付，0-线下支付")
    private Long payTypeId;

    @ApiModelProperty(value = "是否发货，0-未发货,1-已发货")
    private Date dispatchTime;

    @ApiModelProperty(value = "是否收货，0-未收货,1-已收货")
    private Date receiveTime;

    @ApiModelProperty(value = "是否退货，0-未退货，1-退货")
    private Date returnTime;

    @ApiModelProperty(value = "用户收货地址id")
    private Long userAddressId;

    @ApiModelProperty(value = "收货人姓名")
    private String userReceiveName;

    @ApiModelProperty(value = "收货人手机号码")
    private String userReceiveMobile;

    @ApiModelProperty(value = "收货人详细地址")
    private String userReceiveDetailAddress;
    
    @ApiModelProperty(value = "物流单号")
    private String logisticsNo;
    
    @ApiModelProperty(value = "发货单号")
    private String dispatchNo;

    @ApiModelProperty(value = "状态:0-无效 1-有效")
    private Integer status;

    @ApiModelProperty(value = "订单备注")
    private String orderRemark;
    
    @TableField(exist = false)
    private String payTimeStart;
    
    @TableField(exist = false)
    private String payTimeEnd;
    
    @TableField(exist = false)
    private Long productPackageId;
    
    @TableField(exist = false)
    private String productPackageName;
    
    @TableField(exist = false)
    private Long recommendId;
    
    @TableField(exist = false)
    private Integer count;
    
    @TableField(exist = false)
    private String coverImageUrl;

    @TableField(exist = false)
    private String mobile;
    
    @TableField(exist = false)
    private String deviceCode;
    
    @TableField(exist = false)
    private String see;
    
    @TableField(exist = false)
    private String dispatchDetail;
    
    @TableField(exist = false)
    private Integer productPackageNum;

    @TableField(exist = false)
    private Long deviceId;

    @TableField(exist = false)
    private Boolean haveRate = false;
}
