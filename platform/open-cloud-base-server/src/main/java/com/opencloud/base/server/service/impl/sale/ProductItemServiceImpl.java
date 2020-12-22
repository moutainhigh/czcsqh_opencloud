package com.opencloud.base.server.service.impl.sale;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.opencloud.base.client.model.entity.sale.ProductItem;
import com.opencloud.base.server.mapper.sale.ProductItemMapper;
import com.opencloud.base.server.service.sale.ProductItemService;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 产品表 服务实现类
 *
 * @author liyueping
 * @date 2019-11-29
 */
@Service
@SuppressWarnings("unchecked")
@Transactional(rollbackFor = Exception.class)
public class ProductItemServiceImpl extends BaseServiceImpl<ProductItemMapper, ProductItem> implements ProductItemService {
    
    @Autowired
    private ProductItemMapper productItemMapper;

    @Override
    public IPage<ProductItem> findListPage(PageParams pageParams) {
        ProductItem productItem = pageParams.mapToObject(ProductItem.class);
        QueryWrapper<ProductItem> queryWrapper = new QueryWrapper<ProductItem>();
        queryWrapper.lambda().like(ObjectUtils.isNotEmpty(productItem.getProductItemName()), ProductItem::getProductItemName, productItem.getProductItemName())
                             .eq(ObjectUtils.isNotEmpty(productItem.getOnShelves()), ProductItem::getOnShelves, productItem.getOnShelves());
        IPage<ProductItem> productItemPage = productItemMapper.selectPage(pageParams, queryWrapper);
        return productItemPage;
    }

    /**
     * 获取所有可用的产品，即已上架的，有效的
     * @return
     */
    @Override
    public List<ProductItem> getAllEnableProductItem() {
        QueryWrapper<ProductItem> productItemQueryWrapper = new QueryWrapper<>();
        productItemQueryWrapper.lambda().eq(ProductItem::getOnShelves,1).eq(ProductItem::getStatus,1);
        return productItemMapper.selectList(productItemQueryWrapper);
    }
}
