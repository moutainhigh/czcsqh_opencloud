package com.opencloud.base.server.service.impl.sale;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.opencloud.base.client.constants.SaleConstants;
import com.opencloud.base.client.model.entity.BaseUser;
import com.opencloud.base.client.model.entity.sale.*;
import com.opencloud.base.server.mapper.sale.OrderMapper;
import com.opencloud.base.server.service.BaseUserService;
import com.opencloud.base.server.service.sale.*;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.model.ServiceResultBody;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.common.security.OpenHelper;
import com.opencloud.common.security.OpenUserDetails;
import com.opencloud.common.utils.StringUtils;
import com.opencloud.common.utils.UUID8Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单表 服务实现类
 *
 * @author liyueping
 * @date 2019-11-28
 */
@Service
@Slf4j
@SuppressWarnings("unchecked")
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl extends BaseServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ProductItemPackageService productItemPackageService;

    @Autowired
    private UserProductItemService userProductItemService;

    @Autowired
    private ProductPackageService productPackageService;
    
    @Autowired
    private CouponService couponService;

    @Resource(name = "userCouponServiceImpl")
    private UserCouponService userCouponService;

    @Autowired
    private UserCouponOrderDetailService userCouponOrderDetailService;
    
    @Autowired
    private ProductItemService productItemService;
    
    @Autowired
    private BaseUserService userService;

    @Override
    public IPage<Order> findListPage(PageParams pageParams) {
        List<Long> orderIdList = null;
        OrderDetail orderDetail = pageParams.mapToObject(OrderDetail.class);
        // 筛选套餐
        if (orderDetail.getProductPackageId() != null && orderDetail.getProductPackageId() != 0) {
            List<OrderDetail> orderDetailList = orderDetailService.getOrderDetailByParam(orderDetail);
            if (orderDetailList.isEmpty()) {
                return new PageParams();
            }
            orderIdList = orderDetailList.stream().map(OrderDetail::getOrderId).collect(Collectors.toList());
        }

        // 区分看全部数据还是自己的数据
        Order order = pageParams.mapToObject(Order.class);
        List<Long> userIdsByMobile = null;
        if(ObjectUtils.isNotEmpty(order.getMobile())) {
            Map conditionMap = new HashMap(16);
            conditionMap.put("mobile", order.getMobile());
            String conditionMapString = JSON.toJSONString(conditionMap);
            userIdsByMobile = userService.getUserIdListByCondition(conditionMapString);
            if (userIdsByMobile == null || userIdsByMobile.isEmpty()) {
                return new PageParams();
            }
        }

        QueryWrapper<Order> queryWrapper = new QueryWrapper<Order>();

        Object specialOrderStatus = pageParams.getRequestMap().get("specialOrderStatus");
        if(ObjectUtils.isNotEmpty(specialOrderStatus)){
            queryWrapper.lambda().like(ObjectUtils.isNotEmpty(order.getOrderNo()), Order::getOrderNo, order.getOrderNo())
                    .like(ObjectUtils.isNotEmpty(order.getUserRealName()), Order::getUserRealName, order.getUserRealName())
                    .in(ObjectUtils.isNotEmpty(orderIdList), Order::getOrderId, orderIdList)
                    .ge(ObjectUtils.isNotEmpty(order.getPayTimeStart()), Order::getPayTime, order.getPayTimeStart())
                    .le(ObjectUtils.isNotEmpty(order.getPayTimeEnd()), Order::getPayTime, order.getPayTimeEnd())
                    .ge(Order::getOrderStatus, order.getOrderStatus())
                    .orderByDesc(Order::getPayTime, Order::getCreateTime);
        }else {
            queryWrapper.lambda().like(ObjectUtils.isNotEmpty(order.getOrderNo()), Order::getOrderNo, order.getOrderNo())
                    .like(ObjectUtils.isNotEmpty(order.getUserRealName()), Order::getUserRealName, order.getUserRealName())
                    .in(ObjectUtils.isNotEmpty(orderIdList), Order::getOrderId, orderIdList)
                    .in(ObjectUtils.isNotEmpty(userIdsByMobile), Order::getUserId, userIdsByMobile)
                    .ge(ObjectUtils.isNotEmpty(order.getPayTimeStart()), Order::getPayTime, order.getPayTimeStart())
                    .le(ObjectUtils.isNotEmpty(order.getPayTimeEnd()), Order::getPayTime, order.getPayTimeEnd())
                    .eq(order.getOrderStatus() != null && order.getOrderStatus().intValue() != -1, Order::getOrderStatus, order.getOrderStatus())
                    .orderByDesc(Order::getPayTime, Order::getCreateTime);
        }

        IPage<Order> orderPage = orderMapper.selectPage(pageParams, queryWrapper);
        List<Order> orderList = orderPage.getRecords();
        if (orderList.isEmpty()) {
            return new PageParams();
        }
        orderIdList = orderList.stream().map(Order::getOrderId).collect(Collectors.toList());
        List<Long> userIdList = orderList.stream().map(Order::getUserId).collect(Collectors.toList());
        List<OrderDetail> orderDetailList = orderDetailService.getOrderDetailByOrderIdList(orderIdList);
        List<BaseUser> userListByUserIdList = userService.getUserListByUserIdList(userIdList.toString());

        for (Order orderByMobile : orderList) {
            for (BaseUser user : userListByUserIdList) {
                if(orderByMobile.getUserId().equals(user.getUserId())){
                    orderByMobile.setMobile(user.getMobile());
                }
            }
        }

        for (int i = 0; i < orderList.size(); i++) {
            Order returnOrder = orderList.get(i);
            for (int j = 0; j < orderDetailList.size(); j++) {
                OrderDetail returnOrderDetail = orderDetailList.get(j);
                if (returnOrder.getOrderId().longValue() == returnOrderDetail.getOrderId().longValue()) {
                    returnOrder.setProductPackageName(returnOrderDetail.getProductPackageName());
                    returnOrder.setProductPackageId(returnOrderDetail.getProductPackageId());
                    break;
                }
            }
        }

        return orderPage;
    }

    @Override
    public IPage<Order> listPayDone(PageParams pageParams) {
        List<Long> orderIdList = null;
        Order order = pageParams.mapToObject(Order.class);
        QueryWrapper<Order> queryWrapper = new QueryWrapper<Order>();

        queryWrapper.lambda().ge(ObjectUtils.isNotEmpty(order.getPayTimeStart()), Order::getPayTime, order.getPayTimeStart())
                             .le(ObjectUtils.isNotEmpty(order.getPayTimeEnd()), Order::getPayTime, order.getPayTimeEnd())
                             .ge(Order::getOrderStatus, SaleConstants.ORDER_STATUS_WAIT_DISPATCH)
                             .orderByDesc(Order::getPayTime, Order::getCreateTime);

        IPage<Order> orderPage = orderMapper.selectPage(pageParams, queryWrapper);
        List<Order> orderList = orderPage.getRecords();
        orderIdList = orderList.stream().map(Order::getOrderId).collect(Collectors.toList());
        List<Long> userIdList = orderList.stream().map(Order::getUserId).collect(Collectors.toList());
        List<OrderDetail> orderDetailList = orderDetailService.getOrderDetailByOrderIdList(orderIdList);
        List<BaseUser> userListByUserIdList = userService.getUserListByUserIdList(userIdList.toString());

        for (Order orderByMobile : orderList) {
            for (BaseUser user : userListByUserIdList) {
                if(orderByMobile.getUserId().equals(user.getUserId())){
                    orderByMobile.setMobile(user.getMobile());
                }
            }
        }

        for (int i = 0; i < orderList.size(); i++) {
            Order returnOrder = orderList.get(i);
            for (int j = 0; j < orderDetailList.size(); j++) {
                OrderDetail returnOrderDetail = orderDetailList.get(j);
                if (returnOrder.getOrderId().longValue() == returnOrderDetail.getOrderId().longValue()) {
                    returnOrder.setProductPackageName(returnOrderDetail.getProductPackageName());
                    returnOrder.setProductPackageId(returnOrderDetail.getProductPackageId());
                    break;
                }
            }
        }
        return orderPage;
    }
    
    @Override
    public List<Order> allList(PageParams pageParams) {
        List<Long> orderIdList = null;
        OrderDetail orderDetail = pageParams.mapToObject(OrderDetail.class);
        if (orderDetail.getProductPackageId() != null && orderDetail.getProductPackageId() != 0) {
            List<OrderDetail> orderDetailList = orderDetailService.getOrderDetailByParam(orderDetail);
            if (orderDetailList.isEmpty()) {
                return new ArrayList<Order>();
            }
            orderIdList = orderDetailList.stream().map(OrderDetail::getOrderId).collect(Collectors.toList());
        }

        Order order = pageParams.mapToObject(Order.class);
        QueryWrapper<Order> queryWrapper = new QueryWrapper<Order>();
        queryWrapper.lambda().like(ObjectUtils.isNotEmpty(order.getOrderNo()), Order::getOrderNo, order.getOrderNo())
                .like(ObjectUtils.isNotEmpty(order.getUserRealName()), Order::getUserRealName, order.getUserRealName())
                .in(ObjectUtils.isNotEmpty(orderIdList), Order::getOrderId, orderIdList)
                .ge(ObjectUtils.isNotEmpty(order.getPayTimeStart()), Order::getPayTime, order.getPayTimeStart())
                .le(ObjectUtils.isNotEmpty(order.getPayTimeEnd()), Order::getPayTime, order.getPayTimeEnd())
                .eq(order.getOrderStatus() != null && order.getOrderStatus().intValue() != -1, Order::getOrderStatus, order.getOrderStatus())
                .orderByDesc(Order::getPayTime);

        List<Order> orderList = orderMapper.selectList(queryWrapper);
        orderIdList = orderList.stream().map(Order::getOrderId).collect(Collectors.toList());
        List<OrderDetail> orderDetailList = orderDetailService.getOrderDetailByOrderIdList(orderIdList);
        for (int i = 0; i < orderList.size(); i++) {
            Order returnOrder = orderList.get(i);
            for (int j = 0; j < orderDetailList.size(); j++) {
                OrderDetail returnOrderDetail = orderDetailList.get(j);
                if (returnOrder.getOrderId().longValue() == returnOrderDetail.getOrderId().longValue()) {
                    returnOrder.setProductPackageName(returnOrderDetail.getProductPackageName());
                    returnOrder.setProductPackageNum(returnOrderDetail.getProductPackageNum());
                    returnOrder.setProductPackageId(returnOrderDetail.getProductPackageId());
                    break;
                }
            }
        }
        
        // 展示手机号
        List<Long> userIdList = orderList.stream().map(Order::getUserId).collect(Collectors.toList());
        List<BaseUser> userListByUserIdList = userService.getUserListByUserIdList(userIdList.toString());
        for (Order orderByMobile : orderList) {
            for (BaseUser user : userListByUserIdList) {
                if(orderByMobile.getUserId().equals(user.getUserId())){
                    orderByMobile.setMobile(user.getMobile());
                }
            }
        }
        // 查询产品的名称
        Map<Long, Object> productItemMap = new HashMap<Long, Object>();
        List<ProductItem> productItemList = productItemService.list();
        for (int i = 0; i < productItemList.size(); i++) {
            ProductItem productItem = productItemList.get(i);
            productItemMap.put(productItem.getProductItemId(), productItem.getProductItemName() + "," + productItem.getProductItemUnit());
        }
        
        // 查询套餐中包含的产品数量
        List<Long> productPackageIdList = orderDetailList.stream().map(OrderDetail::getProductPackageId).collect(Collectors.toList());
        QueryWrapper<ProductItemPackage> productItemPackageQueryWrapper = new QueryWrapper<ProductItemPackage>();
        productItemPackageQueryWrapper.lambda().in(ProductItemPackage::getProductPackageId, productPackageIdList);
        List<ProductItemPackage> productItemPackageList = productItemPackageService.list(productItemPackageQueryWrapper);
        for (int i = 0; i < orderList.size(); i++) {
            Order returnOrder = orderList.get(i);
            for (int j = 0; j < productItemPackageList.size(); j++) {
                ProductItemPackage productItemPackage = productItemPackageList.get(j);
                if (returnOrder.getProductPackageId().longValue() == productItemPackage.getProductPackageId().longValue()) {
                    if (StringUtils.isNotBlank(returnOrder.getDispatchDetail())) {
                        returnOrder.setDispatchDetail(returnOrder.getDispatchDetail() + "||" + productItemMap.get(productItemPackage.getProductItemId()).toString().split(",")[0] + (returnOrder.getProductPackageNum() * productItemPackage.getProductItemInitNum()) + productItemMap.get(productItemPackage.getProductItemId()).toString().split(",")[1]);
                    } else {
                        returnOrder.setDispatchDetail(productItemMap.get(productItemPackage.getProductItemId()).toString().split(",")[0] + (returnOrder.getProductPackageNum() * productItemPackage.getProductItemInitNum()) + productItemMap.get(productItemPackage.getProductItemId()).toString().split(",")[1]);
                    }
                }
            }
        }

        return orderList;
    }


    @Override
    public Map<String, Object> getOrderById(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            return new HashMap<String, Object>();
        }
        Map<String, Object> orderMap = (Map<String, Object>) JSON.toJSON(order);
        List<Long> orderIdList = new ArrayList<Long>();
        orderIdList.add(orderId);
        List<OrderDetail> orderDetailList = orderDetailService.getOrderDetailByOrderIdList(orderIdList);
        Map<String, Object> orderDetailMap = (Map<String, Object>) JSON.toJSON(orderDetailList.get(0));
        orderMap.putAll(orderDetailMap);
        return orderMap;
    }

    @Override
    public void payDone(String orderNo) {
        try {
            //这里先用orderNo查出orderId
            QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
            orderQueryWrapper.lambda().eq(Order::getOrderNo, orderNo);
            Order tempOrder = orderMapper.selectOne(orderQueryWrapper);
            if (tempOrder == null) {
                log.error("方法payDone中接收到的orderNo无法查询到对应的订单，orderNo:" + orderNo);
                return;
            }
            if (tempOrder.getOrderStatus() > SaleConstants.ORDER_STATUS_WAIT_PAY) {
                log.error("该订单已被支付，无需再次调用支付方法，orderNo:" + orderNo);
                return;
            }

            Long orderId = tempOrder.getOrderId();

            // 更新订单支付状态
            UpdateWrapper<Order> orderUpdateWrapper = new UpdateWrapper<Order>();
            orderUpdateWrapper.lambda().eq(Order::getOrderId, orderId)
                    .set(Order::getOrderStatus, 200)
                    .set(Order::getPayTypeId, 1)
                    .set(Order::getPayTime, new Date());
            orderMapper.update(null, orderUpdateWrapper);
            // 查询购买的订单中的产品数量
            QueryWrapper<OrderDetail> orderDetailQueryWrapper = new QueryWrapper<OrderDetail>();
            orderDetailQueryWrapper.lambda().eq(OrderDetail::getOrderId, orderId)
                    .select(OrderDetail::getProductPackageId, OrderDetail::getProductPackageNum, OrderDetail::getUserId ,OrderDetail::getOrderId);
            OrderDetail orderDetail = orderDetailService.getOne(orderDetailQueryWrapper);

        } catch (Exception e) {
            log.error("支付订单出错", e);
        }

    }


    /**
     * 注意，本方法返回的map中的code与ResultBody中的code的意义是不一样的，0代表失败，1代表成功
     * 2020-3-19添加：修改逻辑，当
     */
    @Override
    public Map createOrder(Long userAddressId, Long productPackageId, Long userCouponId, Integer productPackageNum, String orderRemark) {
        Map resultMap = new HashMap();
        OpenUserDetails user = OpenHelper.getUser();
        //把套餐信息查出来先
        ProductPackage productPackage = productPackageService.getById(productPackageId);
        //把优惠券信息查出来，判断优惠券是否还有剩余总量，判断用户是否还有该优惠券的使用量
        BigDecimal couponAmount = new BigDecimal(0);
        Boolean userCouponFlag = false;
        if (userCouponId != null) {
            //首先校验该优惠券是不是该用户的
            UserCoupon userCoupon = userCouponService.getById(userCouponId);
            if (userCoupon == null) {
                resultMap.put("code", 0);
                resultMap.put("msg", "优惠券不存在");
                return resultMap;
            }
            if (!userCoupon.getUserId().equals(OpenHelper.getUserId())) {
                resultMap.put("code", 0);
                resultMap.put("msg", "该优惠券不属于您");
                return resultMap;
            }
            ServiceResultBody couponServiceResult = userCouponOrderDetailService.getUserCouponStatus(productPackageId, productPackage.getPackagePrice(), userCouponId);
            if (couponServiceResult.getCode() != 0) {
                resultMap.put("code", 0);
                resultMap.put("msg", couponServiceResult.getMessage());
                return resultMap;
            }
            //上面判断了能使用
            userCouponFlag = true;
            Coupon coupon = couponService.getById(userCoupon.getCouponId());
            String couponDiscountJsonStr = coupon.getCouponDiscountJson();
            JSONObject couponDiscountJson = JSONObject.parseObject(couponDiscountJsonStr);
            String couponTypeId = couponDiscountJson.get("couponTypeId").toString();
            if ("1".equals(couponTypeId)) {
                //无门槛，直接减
                couponAmount = new BigDecimal(couponDiscountJson.get("couponAmount").toString());
            } else if ("2".equals(couponTypeId)) {
                //满减券，上面判断过套餐价格是否满足满减，这里就不做判断了
                couponAmount = new BigDecimal(couponDiscountJson.get("couponAmount").toString());
            } else if ("3".equals(couponTypeId)) {
                //折扣券，需要对比一下折扣的价格和最大减免的价格
                BigDecimal couponDiscountPercentage = new BigDecimal(couponDiscountJson.get("couponDiscountPercentage").toString());
                BigDecimal couponDiscountAmount = productPackage.getPackagePrice().multiply(couponDiscountPercentage).divide(new BigDecimal("100"));
                BigDecimal couponAmount2 = new BigDecimal(couponDiscountJson.get("couponAmount").toString());
                couponAmount = couponAmount2.compareTo(couponDiscountAmount) < 0 ? couponAmount2 : couponDiscountAmount;
            }
        }

        LocalDateTime nowTime = LocalDateTime.now();
        BigDecimal orderAmount = productPackage.getPackagePrice().multiply(new BigDecimal(productPackageNum)).subtract(couponAmount);
        StringBuilder orderNo = new StringBuilder();
        orderNo.append(nowTime.getYear()).append(nowTime.getMonthValue()).append(nowTime.getDayOfMonth()).append(UUID8Util.generateShortNumberUuid());
        //开始生成order
        Order order = new Order();
        order.setUserId(user.getUserId());
        order.setOrderNo(orderNo.toString());
        order.setOriginalPrice(productPackage.getPackagePrice().multiply(new BigDecimal(productPackageNum)));
        order.setOrderAmount(orderAmount.compareTo(BigDecimal.ZERO) > 0 ? orderAmount : BigDecimal.ZERO);
        order.setCouponAmount(couponAmount);
        order.setOrderStatus(100);
        order.setUserAddressId(userAddressId);
        order.setUserReceiveName("");
        order.setUserReceiveMobile("");
        order.setUserReceiveDetailAddress("");
        order.setStatus(1);
        order.setOrderRemark(orderRemark);
        this.save(order);

        //然后就开始保存orderDetail表
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(order.getOrderId());
        orderDetail.setUserId(user.getUserId());
        orderDetail.setCoverImageUrl(productPackage.getCoverImageUrl());
        orderDetail.setProductPackageId(productPackage.getProductPackageId());
        orderDetail.setProductPackageName(productPackage.getProductPackageName());
        orderDetail.setProductPackageNum(productPackageNum);
        orderDetail.setOriginalPrice(productPackage.getPackagePrice().multiply(new BigDecimal(productPackageNum)));
        orderDetail.setDiscountedAmount(productPackage.getPackagePrice().multiply(new BigDecimal(productPackageNum)).subtract(couponAmount));
        orderDetail.setStatus(1);
        orderDetail.setCouponAmount(couponAmount);
        orderDetailService.save(orderDetail);
        //订单生成完之后需要对优惠券进行更新，用户优惠券表的用户可使用数-1，优惠券表的优惠券总量-1等等
        //还要保存一下订单详细和优惠券的关系
        if (userCouponFlag) {
            userCouponOrderDetailService.createOrderUserCoupon(orderDetail.getOrderDetailId(), userCouponId);
        }

        //如果orderAmount是0的话，就调用一下支付接口
        if (order.getOrderAmount().compareTo(BigDecimal.ZERO) == 0) {
            payDone(order.getOrderNo());
        }
        resultMap.put("code", 1);
        resultMap.put("data", order.getOrderId());
        resultMap.put("msg", "订单生成成功");
        return resultMap;
    }


    @Override
    public List<Map> getMyAllOrders() {
        List<Map> resultList = new ArrayList<>();
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.lambda().eq(Order::getUserId, OpenHelper.getUserId()).orderByDesc(Order::getCreateTime);
        List<Order> orderList = orderMapper.selectList(orderQueryWrapper);
        if (orderList.isEmpty()) {
            return resultList;
        }
        //获取orderDetail
        List<Long> orderIdList = orderList.stream().map(Order::getOrderId).collect(Collectors.toList());
        QueryWrapper<OrderDetail> orderDetailQueryWrapper = new QueryWrapper<>();
        orderDetailQueryWrapper.lambda().in(OrderDetail::getOrderId, orderIdList);
        List<OrderDetail> orderDetailList = orderDetailService.list(orderDetailQueryWrapper);

        return resultList;
    }

    @Override
    public Boolean cancelOrder(Long orderId) {
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.lambda().eq(Order::getOrderId, orderId).eq(Order::getUserId, OpenHelper.getUserId());
        Order order = orderMapper.selectOne(orderQueryWrapper);
        if (order == null) {
            return false;
        }

        Order newOrder = new Order();
        newOrder.setOrderId(order.getOrderId());
        newOrder.setOrderStatus(0);
        orderMapper.updateById(newOrder);

        // 将使用的优惠券解封
        QueryWrapper<OrderDetail> orderDetailQueryWrapper = new QueryWrapper<OrderDetail>();
        orderDetailQueryWrapper.lambda().eq(OrderDetail::getOrderId, order.getOrderId());
        OrderDetail orderDetail = orderDetailService.getOne(orderDetailQueryWrapper);
        if (orderDetail == null) {
            return false;
        }
        QueryWrapper<UserCouponOrderDetail> userCouponOrderDetailQueryWrapper = new QueryWrapper<UserCouponOrderDetail>();
        userCouponOrderDetailQueryWrapper.lambda().eq(UserCouponOrderDetail::getOrderDetailId, orderDetail.getOrderDetailId());
        UserCouponOrderDetail userCouponOrderDetail = userCouponOrderDetailService.getOne(userCouponOrderDetailQueryWrapper);
        if (userCouponOrderDetail == null) {
            return true;
        }
        UpdateWrapper<UserCoupon> userCouponUpdateWrapper = new UpdateWrapper<UserCoupon>();
        userCouponUpdateWrapper.lambda().set(UserCoupon::getStatus, 1)
                .eq(UserCoupon::getUserCouponId, userCouponOrderDetail.getUserCouponId());
        userCouponService.update(userCouponUpdateWrapper);
        return true;
    }


    @Override
    public int countOrder(PageParams pageParams) {
        List<Long> orderIdList = null;
        OrderDetail orderDetail = pageParams.mapToObject(OrderDetail.class);
        QueryWrapper<OrderDetail> orderDetailQueryWrapper = new QueryWrapper<OrderDetail>();
        orderDetailQueryWrapper.lambda().eq(OrderDetail::getProductPackageId, orderDetail.getProductPackageId())
                .select(OrderDetail::getOrderId);
        List<OrderDetail> orderDetailList = orderDetailService.list(orderDetailQueryWrapper);
        if (orderDetailList.size() == 0) {
            return 0;
        }
        orderIdList = orderDetailList.stream().map(OrderDetail::getOrderId).collect(Collectors.toList());
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<Order>();
        orderQueryWrapper.lambda().in(Order::getOrderId, orderIdList)
                .gt(Order::getOrderStatus, SaleConstants.ORDER_STATUS_WAIT_PAY);
        return this.count(orderQueryWrapper);
    }



}
