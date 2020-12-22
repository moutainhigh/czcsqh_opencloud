package com.opencloud.base.server.service.impl.sale;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.opencloud.base.server.mapper.sale.UserCouponMapper;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.common.security.OpenHelper;
import com.opencloud.base.client.model.UserCouponParam;
import com.opencloud.base.client.model.entity.sale.Coupon;
import com.opencloud.base.client.model.entity.sale.UserCoupon;
import com.opencloud.base.server.service.sale.CouponService;
import com.opencloud.base.server.service.sale.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 *  服务实现类
 *
 * @author liyueping
 * @date 2019-11-29
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserCouponGetServiceImpl extends BaseServiceImpl<UserCouponMapper, UserCoupon> implements UserCouponService {

    @Autowired
    private CouponService couponService;
    
    @Override
    public void saveCouponToUser(UserCouponParam userCouponParam) {
        Coupon coupon = couponService.getById(userCouponParam.getCouponId());
        if (coupon.getCouponTotal().intValue() <= coupon.getCouponUsedTotal().intValue()) {
            return;
        }
        
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(OpenHelper.getUserId());
        userCoupon.setCouponId(coupon.getCouponId());
        userCoupon.setCouponNum(1);
        userCoupon.setStartTime(Date.from(LocalDateTime.of(LocalDate.now(), LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()));
        // 计算到期时间
        String couponUseTimeJson = coupon.getCouponUseTimeJson();
        JSONObject jsonObject = JSON.parseObject(couponUseTimeJson);
        if ("2".equals(jsonObject.get("couponUseTimeType").toString())) {
            int day = Integer.parseInt(jsonObject.getString("couponExpireTime"));
            LocalDateTime endLocalDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
            endLocalDateTime = endLocalDateTime.plusDays(day - 1);
            userCoupon.setEndTime(Date.from(endLocalDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        } else {
            String date = jsonObject.getString("couponExpireTime");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate endLocalDate = LocalDate.parse(date, dateTimeFormatter);
            userCoupon.setEndTime(Date.from(LocalDateTime.of(endLocalDate, LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()));
        }
        userCoupon.setStatus(1);
        // 保存使用数量
        UpdateWrapper<Coupon> couponUpdateWrapper = new UpdateWrapper<Coupon>();
        couponUpdateWrapper.setSql("coupon_used_total = coupon_used_total + 1");
        couponUpdateWrapper.lambda().eq(Coupon::getCouponId, coupon.getCouponId());
        couponService.update(couponUpdateWrapper);
            
        save(userCoupon);
    }

    @Override
    public List<UserCoupon> listUserCoupon(Long productPackageId, Integer userCouponStatus) {
        // TODO Auto-generated method stub
        return null;
    }

}
