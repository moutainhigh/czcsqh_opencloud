package com.opencloud.base.server.service.impl.sale;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.opencloud.base.server.mapper.sale.CouponMapper;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.model.ServiceResultBody;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.base.client.model.entity.sale.*;
import com.opencloud.base.server.service.sale.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 优惠券表 服务实现类
 *
 * @author liyueping
 * @date 2020-03-09
 */
@Service
@SuppressWarnings("unchecked")
@Transactional(rollbackFor = Exception.class)
public class CouponServiceImpl extends BaseServiceImpl<CouponMapper, Coupon> implements CouponService {
    
    @Autowired
    private CouponMapper couponMapper;
    
    @Autowired
    private CouponTypeService couponTypeService;
    
    @Autowired
    private CouponTriggerService couponTriggerService;

    @Autowired
    private CouponProductPackageService couponProductPackageService;

    @Resource(name = "userCouponServiceImpl")
    private UserCouponService userCouponService;

    @Override
    public IPage<Coupon> findListPage(PageParams pageParams) {
        // 查询优惠券分页信息
        Coupon coupon = pageParams.mapToObject(Coupon.class);
        QueryWrapper<Coupon> couponQueryWrapper = new QueryWrapper<Coupon>();
        couponQueryWrapper.lambda().like(ObjectUtils.isNotEmpty(coupon.getCouponName()), Coupon::getCouponName, coupon.getCouponName())
                                   .eq(ObjectUtils.isNotEmpty(coupon.getCouponTypeId()) && coupon.getCouponTypeId() != -1, Coupon::getCouponTypeId, coupon.getCouponTypeId())
                                   .eq(ObjectUtils.isNotEmpty(coupon.getCouponTriggerId()) && coupon.getCouponTriggerId() !=-1, Coupon::getCouponTriggerId, coupon.getCouponTriggerId())
                                   .eq(coupon.getStatus() != null && coupon.getStatus().intValue() != -1, Coupon::getStatus, coupon.getStatus())
                                   .orderByDesc(Coupon::getCreateTime);
        IPage<Coupon> couponPage = couponMapper.selectPage(pageParams, couponQueryWrapper);
        List<Coupon> couponList = couponPage.getRecords();
        if (couponList.isEmpty()) {
            return couponPage;
        }
        // 查询优惠券相关信息
        // 查询优惠券类型
        List<CouponType> couponTypeList = couponTypeService.list();
        for (int i = 0; i < couponList.size(); i++) {
            Coupon couponDetail = couponList.get(i);
            for (int j = 0; j < couponTypeList.size(); j++) {
                CouponType couponType = couponTypeList.get(j);
                if (couponDetail.getCouponTypeId().longValue() == couponType.getCouponTypeId().longValue()) {
                    couponDetail.setCouponTypeName(couponType.getCouponTypeName());
                }
            }
        }
        // 查询优惠券触发条件
        List<CouponTrigger> couponTriggerList = couponTriggerService.list();
        for (int i = 0; i < couponList.size(); i++) {
            Coupon couponDetail = couponList.get(i);
            for (int j = 0; j < couponTriggerList.size(); j++) {
                CouponTrigger couponTrigger = couponTriggerList.get(j);
                if (couponDetail.getCouponTriggerId().longValue() == couponTrigger.getCouponTriggerId().longValue()) {
                    couponDetail.setCouponTriggerName(couponTrigger.getCouponTriggerName());
                }
            }
        }
        //获取适用套餐信息
        for (int i = 0; i < couponList.size(); i++) {
            Coupon couponDetail = couponList.get(i);
            QueryWrapper<CouponProductPackage> couponProductPackageQueryWrapper = new QueryWrapper<>();
            couponProductPackageQueryWrapper.lambda().eq(CouponProductPackage::getCouponId,couponDetail.getCouponId());
            List<CouponProductPackage> couponProductPackageList = couponProductPackageService.list(couponProductPackageQueryWrapper);
            List<Long> productPackageIdList = couponProductPackageList.stream().map(CouponProductPackage::getProductPackageId).collect(Collectors.toList());
            couponDetail.setProductPackageIdList(productPackageIdList);
        }
        return couponPage;
    }

    @Override
    public ServiceResultBody giveCoupon(Long couponId, String userIdListStr) {
        Coupon coupon = getById(couponId);
        if(coupon == null){
            return new ServiceResultBody(-1,"优惠券不存在");
        }
        //判断一下优惠券的剩余数量还够不够
        List list = JSONObject.parseArray(userIdListStr);
        if(coupon.getCouponTotal() - coupon.getCouponUsedTotal() < list.size()){
            return new ServiceResultBody(-1,"优惠券剩余数量不足");
        }
        List<UserCoupon> userCouponList = new ArrayList<>();
        for(int i=0; i<list.size() ;i++){
            Long tempId = Long.parseLong(list.get(i).toString());
            UserCoupon userCoupon = new UserCoupon();
            userCoupon.setStartTime(Date.from(LocalDateTime.of(LocalDate.now(), LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant()));
            userCoupon.setEndTime(handleEndTime(coupon.getCouponUseTimeJson()));
            userCoupon.setStatus(1);
            userCoupon.setCouponId(couponId);
            userCoupon.setCouponNum(1);
            userCoupon.setCouponNo(null);
            userCoupon.setUserId(tempId);
            userCouponList.add(userCoupon);
        }
        if(userCouponList.size()>0){
            userCouponService.saveBatch(userCouponList);
            coupon.setCouponUsedTotal(coupon.getCouponUsedTotal()+list.size());
            this.updateById(coupon);
        }


        return new ServiceResultBody(0,null);
    }

    private Date handleEndTime (String couponUseTimeJson){
        JSONObject jsonObject = JSON.parseObject(couponUseTimeJson);
        if ("2".equals(jsonObject.get("couponUseTimeType").toString())) {
            int day = Integer.parseInt(jsonObject.getString("couponExpireTime"));
            LocalDateTime endLocalDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
            endLocalDateTime = endLocalDateTime.plusDays(day-1);
            return Date.from(endLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
        } else {
            String date = jsonObject.getString("couponExpireTime");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate endLocalDate = LocalDate.parse(date, dateTimeFormatter);
            return Date.from(LocalDateTime.of(endLocalDate, LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
        }
    }
}
