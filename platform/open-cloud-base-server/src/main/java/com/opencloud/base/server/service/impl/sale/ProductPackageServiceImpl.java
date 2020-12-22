package com.opencloud.base.server.service.impl.sale;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.opencloud.base.client.model.entity.sale.ProductItemPackage;
import com.opencloud.base.client.model.entity.sale.ProductPackage;
import com.opencloud.base.server.mapper.sale.ProductPackageMapper;
import com.opencloud.base.server.service.sale.ProductItemPackageService;
import com.opencloud.base.server.service.sale.ProductPackageService;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.common.utils.BeanConvertUtils;
import com.opencloud.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品套餐表 服务实现类
 *
 * @author liyueping
 * @date 2019-11-29
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductPackageServiceImpl extends BaseServiceImpl<ProductPackageMapper, ProductPackage> implements ProductPackageService {

    @Autowired
    private ProductPackageMapper productPackageMapper;
    @Autowired
    private ProductItemPackageService productItemPackageService;

    @Override
    public List<ProductPackage> getAllProductPackage() {
        QueryWrapper<ProductPackage> queryWrapper = new QueryWrapper<ProductPackage>();
        queryWrapper.lambda().eq(ProductPackage::getStatus, 1);
        List<ProductPackage> productPackageList = productPackageMapper.selectList(queryWrapper);
        //获取各套餐的套餐id和销售数量，然后循环放进
        List<Map> salesNumList = productPackageMapper.getAllProductPackageSalesNum();
        for (int i = 0; i < productPackageList.size(); i++) {
            ProductPackage temp = productPackageList.get(i);
            Integer num = 0;
            for (int j = 0; j < salesNumList.size(); j++) {
                Map tempMap = salesNumList.get(j);
                if (Long.parseLong(tempMap.get("productPackageId").toString()) == temp.getProductPackageId()) {
                    num = Integer.parseInt(tempMap.get("salesVolume").toString());
                }
            }
            temp.setSalesVolume(num);
        }
        return productPackageList;
    }

    @Override
    public IPage<ProductPackage> findListPage(PageParams pageParams) {
        //暂时就只做套餐名称和上下架的查询
        ProductPackage query = pageParams.mapToObject(ProductPackage.class);
        QueryWrapper<ProductPackage> productPackageQueryWrapper = new QueryWrapper<>();
        productPackageQueryWrapper.lambda().like(ObjectUtils.isNotEmpty(query.getProductPackageName()), ProductPackage::getProductPackageName, query.getProductPackageName())
                .eq((query.getOnShelves() != null && query.getOnShelves() >= 0), ProductPackage::getOnShelves, query.getOnShelves()).orderByDesc(ProductPackage::getCreateTime);

        IPage<ProductPackage> result = productPackageMapper.selectPage(pageParams, productPackageQueryWrapper);
        return result;
    }

    @Override
    public void add(Map<String, Object> map) {
        PageParams pageParams = new PageParams(map);
        ProductPackage productPackage = pageParams.mapToObject(ProductPackage.class);
        //先保存套餐
        if (productPackage.getProductPackageId() == 0) {
            productPackage.setProductPackageId(null);
        }
        this.save(productPackage);
        //然后保存套餐和产品之间的关系
        //先删除掉原来的
        QueryWrapper<ProductItemPackage> updateWrapper = new QueryWrapper<>();
        updateWrapper.lambda().eq(ProductItemPackage::getProductPackageId, productPackage.getProductPackageId());
        productItemPackageService.remove(updateWrapper);
        //然后插入
        String productListStr = map.get("productList").toString();
        String productItemInitNumListStr = map.get("productItemInitNumList").toString();
        List<Map> productList = JSON.parseArray(productListStr, Map.class);
        List<Map> productItemInitNumList = JSON.parseArray(productItemInitNumListStr, Map.class);
        List<ProductItemPackage> productItemPackageList = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            Map temp = productList.get(i);
            Long productId = Long.parseLong(temp.get("productItemId").toString());
            Integer num = Integer.parseInt(temp.get("productItemNum").toString());
            ProductItemPackage productItemPackage = new ProductItemPackage();
            productItemPackage.setProductPackageId(productPackage.getProductPackageId());
            productItemPackage.setProductItemId(productId);
            productItemPackage.setProductItemNum(num);

            //初始发货数
            for (int j = 0; j < productItemInitNumList.size(); j++) {
                Map temp2 = productItemInitNumList.get(j);
                Long productId2 = Long.parseLong(temp2.get("productItemId").toString());
                Integer productItemInitNum = StringUtils.toInteger(temp2.get("productItemInitNum").toString());
                if (productId.equals(productId2)) {
                    productItemPackage.setProductItemInitNum(productItemInitNum);
                }
            }
            productItemPackageList.add(productItemPackage);
        }
        if (productItemPackageList.size() > 0) {
            productItemPackageService.saveBatch(productItemPackageList);
        }
    }

    @Override
    public void update(Map<String, Object> map) {
        PageParams pageParams = new PageParams(map);
        ProductPackage productPackage = pageParams.mapToObject(ProductPackage.class);
        this.updateById(productPackage);

        //然后保存套餐和产品之间的关系
        //先删除掉原来的
        QueryWrapper<ProductItemPackage> updateWrapper = new QueryWrapper<>();
        updateWrapper.lambda().eq(ProductItemPackage::getProductPackageId, productPackage.getProductPackageId());
        productItemPackageService.remove(updateWrapper);
        //然后插入
        String productListStr = map.get("productList").toString();
        String productItemInitNumListStr = map.get("productItemInitNumList").toString();
        List<Map> productList = JSON.parseArray(productListStr, Map.class);
        List<Map> productItemInitNumList = JSON.parseArray(productItemInitNumListStr, Map.class);
        List<ProductItemPackage> productItemPackageList = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            Map temp = productList.get(i);
            Long productId = Long.parseLong(temp.get("productItemId").toString());
            Integer num = Integer.parseInt(temp.get("productItemNum").toString());
            ProductItemPackage productItemPackage = new ProductItemPackage();
            productItemPackage.setProductPackageId(productPackage.getProductPackageId());
            productItemPackage.setProductItemId(productId);
            productItemPackage.setProductItemNum(num);
            //初始发货数
            for (int j = 0; j < productItemInitNumList.size(); j++) {
                Map temp2 = productItemInitNumList.get(j);
                Long productId2 = Long.parseLong(temp2.get("productItemId").toString());
                Integer productItemInitNum = StringUtils.toInteger(temp2.get("productItemInitNum").toString());
                if (productId.equals(productId2)) {
                    productItemPackage.setProductItemInitNum(productItemInitNum);
                }
            }
            productItemPackageList.add(productItemPackage);
        }
        if (productItemPackageList.size() > 0) {
            productItemPackageService.saveBatch(productItemPackageList);
        }
    }

    @Override
    public Map getProductPackageById(Long productPackageId) {
        Map resultMap = new HashMap();
        ProductPackage productPackage = this.getById(productPackageId);
        if (productPackage == null) {
            return null;
        }
        //去获取对应的产品和数量
        QueryWrapper<ProductItemPackage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ProductItemPackage::getProductPackageId, productPackage.getProductPackageId());
        List<ProductItemPackage> productItemPackageList = productItemPackageService.list(queryWrapper);
        List<Map> mapList = new ArrayList<>();
        for (int i = 0; i < productItemPackageList.size(); i++) {
            ProductItemPackage temp = productItemPackageList.get(i);
            Map tempMap = new HashMap();
            tempMap.put("productItemId", temp.getProductItemId());
            tempMap.put("productItemNum", temp.getProductItemNum());
            tempMap.put("productItemInitNum", temp.getProductItemInitNum());
            mapList.add(tempMap);
        }
        resultMap = BeanConvertUtils.objectToMap(productPackage);
        resultMap.put("productList", mapList);
        return resultMap;
    }

}
