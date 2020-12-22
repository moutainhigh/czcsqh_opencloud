package com.opencloud.base.server.service.impl.sale;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.opencloud.base.server.mapper.sale.UserCouponMapper;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.common.security.OpenHelper;
import com.opencloud.base.client.model.UserCouponParam;
import com.opencloud.base.client.model.entity.sale.Coupon;
import com.opencloud.base.client.model.entity.sale.CouponProductPackage;
import com.opencloud.base.client.model.entity.sale.UserCoupon;
import com.opencloud.base.server.service.sale.CouponProductPackageService;
import com.opencloud.base.server.service.sale.CouponService;
import com.opencloud.base.server.service.sale.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  服务实现类
 *
 * @author liyueping
 * @date 2019-11-29
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserCouponServiceImpl extends BaseServiceImpl<UserCouponMapper, UserCoupon> implements UserCouponService {

    @Autowired
    private UserCouponMapper userCouponMapper;
    
    @Autowired
    private CouponProductPackageService couponProductPackageService;

    @Autowired
    private CouponService couponService;
    
    @Override
    public void saveCouponToUser(UserCouponParam userCouponParam) {
        
    }

    @Override
    public List<UserCoupon> listUserCoupon(Long productPackageId, Integer userCouponStatus) {
        List<Long> couponIdList = null;
        if (productPackageId != null && productPackageId != 0) {
            QueryWrapper<CouponProductPackage> couponProductPackageQueryWrapper = new QueryWrapper<CouponProductPackage>();
            couponProductPackageQueryWrapper.lambda().eq(CouponProductPackage::getProductPackageId, productPackageId);
            List<CouponProductPackage> couponProductPackageList = couponProductPackageService.list(couponProductPackageQueryWrapper);
            couponIdList = couponProductPackageList.stream().map(CouponProductPackage::getCouponId).collect(Collectors.toList());
        }
        
        Long userId = OpenHelper.getUserId();
        Date nowDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String nowDateString = dateFormat.format(nowDate);
        
        QueryWrapper<UserCoupon> userCouponQueryWrapper = new QueryWrapper<UserCoupon>();
        userCouponQueryWrapper.lambda().eq(UserCoupon::getUserId, userId)
                                       .in(ObjectUtils.isNotEmpty(couponIdList), UserCoupon::getCouponId, couponIdList).orderByDesc(UserCoupon::getStatus).orderByDesc(UserCoupon::getCreateTime);
        if (userCouponStatus == 1) {
            userCouponQueryWrapper.lambda().eq(UserCoupon::getStatus, 1)
                                           .ge(UserCoupon::getEndTime, nowDateString);
        } else if (userCouponStatus == 2) {
            userCouponQueryWrapper.lambda().eq(UserCoupon::getStatus, 0);
        } else if (userCouponStatus == 3) {
            userCouponQueryWrapper.lambda().eq(UserCoupon::getStatus, 1)
                                           .lt(UserCoupon::getEndTime, nowDateString);
        }
        List<UserCoupon> userCouponList = userCouponMapper.selectList(userCouponQueryWrapper);
        //封装优惠券的信息
        if(userCouponList.size()>0){
            List<Long> couponIdList2 = userCouponList.stream().map(UserCoupon::getCouponId).collect(Collectors.toList());
            QueryWrapper<Coupon> couponQueryWrapper = new QueryWrapper<>();
            couponQueryWrapper.lambda().in(Coupon::getCouponId,couponIdList2);
            List<Coupon> couponList = couponService.list(couponQueryWrapper);

            for(int i=0;i<userCouponList.size();i++){
                UserCoupon tempUserCoupon = userCouponList.get(i);
                for(int j=0;j<couponList.size();j++){
                    Coupon tempCoupon = couponList.get(j);
                    if(tempUserCoupon.getCouponId().equals(tempCoupon.getCouponId())){
                        tempUserCoupon.setCouponName(tempCoupon.getCouponName());
                        tempUserCoupon.setCouponDesc(tempCoupon.getCouponDesc());
                        tempUserCoupon.setImageUrl(tempCoupon.getImageUrl());
                        tempUserCoupon.setCouponDiscountJson(tempCoupon.getCouponDiscountJson());
                    }
                }
            }
        }

        return userCouponList;
    }

}
