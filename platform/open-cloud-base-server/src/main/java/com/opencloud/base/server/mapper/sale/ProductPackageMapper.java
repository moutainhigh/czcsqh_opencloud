package com.opencloud.base.server.mapper.sale;

import com.opencloud.base.client.model.entity.sale.ProductPackage;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 产品套餐表 Mapper 接口
 * @author liyueping
 * @date 2019-11-29
 */
@Mapper
public interface ProductPackageMapper extends SuperMapper<ProductPackage> {

    /**
     * Map 中含有key：product_package_id ，salesVolume
     * @return List<Map>
     */
    List<Map> getAllProductPackageSalesNum();
}
