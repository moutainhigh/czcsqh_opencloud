package com.opencloud.base.server.service.impl.sale;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.opencloud.base.client.constants.SaleConstants;
import com.opencloud.base.client.model.entity.sale.Order;
import com.opencloud.base.client.model.entity.sale.OrderDetail;
import com.opencloud.base.client.model.entity.sale.ProductItemPackage;
import com.opencloud.base.client.model.entity.sale.ProductPackage;
import com.opencloud.base.server.mapper.sale.OrderDetailMapper;
import com.opencloud.base.server.service.sale.OrderDetailService;
import com.opencloud.base.server.service.sale.OrderService;
import com.opencloud.base.server.service.sale.ProductItemPackageService;
import com.opencloud.base.server.service.sale.ProductPackageService;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  服务实现类
 *
 * @author liyueping
 * @date 2019-11-28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderDetailServiceImpl extends BaseServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
    
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ProductPackageService productPackageService;
    
    @Autowired
    private ProductItemPackageService productItemPackageService;
    
    @Autowired
    private OrderService orderService;

    @Override
    public List<OrderDetail> getOrderDetailByParam(OrderDetail orderDetail) {
        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<OrderDetail>();
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(orderDetail.getProductPackageId()), OrderDetail::getProductPackageId, orderDetail.getProductPackageId());
        if (queryWrapper.lambda().isEmptyOfWhere()) {
            return new ArrayList<OrderDetail>();
        }
        List<OrderDetail> orderDetailList = orderDetailMapper.selectList(queryWrapper);
        return orderDetailList;
    }

    @Override
    public List<OrderDetail> getOrderDetailByOrderIdList(List<Long> orderIdList) {
        if (orderIdList == null || orderIdList.isEmpty()) {
            return new ArrayList<OrderDetail>();
        }
        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<OrderDetail>();
        queryWrapper.lambda().in(OrderDetail::getOrderId, orderIdList);
        List<OrderDetail> orderDetailList = orderDetailMapper.selectList(queryWrapper);
        return orderDetailList;
    }

    @Override
    public List<OrderDetail> getOrderDetailsByOrderId(Long orderId) {
        QueryWrapper<OrderDetail> orderDetailQueryWrapper = new QueryWrapper<>();
        orderDetailQueryWrapper.lambda().eq(OrderDetail::getOrderId,orderId);
        List<OrderDetail> orderDetailList = orderDetailMapper.selectList(orderDetailQueryWrapper);
        if(!orderDetailList.isEmpty()){
            //把套餐的封面图查出来
            List<Long> productPackageIdList = orderDetailList.stream().map(OrderDetail::getProductPackageId).collect(Collectors.toList());
            QueryWrapper<ProductPackage> productPackageQueryWrapper = new QueryWrapper<>();
            productPackageQueryWrapper.lambda().in(ProductPackage::getProductPackageId,productPackageIdList);
            List<ProductPackage> productPackageList = productPackageService.list(productPackageQueryWrapper);
            for(int i=0;i<orderDetailList.size();i++){
                OrderDetail temp = orderDetailList.get(i);
                for(int j=0;j<productPackageList.size();j++){
                    ProductPackage tempProductPackage = productPackageList.get(j);
                    if(temp.getProductPackageId().equals(tempProductPackage.getProductPackageId())){
                        temp.setCoverImageUrl(tempProductPackage.getCoverImageUrl());
                    }
                }
            }
        }
        return orderDetailList;
    }
    
    @Override
    public Map<Long, Integer> getBuyPaperSumByUserIdList(List<Long> userIdList) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<Order>();
        orderQueryWrapper.lambda().in(Order::getUserId, userIdList)
                                  .in(Order::getOrderStatus, SaleConstants.ORDER_STATUS_WAIT_DISPATCH, SaleConstants.ORDER_STATUS_WAIT_RECEIVE, SaleConstants.ORDER_STATUS_DONE);
        List<Order> orderList = orderService.list(orderQueryWrapper);
        if(orderList.size()==0){
            return new HashMap<Long,Integer>();
        }
        List<Long> orderIdList = orderList.stream().map(Order::getOrderId).collect(Collectors.toList());
        
        QueryWrapper<OrderDetail> orderDetailQueryWrapper = new QueryWrapper<OrderDetail>();
        orderDetailQueryWrapper.lambda().in(OrderDetail::getOrderId, orderIdList)
                                        .orderByAsc(OrderDetail::getUserId, OrderDetail::getOrderDetailId)
                                        .select(OrderDetail::getOrderDetailId, OrderDetail::getProductPackageId, OrderDetail::getUserId);
        List<Map<String, Object>> orderDetailList = orderDetailMapper.selectMaps(orderDetailQueryWrapper);
        
        // 查询套餐里面的试纸数量
        List<ProductItemPackage> productItemPackageList = productItemPackageService.list();
        for (int i = 0; i < orderDetailList.size(); i++) {
            Map<String, Object> orderDetailMap = orderDetailList.get(i);
            for (int j = 0; j < productItemPackageList.size(); j++) {
                ProductItemPackage productItemPackage = productItemPackageList.get(j);
                if (Long.parseLong(orderDetailMap.get("product_package_id").toString()) == productItemPackage.getProductPackageId().longValue() && "2".equals(productItemPackage.getProductItemId().toString())) {
                    orderDetailMap.put("paperCount", productItemPackage.getProductItemNum());
                }
            }
        }
        
        // 合并相同的用户试纸数量
        Map<Long, Integer> resultMap = new HashMap<Long, Integer>();
        for (int i = 0; i < orderDetailList.size(); i++) {
            Map<String, Object> orderDetailMap = orderDetailList.get(i);
            long userId = Long.parseLong(orderDetailMap.get("user_id").toString());
            if (resultMap.get(userId) == null) {
                resultMap.put(userId, Integer.parseInt(orderDetailMap.get("paperCount").toString()));
            } else {
                resultMap.put(userId, resultMap.get(userId) + Integer.parseInt(orderDetailMap.get("paperCount").toString()));
            }
        }
        return resultMap;
    }
}
