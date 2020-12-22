package com.opencloud.base.server.service.sale;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.entity.sale.ProductItem;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.mybatis.base.service.IBaseService;

import java.util.List;

/**
 * 产品表 服务类
 *
 * @author liyueping
 * @date 2019-11-29
 */
public interface ProductItemService extends IBaseService<ProductItem> {
    
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<ProductItem> findListPage(PageParams pageParams);

    List<ProductItem> getAllEnableProductItem();
}
