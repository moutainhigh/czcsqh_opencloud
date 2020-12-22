package com.opencloud.base.server.mapper.sale;

import com.opencloud.base.client.model.entity.sale.UserCouponOrderDetail;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券与订单详情关联表 Mapper 接口
 * @author liyueping
 * @date 2020-03-10
 */
@Mapper
public interface UserCouponOrderDetailMapper extends SuperMapper<UserCouponOrderDetail> {

}
