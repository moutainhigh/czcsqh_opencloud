package com.opencloud.base.server.service.impl.sale;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.opencloud.base.server.mapper.sale.UserCouponOrderDetailMapper;
import com.opencloud.common.model.ServiceResultBody;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.base.client.model.entity.sale.Coupon;
import com.opencloud.base.client.model.entity.sale.CouponProductPackage;
import com.opencloud.base.client.model.entity.sale.UserCoupon;
import com.opencloud.base.client.model.entity.sale.UserCouponOrderDetail;
import com.opencloud.base.server.service.sale.CouponProductPackageService;
import com.opencloud.base.server.service.sale.CouponService;
import com.opencloud.base.server.service.sale.UserCouponOrderDetailService;
import com.opencloud.base.server.service.sale.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 优惠券与订单详情关联表 服务实现类
 *
 * @author liyueping
 * @date 2020-03-10
 */
@Service
@SuppressWarnings("rawtypes")
@Transactional(rollbackFor = Exception.class)
public class UserCouponOrderDetailServiceImpl extends BaseServiceImpl<UserCouponOrderDetailMapper, UserCouponOrderDetail> implements UserCouponOrderDetailService {

    @Autowired
    private UserCouponOrderDetailMapper userCouponOrderDetailMapper;

    @Resource(name = "userCouponServiceImpl")
    private UserCouponService userCouponService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponProductPackageService couponProductPackageService;

    @Override
    public ServiceResultBody<?> getUserCouponStatus(Long productPackageId, BigDecimal productPackageAmount, Long userCouponId) {
        UserCoupon userCoupon = userCouponService.getById(userCouponId);
        Coupon coupon = couponService.getById(userCoupon.getCouponId());
        if (coupon.getAllProductPackage() == 0) {
            // 先查询这张优惠券是否适用于该套餐
            QueryWrapper<CouponProductPackage> couponProductPackageQueryWrapper = new QueryWrapper<CouponProductPackage>();
            couponProductPackageQueryWrapper.lambda().eq(CouponProductPackage::getCouponId, coupon.getCouponId())
                    .eq(CouponProductPackage::getProductPackageId, productPackageId);
            int couponProductPackageCount = couponProductPackageService.count(couponProductPackageQueryWrapper);
            if (couponProductPackageCount < 1) {
                ServiceResultBody serviceResultBody = new ServiceResultBody(3, "不适用的套餐");
                return serviceResultBody;
            }
        }
        // 查询这张优惠券是否被使用过或者过期了
        if (userCoupon.getStatus() == 0) {
            ServiceResultBody serviceResultBody = new ServiceResultBody(1, "该优惠券已被使用");
            return serviceResultBody;
        }
        Instant instant = userCoupon.getEndTime().toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime endLocalDateTime = instant.atZone(zoneId).toLocalDateTime();
        if (LocalDateTime.now().isAfter(endLocalDateTime)) {
            ServiceResultBody serviceResultBody = new ServiceResultBody(2, "该优惠券已过期");
            return serviceResultBody;
        }

        // 查询这张优惠券的是否达到使用要求
        String couponDiscountJson = coupon.getCouponDiscountJson();
        JSONObject jsonObject = JSONObject.parseObject(couponDiscountJson);
        if (jsonObject.get("couponFullAmount") != null) {
            String couponFullAmount = jsonObject.getString("couponFullAmount");
            if (productPackageAmount.compareTo(new BigDecimal(couponFullAmount)) == -1) {
                ServiceResultBody serviceResultBody = new ServiceResultBody(4, "套餐价格达不到使用要求");
                return serviceResultBody;
            }
        }

        ServiceResultBody serviceResultBody = new ServiceResultBody(0, "");
        return serviceResultBody;
    }

    @Override
    public ServiceResultBody<?> createOrderUserCoupon(Long orderDetailId, Long userCouponId) {
        UserCoupon userCoupon = userCouponService.getById(userCouponId);

        // 保存所使用的优惠券与套餐的关系
        UserCouponOrderDetail userCouponOrderDetail = new UserCouponOrderDetail();
        userCouponOrderDetail.setUserCouponId(userCouponId);
        userCouponOrderDetail.setOrderDetailId(orderDetailId);
        userCouponOrderDetailMapper.insert(userCouponOrderDetail);

        // 将优惠券的状态改为已使用-无效
        userCoupon.setStatus(0);
        userCouponService.updateById(userCoupon);
        ServiceResultBody serviceResultBody = new ServiceResultBody(0, "");
        return serviceResultBody;
    }

}
