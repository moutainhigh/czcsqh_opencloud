package com.opencloud.base.server.service.impl.sale;

import com.opencloud.base.client.model.entity.sale.ProductItemPackage;
import com.opencloud.base.server.mapper.sale.ProductItemPackageMapper;
import com.opencloud.base.server.service.sale.ProductItemPackageService;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 产品套餐关联表 服务实现类
 *
 * @author liyueping
 * @date 2019-11-29
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductItemPackageServiceImpl extends BaseServiceImpl<ProductItemPackageMapper, ProductItemPackage> implements ProductItemPackageService {

}
