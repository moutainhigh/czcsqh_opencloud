package com.opencloud.base.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.entity.Subscribe;
import com.opencloud.base.server.service.SubscribeService;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.model.ResultBody;
import com.opencloud.common.security.OpenHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.Map;

/**
 * 预约表 前端控制器
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
@Api(value = "预约表", tags = "预约表")
    @RestController
@RequestMapping("subscribe")
    public class SubscribeController {

    @Autowired
    private SubscribeService targetService;

    /**
    * 获取分页数据
    *
    * @return
    */
    @ApiOperation(value = "获取我的约看分页数据", notes = "获取我的约看分页数据,普通用户调用时获取的是用户自身预约的列表，房东调用时获取的是该房东被预约的列表")
    @GetMapping(value = "/list")
    public ResultBody<IPage<Subscribe>>list(@RequestParam(required = false) Map map){
        return ResultBody.ok().data(targetService.list(new PageParams(map)));
    }

    /**
    * 添加数据
    * @return
    */
    @ApiOperation(value = "提交预约看房申请", notes = "提交预约看房申请")
    @ApiImplicitParams({
         @ApiImplicitParam(name = "houseId", required = true, value = "房源id", paramType = "form")
            })
    @PostMapping("/add")
    public ResultBody add(@RequestParam(value = "houseId") Long houseId){
        targetService.add(houseId);
        return ResultBody.ok();
    }

    @ApiOperation(value = "用户取消预约" ,notes = "用户取消预约")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subscribeId", required = true, value = "预约id", paramType = "form")
    })
    @PostMapping("/cancel")
    public ResultBody cancel(@RequestParam("subscribeId")Long subscribeId){
        Subscribe subscribe = targetService.getById(subscribeId);
        //判断是否用户本人取消的
        if(subscribe.getUserId().equals(OpenHelper.getUserId())){
            subscribe.setStatus(0);
            targetService.updateById(subscribe);
            return ResultBody.ok();
        }else {
            return ResultBody.failed();
        }
    }

    @ApiOperation(value = "用户取消预约" ,notes = "用户取消预约")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subscribeId", required = true, value = "预约id", paramType = "form")
    })
    @PostMapping("/deleteByUser")
    public ResultBody deleteByUser(@RequestParam("subscribeId")Long subscribeId){
        Subscribe subscribe = targetService.getById(subscribeId);
        //判断是否用户本人删除的
        if(subscribe.getUserId().equals(OpenHelper.getUserId())){
            subscribe.setStatus(-1);
            targetService.updateById(subscribe);
            return ResultBody.ok();
        }else {
            return ResultBody.failed();
        }
    }

    @ApiOperation(value = "房东接收预约" ,notes = "房东接收预约")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subscribeId", required = true, value = "预约id", paramType = "form")
    })
    @PostMapping("/accept")
    public ResultBody accept(@RequestParam("subscribeId")Long subscribeId){
        Subscribe subscribe = targetService.getById(subscribeId);
        //判断是否房东本人拒绝的
        if(subscribe.getHouseOwnerId().equals(OpenHelper.getUserId())){
            subscribe.setStatus(1);
            targetService.updateById(subscribe);
            return ResultBody.ok();
        }else {
            return ResultBody.failed();
        }
    }


    @ApiOperation(value = "房东拒绝预约" ,notes = "房东拒绝预约")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subscribeId", required = true, value = "预约id", paramType = "form")
    })
    @PostMapping("/reject")
    public ResultBody reject(@RequestParam("subscribeId")Long subscribeId){
        Subscribe subscribe = targetService.getById(subscribeId);
        //判断是否房东本人拒绝的
        if(subscribe.getHouseOwnerId().equals(OpenHelper.getUserId())){
            subscribe.setStatus(2);
            targetService.updateById(subscribe);
            return ResultBody.ok();
        }else {
            return ResultBody.failed();
        }
    }

    @ApiOperation(value = "房东删除预约" ,notes = "房东删除预约")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "subscribeId", required = true, value = "预约id", paramType = "form")
    })
    @PostMapping("/deleteByHouseOwner")
    public ResultBody deleteByHouseOwner(@RequestParam("subscribeId")Long subscribeId){
        Subscribe subscribe = targetService.getById(subscribeId);
        //判断是否房东本人拒绝的
        if(subscribe.getHouseOwnerId().equals(OpenHelper.getUserId())){
            subscribe.setStatus(-1);
            targetService.updateById(subscribe);
            return ResultBody.ok();
        }else {
            return ResultBody.failed();
        }
    }
}
