package com.opencloud.base.server.service.sale;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.entity.sale.Order;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.mybatis.base.service.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * 订单表 服务类
 *
 * @author liyueping
 * @date 2019-11-28
 */
public interface OrderService extends IBaseService<Order> {
    
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<Order> findListPage(PageParams pageParams);
    
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<Order> listPayDone(PageParams pageParams);
    
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    List<Order> allList(PageParams pageParams);
    
    /**
     * 根据订单ID获取订单信息
     *
     * @param orderId
     * @return
     */
    Map<String, Object> getOrderById(Long orderId);
    
    /**
     * 确认支付
     *
     * @param orderNo
     * @return
     */
    void payDone(String orderNo);

    /**
     * 前台创建订单
     */
    Map createOrder(Long userAddressId, Long productPackageId, Long userCouponId, Integer productPackageNum,String orderRemark);

    /**
     * 获取自己所有的订单
     */
    List<Map> getMyAllOrders();

    /**
     * 取消订单
     */
    Boolean cancelOrder(Long orderId);
    
    /**
     * 获取所有订单数量
     *
     * @param pageParams
     * @return
     */
    int countOrder(PageParams pageParams);

}
