package com.opencloud.base.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.opencloud.base.client.model.entity.Favorite;
import com.opencloud.base.server.service.FavoriteService;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.model.ResultBody;
import com.opencloud.common.security.OpenHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javafx.scene.chart.ValueAxis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 收藏表 前端控制器
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
@Api(value = "收藏表", tags = "收藏表")
    @RestController
@RequestMapping("favorite")
    public class FavoriteController {

    @Autowired
    private FavoriteService targetService;

    /**
    * 获取分页数据
    *
    * @return
    */
    @ApiOperation(value = "获取我的收藏分页数据", notes = "获取我的收藏分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", required = false, value = "页码", paramType = "form", dataType = "int", example = "1"),
            @ApiImplicitParam(name = "limit", required = false, value = "显示条数", paramType = "form", dataType = "int", example = "10"),
    })
    @GetMapping(value = "/list")
    public ResultBody<IPage<Favorite>>list(@RequestParam(required = false) Map map){
        return ResultBody.ok().data(targetService.list(new PageParams(map)));
    }

    @ApiOperation(value = "用户收藏房源", notes = "用户收藏房源")
    @ApiImplicitParams({
         @ApiImplicitParam(name = "houseId", required = true, value = "房源id", paramType = "form")
            })
    @PostMapping("/add")
    public ResultBody add(@RequestParam(value = "houseId") Long houseId){
        Favorite entity = new Favorite();
        entity.setHouseId(houseId);
        entity.setUserId(OpenHelper.getUserId());
        targetService.save(entity);
        return ResultBody.ok();
    }

    @ApiOperation(value = "校验用户是否收藏了该房源" , notes = "校验用户是否收藏了该房源")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "houseId" , name = "houseId")
    })
    @GetMapping("/checkFavorite")
    public ResultBody checkFavorite(@RequestParam("houseId")Long houseId){
        return ResultBody.ok().data(this.checkFavorite(houseId));
    }

    @ApiOperation(value = "用户取消收藏房源" ,notes = "用户取消收藏房源")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "houseId", name = "houseId")
    })
    @PostMapping("/cancelFavorite")
    public ResultBody cancelFavorite(@RequestParam("houseId")Long houseId){
        QueryWrapper<Favorite> favoriteQueryWrapper = new QueryWrapper<>();
        favoriteQueryWrapper.lambda().eq(Favorite::getHouseId,houseId).eq(Favorite::getUserId,OpenHelper.getUserId());
        targetService.remove(favoriteQueryWrapper);
        return ResultBody.ok();
    }
}
