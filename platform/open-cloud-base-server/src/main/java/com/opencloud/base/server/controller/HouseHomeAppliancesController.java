package com.opencloud.base.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.entity.HouseHomeAppliances;
import com.opencloud.base.server.service.HouseHomeAppliancesService;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.model.ResultBody;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("houseHomeAppliances")
public class HouseHomeAppliancesController {

    @Autowired
    private HouseHomeAppliancesService targetService;

    /**
     * 获取分页数据
     *
     * @return
     */
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    @GetMapping(value = "/list")
    public ResultBody<IPage<HouseHomeAppliances>> list(@RequestParam(required = false) Map map) {
        PageParams pageParams = new PageParams(map);
        HouseHomeAppliances query = pageParams.mapToObject(HouseHomeAppliances.class);
        QueryWrapper<HouseHomeAppliances> queryWrapper = new QueryWrapper();
        return ResultBody.ok().data(targetService.page(pageParams, queryWrapper));
    }

    /**
     * 添加数据
     *
     * @return
     */
    @ApiOperation(value = "添加数据", notes = "添加数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "houseId", required = true, value = "房源id", paramType = "form"),
            @ApiImplicitParam(name = "homeAppliancesTypeId", required = true, value = "家电id", paramType = "form")
    })
    @PostMapping("/add")
    public ResultBody add(
            @RequestParam(value = "houseId") Long houseId,
            @RequestParam(value = "homeAppliancesTypeId") Long homeAppliancesTypeId
    ) {
        HouseHomeAppliances entity = new HouseHomeAppliances();
        entity.setHouseId(houseId);
        entity.setHomeAppliancesTypeId(homeAppliancesTypeId);
        targetService.save(entity);
        return ResultBody.ok();
    }


    /**
     * 批量删除数据
     *
     * @return
     */
    @ApiOperation(value = "批量删除数据", notes = "批量删除数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", required = true, value = "多个用,号隔开", paramType = "form")
    })
    @PostMapping("/batch/remove")
    public ResultBody batchRemove(
            @RequestParam(value = "ids") String ids
    ) {
        targetService.removeByIds(Arrays.asList(ids.split(",")));
        return ResultBody.ok();
    }

}
