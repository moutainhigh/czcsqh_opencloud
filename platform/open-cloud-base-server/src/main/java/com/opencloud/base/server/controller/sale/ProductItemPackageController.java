package com.opencloud.base.server.controller.sale;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.entity.sale.ProductItemPackage;
import com.opencloud.base.server.service.sale.ProductItemPackageService;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.model.ResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 产品套餐关联表 前端控制器
 *
 * @author liyueping
 * @date 2019-11-29
 */
@Api(value = "产品套餐关联表", tags = "产品套餐关联表")
    @RestController
@RequestMapping("productItemPackage")
    public class ProductItemPackageController {

    @Autowired
    private ProductItemPackageService targetService;

    /**
    * 获取分页数据
    *
    * @return
    */
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    @GetMapping(value = "/list")
    public ResultBody<IPage<ProductItemPackage>>list(@RequestParam(required = false) Map map){
        PageParams pageParams = new PageParams(map);
        ProductItemPackage query = pageParams.mapToObject(ProductItemPackage.class);
        QueryWrapper<ProductItemPackage> queryWrapper = new QueryWrapper();
        return ResultBody.ok().data(targetService.page(pageParams,queryWrapper));
    }

    /**
     * 根据ID查找数据
     */
    @ApiOperation(value = "根据ID查找数据", notes = "根据ID查找数据")
    @ResponseBody
    @RequestMapping("/get")
    public ResultBody<ProductItemPackage> get(@RequestParam("id") Long id){
        ProductItemPackage entity = targetService.getById(id);
        return ResultBody.ok().data(entity);
    }

    /**
    * 添加数据
    * @return
    */
    @ApiOperation(value = "添加数据", notes = "添加数据")
    @ApiImplicitParams({
         @ApiImplicitParam(name = "productPackageId", required = true, value = "产品套餐id", paramType = "form"),
         @ApiImplicitParam(name = "productItemId", required = true, value = "产品id", paramType = "form"),
        @ApiImplicitParam(name = "productItemNum", required = true, value = "产品个数", paramType = "form")
            })
    @PostMapping("/add")
    public ResultBody add(
        @RequestParam(value = "productPackageId") Long productPackageId,
        @RequestParam(value = "productItemId") Long productItemId,
        @RequestParam(value = "productItemNum") Integer productItemNum
            ){
        ProductItemPackage entity = new ProductItemPackage();
        entity.setProductPackageId(productPackageId);
        entity.setProductItemId(productItemId);
        entity.setProductItemNum(productItemNum);
        targetService.save(entity);
        return ResultBody.ok();
    }

    /**
    * 更新数据
    * @return
    */
    @ApiOperation(value = "更新数据", notes = "更新数据")
    @ApiImplicitParams({
                    @ApiImplicitParam(name = "productItemPackageId", required = true, value = "产品套餐关联id", paramType = "form"),
                    @ApiImplicitParam(name = "productPackageId", required = true, value = "产品套餐id", paramType = "form"),
                    @ApiImplicitParam(name = "productItemId", required = true, value = "产品id", paramType = "form"),
                    @ApiImplicitParam(name = "productItemNum", required = true, value = "产品个数", paramType = "form")
        })
        @PostMapping("/update")
        public ResultBody add(
                @RequestParam(value = "productItemPackageId") Long productItemPackageId,
                @RequestParam(value = "productPackageId") Long productPackageId,
                @RequestParam(value = "productItemId") Long productItemId,
                @RequestParam(value = "productItemNum") Integer productItemNum
        ){
            ProductItemPackage entity = new ProductItemPackage();
                    entity.setProductItemPackageId(productItemPackageId);
                    entity.setProductPackageId(productPackageId);
                    entity.setProductItemId(productItemId);
                    entity.setProductItemNum(productItemNum);
                targetService.updateById(entity);
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
            targetService.removeById(id);
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
            targetService.removeByIds(Arrays.asList(ids.split(",")));
            return ResultBody.ok();
     }

}
