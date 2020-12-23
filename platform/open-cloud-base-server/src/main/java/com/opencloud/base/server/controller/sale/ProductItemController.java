package com.opencloud.base.server.controller.sale;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.entity.sale.ProductItem;
import com.opencloud.base.server.service.sale.ProductItemService;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.model.ResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 产品表 前端控制器
 *
 * @author liyueping
 * @date 2019-11-29
 */
//@Api(value = "产品表", tags = "产品表")
@RestController
@RequestMapping("productItem")
    public class ProductItemController {

    @Autowired
    private ProductItemService productItemService;

    /**
    * 获取产品分页数据
    *
    * @return
    */
    @ApiOperation(value = "获取产品分页数据", notes = "获取产品分页数据")
    @GetMapping(value = "/list")
    public ResultBody<IPage<ProductItem>> list(@RequestParam(required = false) Map map){
        return ResultBody.ok().data(productItemService.findListPage(new PageParams(map)));
    }

    /**
     * 获取所有可用的产品
     *
     */
    @ApiOperation(value = "获取产品分页数据", notes = "获取产品分页数据")
    @GetMapping(value = "/getAllEnableProductItem")
    public ResultBody<List<ProductItem>> getAllEnableProductItem(@RequestParam(required = false) Map map){
        return ResultBody.ok().data(productItemService.getAllEnableProductItem());
    }

    /**
     * 根据ID查找数据
     */
    @ApiOperation(value = "根据ID查找数据", notes = "根据ID查找数据")
    @GetMapping("/get")
    public ResultBody<ProductItem> get(@RequestParam("id") Long id){
        ProductItem entity = productItemService.getById(id);
        return ResultBody.ok().data(entity);
    }

    /**
    * 添加数据
    * @return
    */
    @ApiOperation(value = "添加数据", notes = "添加数据")
    @ApiImplicitParams({
         @ApiImplicitParam(name = "productItemName", required = true, value = "产品名称", paramType = "form"),
         @ApiImplicitParam(name = "onShelves", required = true, value = "是否上架中,1-上架中，0-未上架", paramType = "form"),
         @ApiImplicitParam(name = "productItemSort", required = true, value = "排序", paramType = "form"),
         @ApiImplicitParam(name = "productItemUnit", required = true, value = "产品基础单位", paramType = "form"),
         @ApiImplicitParam(name = "productItemDesc", required = true, value = "产品详细描述", paramType = "form"),
         @ApiImplicitParam(name = "productItemCode", required = true, value = "产品代码", paramType = "form"),
         @ApiImplicitParam(name = "largeImageId", required = true, value = "产品大图id", paramType = "form"),
         @ApiImplicitParam(name = "largeImageUrl", required = true, value = "产品大图地址", paramType = "form"),
         @ApiImplicitParam(name = "mediumImageId", required = true, value = "产品中图id", paramType = "form"),
         @ApiImplicitParam(name = "mediumImageUrl", required = true, value = "产品中图地址", paramType = "form"),
         @ApiImplicitParam(name = "smallImageId", required = true, value = "产品小图id", paramType = "form"),
         @ApiImplicitParam(name = "smallImageUrl", required = true, value = "产品小图地址", paramType = "form"),
        @ApiImplicitParam(name = "status", required = true, value = "状态:0-无效 1-有效", paramType = "form")
            })
    @PostMapping("/add")
    public ResultBody add(
        @RequestParam(value = "productItemName") String productItemName,
        @RequestParam(value = "onShelves") Integer onShelves,
        @RequestParam(value = "productItemSort") Integer productItemSort,
        @RequestParam(value = "productItemUnit") String productItemUnit,
        @RequestParam(value = "productItemDesc") String productItemDesc,
        @RequestParam(value = "productItemCode") String productItemCode,
        @RequestParam(value = "largeImageId") Long largeImageId,
        @RequestParam(value = "largeImageUrl") String largeImageUrl,
        @RequestParam(value = "mediumImageId") Long mediumImageId,
        @RequestParam(value = "mediumImageUrl") String mediumImageUrl,
        @RequestParam(value = "smallImageId") Long smallImageId,
        @RequestParam(value = "smallImageUrl") String smallImageUrl,
        @RequestParam(value = "status") Integer status
            ){
        ProductItem entity = new ProductItem();
        entity.setProductItemName(productItemName);
        entity.setOnShelves(onShelves);
        entity.setProductItemSort(productItemSort);
        entity.setProductItemUnit(productItemUnit);
        entity.setProductItemDesc(productItemDesc);
        entity.setProductItemCode(productItemCode);
        entity.setLargeImageId(largeImageId);
        entity.setLargeImageUrl(largeImageUrl);
        entity.setMediumImageId(mediumImageId);
        entity.setMediumImageUrl(mediumImageUrl);
        entity.setSmallImageId(smallImageId);
        entity.setSmallImageUrl(smallImageUrl);
        entity.setStatus(status);
        productItemService.save(entity);
        return ResultBody.ok();
    }

    /**
    * 更新数据
    * @return
    */
    @ApiOperation(value = "更新数据", notes = "更新数据")
    @ApiImplicitParams({
                    @ApiImplicitParam(name = "productItemId", required = true, value = "产品id", paramType = "form"),
                    @ApiImplicitParam(name = "productItemName", required = true, value = "产品名称", paramType = "form"),
                    @ApiImplicitParam(name = "onShelves", required = true, value = "是否上架中,1-上架中，0-未上架", paramType = "form"),
                    @ApiImplicitParam(name = "productItemSort", required = true, value = "排序", paramType = "form"),
                    @ApiImplicitParam(name = "productItemUnit", required = true, value = "产品基础单位", paramType = "form"),
                    @ApiImplicitParam(name = "productItemDesc", required = true, value = "产品详细描述", paramType = "form"),
                    @ApiImplicitParam(name = "productItemCode", required = true, value = "产品代码", paramType = "form"),
                    @ApiImplicitParam(name = "largeImageId", required = true, value = "产品大图id", paramType = "form"),
                    @ApiImplicitParam(name = "largeImageUrl", required = true, value = "产品大图地址", paramType = "form"),
                    @ApiImplicitParam(name = "mediumImageId", required = true, value = "产品中图id", paramType = "form"),
                    @ApiImplicitParam(name = "mediumImageUrl", required = true, value = "产品中图地址", paramType = "form"),
                    @ApiImplicitParam(name = "smallImageId", required = true, value = "产品小图id", paramType = "form"),
                    @ApiImplicitParam(name = "smallImageUrl", required = true, value = "产品小图地址", paramType = "form"),
                    @ApiImplicitParam(name = "status", required = true, value = "状态:0-无效 1-有效", paramType = "form")
        })
        @PostMapping("/update")
        public ResultBody add(
                @RequestParam(value = "productItemId") Long productItemId,
                @RequestParam(value = "productItemName") String productItemName,
                @RequestParam(value = "onShelves") Integer onShelves,
                @RequestParam(value = "productItemSort") Integer productItemSort,
                @RequestParam(value = "productItemUnit") String productItemUnit,
                @RequestParam(value = "productItemDesc") String productItemDesc,
                @RequestParam(value = "productItemCode") String productItemCode,
                @RequestParam(value = "largeImageId") Long largeImageId,
                @RequestParam(value = "largeImageUrl") String largeImageUrl,
                @RequestParam(value = "mediumImageId") Long mediumImageId,
                @RequestParam(value = "mediumImageUrl") String mediumImageUrl,
                @RequestParam(value = "smallImageId") Long smallImageId,
                @RequestParam(value = "smallImageUrl") String smallImageUrl,
                @RequestParam(value = "status") Integer status
        ){
            ProductItem entity = new ProductItem();
                    entity.setProductItemId(productItemId);
                    entity.setProductItemName(productItemName);
                    entity.setOnShelves(onShelves);
                    entity.setProductItemSort(productItemSort);
                    entity.setProductItemUnit(productItemUnit);
                    entity.setProductItemDesc(productItemDesc);
                    entity.setProductItemCode(productItemCode);
                    entity.setLargeImageId(largeImageId);
                    entity.setLargeImageUrl(largeImageUrl);
                    entity.setMediumImageId(mediumImageId);
                    entity.setMediumImageUrl(mediumImageUrl);
                    entity.setSmallImageId(smallImageId);
                    entity.setSmallImageUrl(smallImageUrl);
                    entity.setStatus(status);
                productItemService.updateById(entity);
                return ResultBody.ok();
        }

    /**
    * 删除数据
    * @return
    */
    @ApiOperation(value = "删除数据", notes = "删除数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, value = "id", paramType = "form")
    })
    @PostMapping("/remove")
    public ResultBody remove(
            @RequestParam(value = "id") Long id
    ){
            productItemService.removeById(id);
            return ResultBody.ok();
      }

    /**
    * 批量删除数据
    * @return
    */
    @ApiOperation(value = "批量删除数据", notes = "批量删除数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", required = true, value = "多个用,号隔开", paramType = "form")
    })
    @PostMapping("/batch/remove")
    public ResultBody batchRemove(
                @RequestParam(value = "ids") String ids
            ){
            productItemService.removeByIds(Arrays.asList(ids.split(",")));
            return ResultBody.ok();
     }

}
