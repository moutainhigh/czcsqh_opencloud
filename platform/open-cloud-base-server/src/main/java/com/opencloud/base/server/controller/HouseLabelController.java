package com.opencloud.base.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.entity.HouseLabel;
import com.opencloud.base.server.service.HouseLabelService;
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
 * 房源标签关系表 前端控制器
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
@Api(value = "房源标签关系表", tags = "房源标签关系表")
    @RestController
@RequestMapping("houseLabel")
    public class HouseLabelController {

    @Autowired
    private HouseLabelService targetService;

    /**
    * 获取分页数据
    *
    * @return
    */
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    @GetMapping(value = "/list")
    public ResultBody<IPage<HouseLabel>>list(@RequestParam(required = false) Map map){
        PageParams pageParams = new PageParams(map);
        HouseLabel query = pageParams.mapToObject(HouseLabel.class);
        QueryWrapper<HouseLabel> queryWrapper = new QueryWrapper();
        return ResultBody.ok().data(targetService.page(pageParams,queryWrapper));
    }

    /**
    * 添加数据
    * @return
    */
    @ApiOperation(value = "添加数据", notes = "添加数据")
    @ApiImplicitParams({
         @ApiImplicitParam(name = "houseId", required = true, value = "房源id", paramType = "form"),
         @ApiImplicitParam(name = "labelId", required = true, value = "标签id", paramType = "form"),
        @ApiImplicitParam(name = "labelText", required = true, value = "标签文本", paramType = "form")
            })
    @PostMapping("/add")
    public ResultBody add(
        @RequestParam(value = "houseId") Long houseId,
        @RequestParam(value = "labelId") Long labelId,
        @RequestParam(value = "labelText") String labelText
            ){
        HouseLabel entity = new HouseLabel();
        entity.setHouseId(houseId);
        entity.setLabelId(labelId);
        entity.setLabelText(labelText);
        targetService.save(entity);
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
