package com.opencloud.base.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.entity.KingKong;
import com.opencloud.base.server.service.KingKongService;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.model.ResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 金刚区 前端控制器
 *
 * @author yanjiajun
 * @date 2020-12-22
 */
@Api(value = "金刚区", tags = "金刚区")
@RestController
@RequestMapping("kingKong")
public class KingKongController {

    @Autowired
    private KingKongService targetService;

    /**
     * 获取分页数据
     *
     * @return
     */
    @ApiOperation(value = "获取所有金刚区数据", notes = "获取所有金刚区数据")
    @GetMapping(value = "/list")
    public ResultBody<IPage<KingKong>> list(@RequestParam(required = false) Map map) {
        PageParams pageParams = new PageParams(map);
        pageParams.setSize(9999);
        pageParams.setPage(1);
        QueryWrapper<KingKong> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().orderByAsc(KingKong::getSort);
        return ResultBody.ok().data(targetService.page(pageParams, queryWrapper));
    }

    /**
     * 添加数据
     *
     * @return
     */
    @ApiOperation(value = "添加金刚区数据", notes = "添加金刚区数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", required = true, value = "名称", paramType = "form"),
            @ApiImplicitParam(name = "bgColor", required = true, value = "颜色", paramType = "form"),
            @ApiImplicitParam(name = "imgSrc", required = true, value = "路径", paramType = "form"),
            @ApiImplicitParam(name = "sort", required = true, value = "排序", paramType = "form")
    })
    @PostMapping("/add")
    public ResultBody add(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "bgColor") String bgColor,
            @RequestParam(value = "imgSrc") String imgSrc,
            @RequestParam(value = "sort") Integer sort
    ) {
        KingKong entity = new KingKong();
        entity.setName(name);
        entity.setBgColor(bgColor);
        entity.setImgSrc(imgSrc);
        entity.setSort(sort);
        targetService.save(entity);
        return ResultBody.ok();
    }

    /**
     * 更新数据
     *
     * @return
     */
    @ApiOperation(value = "更新金刚区数据", notes = "更新金刚区数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kingKongId", required = true, value = "", paramType = "form"),
            @ApiImplicitParam(name = "name", required = true, value = "名称", paramType = "form"),
            @ApiImplicitParam(name = "bgColor", required = true, value = "颜色", paramType = "form"),
            @ApiImplicitParam(name = "imgSrc", required = true, value = "路径", paramType = "form"),
            @ApiImplicitParam(name = "sort", required = true, value = "排序", paramType = "form")
    })
    @PostMapping("/update")
    public ResultBody add(
            @RequestParam(value = "kingKongId") Long kingKongId,
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "bgColor",required = false) String bgColor,
            @RequestParam(value = "imgSrc",required = false) String imgSrc,
            @RequestParam(value = "sort",required = false) Integer sort
    ) {
        KingKong entity = new KingKong();
        entity.setKingKongId(kingKongId);
        entity.setName(name);
        entity.setBgColor(bgColor);
        entity.setImgSrc(imgSrc);
        entity.setSort(sort);
        targetService.updateById(entity);
        return ResultBody.ok();
    }

    /**
     * 删除数据
     *
     * @return
     */
    @ApiOperation(value = "删除金刚区数据", notes = "删除金刚区数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, value = "id", paramType = "form")
    })
    @PostMapping("/remove")
    public ResultBody remove(@RequestParam(value = "id") Long id) {
        targetService.removeById(id);
        return ResultBody.ok();
    }


}
