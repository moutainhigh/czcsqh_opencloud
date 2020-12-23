package com.opencloud.base.server.controller.sale;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.entity.sale.ProductPackage;
import com.opencloud.base.server.service.sale.ProductPackageService;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.model.ResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//@Api(value = "产品套餐表", tags = "产品套餐表")
@RestController
@RequestMapping("productPackage")
public class ProductPackageController {

    @Autowired
    private ProductPackageService productPackageService;

    /**
     * 获取分页数据
     *
     * @return
     */
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    @GetMapping(value = "/list")
    public ResultBody<IPage<ProductPackage>> findListPage(@RequestParam(required = false) Map map) {
        return ResultBody.ok().data(productPackageService.findListPage(new PageParams(map)));
    }

    /**
     * 查看所有产品套餐
     */
    @ApiOperation(value = "查看所有产品套餐", notes = "查看所有产品套餐")
    @GetMapping("/getAll")
    public ResultBody<List<ProductPackage>> getAll() {
        List<ProductPackage> productPackageList = productPackageService.getAllProductPackage();
        return ResultBody.ok().data(productPackageList);
    }

    /**
     * 根据套餐id获取套餐信息
     */
    @ApiOperation(value = "根据套餐id获取套餐信息", notes = "根据套餐id获取套餐信息")
    @GetMapping("/getById")
    public ResultBody getById(@RequestParam(value = "productPackageId") Long productPackageId) {
        return ResultBody.ok().data(productPackageService.getProductPackageById(productPackageId));
    }

    /**
     * 添加数据
     *
     * @return
     */
    @ApiOperation(value = "添加套餐", notes = "添加套餐")
    @PostMapping("/add")
    public ResultBody add(@RequestParam Map<String, Object> map) {
        productPackageService.add(map);
        return ResultBody.ok();
    }

    /**
     * 添加数据
     *
     * @return
     */
    @ApiOperation(value = "修改套餐", notes = "修改套餐")
    @PostMapping("/update")
    public ResultBody update(@RequestParam Map<String, Object> map) {
        productPackageService.update(map);
        return ResultBody.ok();
    }

    /**
     * 修改套餐的上下架状态
     */
    @ApiOperation(value="修改套餐的上下架状态",notes = "修改套餐的上下架状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name="productPackageId",required = true,value = "套餐id"),
            @ApiImplicitParam(name="onShelves",required = true,value = "上下架状态，0-下架，1-上架")
    })
    @PostMapping("/changeOnShelves")
    public ResultBody changeOnShelves(@RequestParam(value = "productPackageId")Long productPackageId,
                                      @RequestParam(value = "onShelves")Integer onShelves){
        ProductPackage productPackage = productPackageService.getById(productPackageId);
        if(productPackage == null){
            return ResultBody.failed().msg("套餐不存在");
        }
        productPackage.setOnShelves(onShelves);
        productPackageService.updateById(productPackage);
        return ResultBody.ok();
    }

}
