package com.opencloud.base.server.mapper.sale;

import com.opencloud.base.client.model.entity.sale.Order;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单表 Mapper 接口
 * @author liyueping
 * @date 2019-11-29
 */
@Mapper
public interface OrderMapper extends SuperMapper<Order> {

}
