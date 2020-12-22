package com.opencloud.base.server.mapper.sale;

import com.opencloud.base.client.model.entity.sale.CouponType;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券类型表 Mapper 接口
 * @author liyueping
 * @date 2020-03-09
 */
@Mapper
public interface CouponTypeMapper extends SuperMapper<CouponType> {

}
