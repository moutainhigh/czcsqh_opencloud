package com.opencloud.base.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.entity.HouseOwner;
import com.opencloud.base.server.service.HouseOwnerService;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.model.ResultBody;
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
 * 房东表 前端控制器
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
@Api(value = "房东表", tags = "房东表")
    @RestController
@RequestMapping("houseOwner")
    public class HouseOwnerController {

    @Autowired
    private HouseOwnerService targetService;

    /**
    * 获取分页数据
    *
    * @return
    */
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    @GetMapping(value = "/list")
    public ResultBody<IPage<HouseOwner>>list(@RequestParam(required = false) Map map){
        PageParams pageParams = new PageParams(map);
        HouseOwner query = pageParams.mapToObject(HouseOwner.class);
        QueryWrapper<HouseOwner> queryWrapper = new QueryWrapper();
        return ResultBody.ok().data(targetService.page(pageParams,queryWrapper));
    }

    /**
     * 根据ID查找数据
     */
    @ApiOperation(value = "根据ID查找数据", notes = "根据ID查找数据")
    @ResponseBody
    @RequestMapping("/get")
    public ResultBody<HouseOwner> get(@RequestParam("id") Long id){
        HouseOwner entity = targetService.getById(id);
        return ResultBody.ok().data(entity);
    }

    /**
    * 添加数据
    * @return
    */
    @ApiOperation(value = "添加数据", notes = "添加数据")
    @ApiImplicitParams({
         @ApiImplicitParam(name = "userId", required = true, value = "用户id", paramType = "form"),
        @ApiImplicitParam(name = "status", required = true, value = "房东状态，0-未审核通过，1-审核通过", paramType = "form")
            })
    @PostMapping("/add")
    public ResultBody add(
        @RequestParam(value = "userId") Long userId,
        @RequestParam(value = "status") Integer status
            ){
        HouseOwner entity = new HouseOwner();
        entity.setUserId(userId);
        entity.setStatus(status);
        targetService.save(entity);
        return ResultBody.ok();
    }

    /**
    * 更新数据
    * @return
    */
    @ApiOperation(value = "更新数据", notes = "更新数据")
    @ApiImplicitParams({
                    @ApiImplicitParam(name = "houseOwnerId", required = true, value = "房东id", paramType = "form"),
                    @ApiImplicitParam(name = "userId", required = true, value = "用户id", paramType = "form"),
                    @ApiImplicitParam(name = "status", required = true, value = "房东状态，0-未审核通过，1-审核通过", paramType = "form")
        })
        @PostMapping("/update")
        public ResultBody add(
                @RequestParam(value = "houseOwnerId") Long houseOwnerId,
                @RequestParam(value = "userId") Long userId,
                @RequestParam(value = "status") Integer status
        ){
            HouseOwner entity = new HouseOwner();
                    entity.setHouseOwnerId(houseOwnerId);
                    entity.setUserId(userId);
                    entity.setStatus(status);
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
