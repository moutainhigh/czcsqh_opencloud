package com.opencloud.base.server.mapper.sale;

import com.opencloud.base.client.model.entity.sale.ProductItem;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 产品表 Mapper 接口
 * @author liyueping
 * @date 2019-11-29
 */
@Mapper
public interface ProductItemMapper extends SuperMapper<ProductItem> {

}
