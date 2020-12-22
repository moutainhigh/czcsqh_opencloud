package com.opencloud.base.server.mapper.sale;

import com.opencloud.base.client.model.entity.sale.WxPayOrder;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 微信支付实际单号表 Mapper 接口
 * @author yanjiajun
 * @date 2020-3-3
 */
@Mapper
public interface WxPayOrderMapper extends SuperMapper<WxPayOrder> {

}
