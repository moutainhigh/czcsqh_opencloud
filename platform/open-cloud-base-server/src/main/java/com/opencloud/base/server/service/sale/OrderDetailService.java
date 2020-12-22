package com.opencloud.base.server.service.sale;


import com.opencloud.base.client.model.entity.sale.OrderDetail;
import com.opencloud.common.mybatis.base.service.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * 订单明细 服务类
 *
 * @author liyueping
 * @date 2019-11-28
 */
public interface OrderDetailService extends IBaseService<OrderDetail> {
    
    /**
     * 获取订单详情信息
     *
     * @param orderDetail
     * @return
     */
    List<OrderDetail> getOrderDetailByParam(OrderDetail orderDetail);
    
    /**
     * 根据订单id批量获取订单详情信息
     *
     * @param orderIdList
     * @return
     */
    List<OrderDetail> getOrderDetailByOrderIdList(List<Long> orderIdList);

    /**
     * 通过orderId获取订单详情列表
     */
    List<OrderDetail> getOrderDetailsByOrderId(Long userId);
    
    Map<Long, Integer> getBuyPaperSumByUserIdList(List<Long> userIdList);
}
