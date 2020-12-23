package com.opencloud.base.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.entity.GlobalConfig;
import com.opencloud.base.server.service.GlobalConfigService;
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
 * 全局配置 前端控制器
 *
 * @author yanjiajun
 * @date 2020-12-21
 */
@Api(value = "全局配置", tags = "全局配置")
    @RestController
@RequestMapping("globalConfig")
    public class GlobalConfigController {

    @Autowired
    private GlobalConfigService targetService;

    /**
    * 获取分页数据
    *
    * @return
    */
    @ApiOperation(value = "获取全部全局配置", notes = "获取全部全局配置")
    @GetMapping(value = "/list")
    public ResultBody<IPage<GlobalConfig>>list(@RequestParam(required = false) Map map){
        PageParams pageParams = new PageParams(map);
        pageParams.setPage(1);
        pageParams.setSize(9999);
        QueryWrapper<GlobalConfig> queryWrapper = new QueryWrapper();
        return ResultBody.ok().data(targetService.page(pageParams,queryWrapper));
    }

    /**
     * 根据ID查找数据
     */
    @ApiOperation(value = "根据ID查找数据", notes = "根据ID查找数据")
    @ResponseBody
    @GetMapping("/get")
    public ResultBody<GlobalConfig> get(@RequestParam("id") Long id){
        GlobalConfig entity = targetService.getById(id);
        return ResultBody.ok().data(entity);
    }

    /**
    * 添加数据
    * @return
    */
    @ApiOperation(value = "添加数据", notes = "添加数据")
    @ApiImplicitParams({
         @ApiImplicitParam(name = "globalName", required = true, value = "配置名", paramType = "form"),
         @ApiImplicitParam(name = "globalValue", required = true, value = "配置值", paramType = "form")
            })
    @PostMapping("/add")
    public ResultBody add(
        @RequestParam(value = "globalName") String globalName,
        @RequestParam(value = "globalValue") String globalValue
            ){
        GlobalConfig entity = new GlobalConfig();
        entity.setGlobalName(globalName);
        entity.setGlobalValue(globalValue);
        targetService.save(entity);
        return ResultBody.ok();
    }

    /**
    * 更新数据
    * @return
    */
    @ApiOperation(value = "更新数据", notes = "更新数据")
    @ApiImplicitParams({
                    @ApiImplicitParam(name = "globalConfigId", required = true, value = "", paramType = "form"),
                    @ApiImplicitParam(name = "globalName", required = true, value = "配置名", paramType = "form"),
                    @ApiImplicitParam(name = "globalValue", required = true, value = "配置值", paramType = "form")
        })
        @PostMapping("/update")
        public ResultBody add(
                @RequestParam(value = "globalConfigId") Long globalConfigId,
                @RequestParam(value = "globalName") String globalName,
                @RequestParam(value = "globalValue") String globalValue
        ){
            GlobalConfig entity = new GlobalConfig();
                    entity.setGlobalConfigId(globalConfigId);
                    entity.setGlobalName(globalName);
                    entity.setGlobalValue(globalValue);
                targetService.updateById(entity);
                return ResultBody.ok();
        }


}
