package com.opencloud.base.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.entity.HouseFile;
import com.opencloud.base.server.service.HouseFileService;
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
 * 房源文件表（图片，视频） 前端控制器
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
@Api(value = "房源文件表（图片，视频）", tags = "房源文件表（图片，视频）")
@RestController
@RequestMapping("houseFile")
public class HouseFileController {

    @Autowired
    private HouseFileService targetService;

    /**
     * 获取分页数据
     *
     * @return
     */
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    @GetMapping(value = "/list")
    public ResultBody<IPage<HouseFile>> list(@RequestParam(required = false) Map map) {
        PageParams pageParams = new PageParams(map);
        HouseFile query = pageParams.mapToObject(HouseFile.class);
        QueryWrapper<HouseFile> queryWrapper = new QueryWrapper();
        return ResultBody.ok().data(targetService.page(pageParams, queryWrapper));
    }

    /**
     * 根据ID查找数据
     */
    @ApiOperation(value = "根据ID查找数据", notes = "根据ID查找数据")
    @ResponseBody
    @RequestMapping("/get")
    public ResultBody<HouseFile> get(@RequestParam("id") Long id) {
        HouseFile entity = targetService.getById(id);
        return ResultBody.ok().data(entity);
    }

    /**
     * 添加数据
     *
     * @return
     */
    @ApiOperation(value = "添加数据", notes = "添加数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "houseId", required = true, value = "", paramType = "form"),
            @ApiImplicitParam(name = "type", required = true, value = "文件类型", paramType = "form"),
            @ApiImplicitParam(name = "fileUrl", required = true, value = "文件路径", paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, value = "文件状态", paramType = "form")
    })
    @PostMapping("/add")
    public ResultBody add(
            @RequestParam(value = "houseId") Long houseId,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "fileUrl") String fileUrl,
            @RequestParam(value = "status") Integer status
    ) {
        HouseFile entity = new HouseFile();
        entity.setHouseId(houseId);
        entity.setType(type);
        entity.setFileUrl(fileUrl);
        entity.setStatus(status);
        targetService.save(entity);
        return ResultBody.ok();
    }

    /**
     * 更新数据
     *
     * @return
     */
    @ApiOperation(value = "更新数据", notes = "更新数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "houseFileId", required = true, value = "", paramType = "form"),
            @ApiImplicitParam(name = "houseId", required = true, value = "", paramType = "form"),
            @ApiImplicitParam(name = "type", required = true, value = "文件类型", paramType = "form"),
            @ApiImplicitParam(name = "fileUrl", required = true, value = "文件路径", paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, value = "文件状态", paramType = "form")
    })
    @PostMapping("/update")
    public ResultBody add(
            @RequestParam(value = "houseFileId") Long houseFileId,
            @RequestParam(value = "houseId") Long houseId,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "fileUrl") String fileUrl,
            @RequestParam(value = "status") Integer status
    ) {
        HouseFile entity = new HouseFile();
        entity.setHouseFileId(houseFileId);
        entity.setHouseId(houseId);
        entity.setType(type);
        entity.setFileUrl(fileUrl);
        entity.setStatus(status);
        targetService.updateById(entity);
        return ResultBody.ok();
    }

    /**
     * 删除数据
     *
     * @return
     */
    @ApiOperation(value = "删除数据", notes = "删除数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, value = "id", paramType = "form")
    })
    @PostMapping("/remove")
    public ResultBody remove(
            @RequestParam(value = "id") Long id
    ) {
        targetService.removeById(id);
        return ResultBody.ok();
    }


}
