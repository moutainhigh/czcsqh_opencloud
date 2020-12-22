package com.opencloud.base.server.service.sale;

import com.opencloud.base.client.model.entity.sale.WxPayOrder;
import com.opencloud.common.mybatis.base.service.IBaseService;

/**
 * 微信支付实际单号表 服务类
 *
 * @author yanjiajun
 * @date 2020-03-03
 */
public interface WxPayOrderService extends IBaseService<WxPayOrder> {

    /**
     * 根据orderId检测该orderId是否已经被支付过
     * @param orderId
     * @return
     */
    Boolean checkOrderPayStatus(Long orderId);

    WxPayOrder getByRealOrderNo(String realOrderNo);

    void updatePayStatusByRealOrderNo(String realOrderNo,String resultJson);
}

