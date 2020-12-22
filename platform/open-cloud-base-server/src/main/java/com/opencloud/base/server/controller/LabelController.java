package com.opencloud.base.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.entity.Label;
import com.opencloud.base.server.service.LabelService;
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
 * 房源标签基础表 前端控制器
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
@Api(value = "房源标签基础表", tags = "房源标签基础表")
    @RestController
@RequestMapping("label")
    public class LabelController {

    @Autowired
    private LabelService targetService;

    /**
    * 获取分页数据
    *
    * @return
    */
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    @GetMapping(value = "/list")
    public ResultBody<IPage<Label>>list(@RequestParam(required = false) Map map){
        PageParams pageParams = new PageParams(map);
        Label query = pageParams.mapToObject(Label.class);
        QueryWrapper<Label> queryWrapper = new QueryWrapper();
        return ResultBody.ok().data(targetService.page(pageParams,queryWrapper));
    }

    /**
     * 根据ID查找数据
     */
    @ApiOperation(value = "根据ID查找数据", notes = "根据ID查找数据")
    @ResponseBody
    @RequestMapping("/get")
    public ResultBody<Label> get(@RequestParam("id") Long id){
        Label entity = targetService.getById(id);
        return ResultBody.ok().data(entity);
    }


}
