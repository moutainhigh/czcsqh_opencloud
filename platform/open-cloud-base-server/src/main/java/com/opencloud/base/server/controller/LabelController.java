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
    @ApiOperation(value = "获取所有房源标签", notes = "获取所有房源标签")
    @GetMapping(value = "/list")
    public ResultBody<IPage<Label>>list(){
        return ResultBody.ok().data(targetService.list());
    }

    @ApiOperation(value = "增加房源标签",notes = "增加房源标签")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "标签文字" ,name = "labelText" ,required = true)
    })
    @PostMapping("/add")
    public ResultBody add(@RequestParam("labelText")String labelText){
        Label label = new Label();
        label.setLabelText(labelText);
        targetService.save(label);
        return ResultBody.ok();
    }

    @ApiOperation(value = "删除房源标签" ,notes = "删除房源标签")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "labelId",name = "labelId",required = true)
    })
    @PostMapping("/del")
    public ResultBody delete(@RequestParam("labelId")Long labelId){
        targetService.removeById(labelId);
        return ResultBody.ok();
    }

}
