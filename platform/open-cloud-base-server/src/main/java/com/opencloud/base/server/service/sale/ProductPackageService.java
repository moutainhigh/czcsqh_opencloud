package com.opencloud.base.server.service.sale;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.entity.sale.ProductPackage;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.mybatis.base.service.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * 产品套餐表 服务类
 *
 * @author liyueping
 * @date 2019-11-29
 */
public interface ProductPackageService extends IBaseService<ProductPackage> {
    
    /**
     * 获取所有的套餐
     *
     * @return
     */
    List<ProductPackage> getAllProductPackage();

    /**
     * 分页获取套餐
     */
    IPage<ProductPackage> findListPage(PageParams pageParams);

    /**
     * 添加套餐
     */
    void add(Map<String,Object> map);

    /**
     *
     */
    void update(Map<String,Object> map);

    /**
     * 获取套餐的信息，包括它
     */
    Map getProductPackageById(Long productPackageId);



}
