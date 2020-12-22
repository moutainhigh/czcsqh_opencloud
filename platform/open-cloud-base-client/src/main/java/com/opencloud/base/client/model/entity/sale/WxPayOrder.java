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
 * 订单表
 *
 * @author liyueping
 * @date 2019-11-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("sale_wx_pay_order")
@ApiModel(value="WxPayOrder对象", description="微信支付实际单号表")
public class WxPayOrder extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "wx_pay_order_id", type = IdType.ID_WORKER)
    private Long wxPayOrderId;

    @ApiModelProperty(value = "orderId")
    private Long orderId;

    @ApiModelProperty(value = "微信支付真实单号")
    private String realOrderNo;

    @ApiModelProperty(value = "微信appid")
    private String appid;

    @ApiModelProperty(value = "回调结果")
    private String result;

    @ApiModelProperty(value = "调用时发送的json")
    private String sendJson;

    @ApiModelProperty(value = "回调结果json")
    private String resultJson;



}
