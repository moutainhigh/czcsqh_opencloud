package com.opencloud.base.server.controller.sale;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.entity.sale.UserProductItem;
import com.opencloud.base.server.service.sale.UserProductItemService;
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


@Api(value = "用户产品表", tags = "用户产品表")
@RestController
@RequestMapping("userProductItem")
public class UserProductItemController {

    @Autowired
    private UserProductItemService targetService;

    /**
     * 获取分页数据
     *
     * @return
     */
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    @GetMapping(value = "/list")
    public ResultBody<IPage<UserProductItem>> list(@RequestParam(required = false) Map map) {
        PageParams pageParams = new PageParams(map);
        UserProductItem query = pageParams.mapToObject(UserProductItem.class);
        QueryWrapper<UserProductItem> queryWrapper = new QueryWrapper();
        return ResultBody.ok().data(targetService.page(pageParams, queryWrapper));
    }


    /**
     * 查询用户试纸总数
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "查询用户试纸总数", notes = "查询用户试纸总数")
    @ResponseBody
    @GetMapping("/getProductTotalByUserId")
    public ResultBody<UserProductItem> getProductTotalByUserId(@RequestParam("id") Long id) {
        Integer total = targetService.getProductTotalByUserId(id);
        return ResultBody.ok().data(total);
    }


    @ApiOperation(value = "绑定设备时增加用户拥有的产品数量", notes = "绑定设备时增加用户拥有的产品数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "套餐id", value = "productPackageId", required = true),
            @ApiImplicitParam(name = "设备id", value = "deviceId", required = true)
    })
    @PostMapping("/addWhenBindingDevice")
    public ResultBody addWhenBindingDevice(@RequestParam(value = "productPackageId") Long productPackageId,
                                           @RequestParam(value = "deviceId") Long deviceId,
                                           @RequestParam(value = "userId") Long userId,
                                           @RequestParam(value = "orderId" ,required = false) Long orderId) {
        targetService.addWhenBindingDevice(productPackageId, deviceId,userId,orderId);
        return ResultBody.ok();
    }

    @ApiOperation(value = "设备退货时删除掉对应的产品额度", notes = "设备退货时删除掉对应的产品额度")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", required = true),
            @ApiImplicitParam(value = "设备id", name = "deviceId", required = true),
            @ApiImplicitParam(value = "套餐id", name = "productPackageId", required = true)
    })
    @PostMapping("/delWhenReturnDevice")
    public ResultBody delWhenReturnDevice(@RequestParam(value = "userId") Long userId,
                                          @RequestParam(value = "deviceId") Long deviceId,
                                          @RequestParam(value = "productPackageId",required = false) Long productPackageId) {
        targetService.delWhenReturnDevice(userId, deviceId, productPackageId);
        return ResultBody.ok();
    }

    @ApiOperation(value = "免费试纸自动过期",notes = "免费试纸自动过期")
    @GetMapping("/dualExpiredProductItem")
    public ResultBody dualExpiredProductItem(){
        targetService.dualExpiredProductItem();
        return ResultBody.ok();
    }

    @ApiOperation(value = "获取某设备的免费试纸记录" ,notes = "获取某设备的免费试纸记录")
    @GetMapping(value = "/getUserProductItemByDeviceId")
    public ResultBody<List<UserProductItem>> getUserProductItemByDeviceId(@RequestParam(value = "deviceId") Long deviceId){
        List<UserProductItem> list = targetService.getUserProductItemByDeviceId(deviceId);
        return ResultBody.ok().data(list);
    }

}
