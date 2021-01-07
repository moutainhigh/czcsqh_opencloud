package com.opencloud.base.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.opencloud.base.client.model.entity.HomeAppliancesType;
import com.opencloud.base.server.service.HomeAppliancesTypeService;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.model.ResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 家电基础表 前端控制器
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
@Api(value = "家电基础表", tags = "家电基础表")
@RestController
@RequestMapping("homeAppliancesType")
public class HomeAppliancesTypeController {

    @Autowired
    private HomeAppliancesTypeService targetService;

    /**
     * 获取分页数据
     *
     * @return
     */
    @ApiOperation(value = "获取所有家电数据", notes = "获取所有家电数据")
    @GetMapping(value = "/list")
    public ResultBody<List<HomeAppliancesType>> list(@RequestParam(required = false) Map map) {
        QueryWrapper<HomeAppliancesType> queryWrapper = new QueryWrapper();
        return ResultBody.ok().data(targetService.list(queryWrapper));
    }

//    /**
//     * 根据ID查找数据
//     */
//    @ApiOperation(value = "根据ID查找数据", notes = "根据ID查找数据")
//    @ResponseBody
//    @GetMapping("/get")
//    public ResultBody<HomeAppliancesType> get(@RequestParam("id") Long id) {
//        HomeAppliancesType entity = targetService.getById(id);
//        return ResultBody.ok().data(entity);
//    }

}
