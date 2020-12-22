package com.opencloud.base.server.service.impl.sale;

import com.opencloud.base.server.mapper.sale.CouponTypeMapper;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.base.client.model.entity.sale.CouponType;
import com.opencloud.base.server.service.sale.CouponTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 优惠券类型表 服务实现类
 *
 * @author liyueping
 * @date 2020-03-09
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CouponTypeServiceImpl extends BaseServiceImpl<CouponTypeMapper, CouponType> implements CouponTypeService {
    
    @Autowired
    private CouponTypeMapper couponTypeMapper;

    @Override
    public List<CouponType> getAllCouponType() {
        return couponTypeMapper.selectList(null);
    }

}
