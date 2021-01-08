package com.opencloud.base.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.opencloud.base.client.constants.BaseConstants;
import com.opencloud.base.client.model.entity.BaseUser;
import com.opencloud.base.client.model.entity.House;
import com.opencloud.base.client.model.entity.HouseOwner;
import com.opencloud.base.server.service.BaseUserService;
import com.opencloud.base.server.service.HouseOwnerService;
import com.opencloud.base.server.service.HouseService;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.model.ResultBody;
import com.opencloud.common.security.OpenHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 房源表 前端控制器
 *
 * @author yanjiajun
 * @date 2020-10-19
 */
@Api(value = "房源表", tags = "房源表")
@RestController
@RequestMapping("house")
public class HouseController {

    @Autowired
    private HouseService targetService;
    @Autowired
    private BaseUserService userService;
    @Autowired
    private HouseOwnerService houseOwnerService;

    /**
     * 获取分页数据
     *
     * @return
     */
    @ApiOperation(value = "房源分页查询", notes = "首页房源列表的查询功能。提供租赁类型、户型、租金和地区的条件查询。")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "租赁类型，整租、合租等等" , name = "rentType" ,required = false),
            @ApiImplicitParam(value = "户型" , name = "layout" ,required = false),
            @ApiImplicitParam(value = "最大租金" , name = "maxRental" ,required = false),
            @ApiImplicitParam(value = "最小租金" , name = "minRental" ,required = false),
            @ApiImplicitParam(value = "区域" , name = "district" ,required = false),
            @ApiImplicitParam(value = "当前页数" , name = "page" ,required = false ,defaultValue = "1"),
            @ApiImplicitParam(value = "每页条数" , name = "limit" ,required = false ,defaultValue = "10"),
    })
    @GetMapping(value = "/list")
    public ResultBody<IPage<House>> list(@RequestParam(required = false) Map map) {
        return ResultBody.ok().data(targetService.findListPage(new PageParams(map)));
    }

    /**
     * 根据ID查找数据
     */
    @ApiOperation(value = "使用房源id获取房源记录", notes = "使用房源id获取房源记录")
    @ApiImplicitParams({@ApiImplicitParam(value = "房源id" , name = "houseId" ,required = true)})
    @ResponseBody
    @GetMapping("/getDetailById")
    public ResultBody<House> getDetailById(@RequestParam("id") Long id) {
        House entity = targetService.getDetailById(id);
        return ResultBody.ok().data(entity);
    }

    /**
     * 添加数据
     *
     * @return
     */
    @ApiOperation(value = "添加房源", notes = "添加房源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "houseOwnerId", required = true, value = "房东id", paramType = "form"),
            @ApiImplicitParam(name = "userId", required = true, value = "用户id", paramType = "form"),
            @ApiImplicitParam(name = "rental", required = true, value = "租金", paramType = "form"),
            @ApiImplicitParam(name = "waterRate", required = true, value = "水费", paramType = "form"),
            @ApiImplicitParam(name = "powerRate", required = true, value = "电费", paramType = "form"),
            @ApiImplicitParam(name = "floor", required = true, value = "楼层", paramType = "form"),
            @ApiImplicitParam(name = "area", required = true, value = "面积", paramType = "form"),
            @ApiImplicitParam(name = "roadside", required = true, value = "路边距离", paramType = "form"),
            @ApiImplicitParam(name = "detail", required = true, value = "房源详情，富文本", paramType = "form"),
            @ApiImplicitParam(name = "remark", required = true, value = "备注", paramType = "form"),
            @ApiImplicitParam(name = "province", required = true, value = "房源所在省份", paramType = "form"),
            @ApiImplicitParam(name = "city", required = true, value = "房源所在城市", paramType = "form"),
            @ApiImplicitParam(name = "district", required = true, value = "房源所在区域", paramType = "form"),
            @ApiImplicitParam(name = "addressDetail", required = true, value = "房源具体地址", paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, value = "房源状态", paramType = "form")
    })
    @PostMapping("/add")
    public ResultBody add(
            @RequestParam(value = "houseOwnerId") Long houseOwnerId,
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "rental") BigDecimal rental,
            @RequestParam(value = "waterRate") BigDecimal waterRate,
            @RequestParam(value = "powerRate") BigDecimal powerRate,
            @RequestParam(value = "floor") Integer floor,
            @RequestParam(value = "area") BigDecimal area,
            @RequestParam(value = "roadside") String roadside,
            @RequestParam(value = "detail") String detail,
            @RequestParam(value = "remark") String remark,
            @RequestParam(value = "province") String province,
            @RequestParam(value = "city") String city,
            @RequestParam(value = "district") String district,
            @RequestParam(value = "addressDetail") String addressDetail,
            @RequestParam(value = "status") Integer status
    ) {
        House entity = new House();
        entity.setHouseOwnerId(houseOwnerId);
        entity.setUserId(userId);
        entity.setRental(rental);
        entity.setWaterRate(waterRate);
        entity.setPowerRate(powerRate);
        entity.setFloor(floor);
        entity.setArea(area);
        entity.setRoadside(roadside);
        entity.setDetail(detail);
        entity.setRemark(remark);
        entity.setProvince(province);
        entity.setCity(city);
        entity.setDistrict(district);
        entity.setAddressDetail(addressDetail);
        entity.setStatus(status);
        targetService.save(entity);
        return ResultBody.ok();
    }

    /**
     * 更新数据
     *
     * @return
     */
    @ApiOperation(value = "更新房源", notes = "更新房源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "houseId", required = true, value = "", paramType = "form"),
            @ApiImplicitParam(name = "houseOwnerId", required = true, value = "房东id", paramType = "form"),
            @ApiImplicitParam(name = "userId", required = true, value = "用户id", paramType = "form"),
            @ApiImplicitParam(name = "rental", required = true, value = "租金", paramType = "form"),
            @ApiImplicitParam(name = "waterRate", required = true, value = "水费", paramType = "form"),
            @ApiImplicitParam(name = "powerRate", required = true, value = "电费", paramType = "form"),
            @ApiImplicitParam(name = "floor", required = true, value = "楼层", paramType = "form"),
            @ApiImplicitParam(name = "area", required = true, value = "面积", paramType = "form"),
            @ApiImplicitParam(name = "roadside", required = true, value = "路边距离", paramType = "form"),
            @ApiImplicitParam(name = "detail", required = true, value = "房源详情，富文本", paramType = "form"),
            @ApiImplicitParam(name = "remark", required = true, value = "备注", paramType = "form"),
            @ApiImplicitParam(name = "province", required = true, value = "房源所在省份", paramType = "form"),
            @ApiImplicitParam(name = "city", required = true, value = "房源所在城市", paramType = "form"),
            @ApiImplicitParam(name = "district", required = true, value = "房源所在区域", paramType = "form"),
            @ApiImplicitParam(name = "addressDetail", required = true, value = "房源具体地址", paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, value = "房源状态", paramType = "form")
    })
    @PostMapping("/update")
    public ResultBody update(
            @RequestParam(value = "houseId") Long houseId,
            @RequestParam(value = "houseOwnerId") Long houseOwnerId,
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "rental") BigDecimal rental,
            @RequestParam(value = "waterRate") BigDecimal waterRate,
            @RequestParam(value = "powerRate") BigDecimal powerRate,
            @RequestParam(value = "floor") Integer floor,
            @RequestParam(value = "area") BigDecimal area,
            @RequestParam(value = "roadside") String roadside,
            @RequestParam(value = "detail") String detail,
            @RequestParam(value = "remark") String remark,
            @RequestParam(value = "province") String province,
            @RequestParam(value = "city") String city,
            @RequestParam(value = "district") String district,
            @RequestParam(value = "addressDetail") String addressDetail,
            @RequestParam(value = "status") Integer status
    ) {
        House entity = new House();
        entity.setHouseId(houseId);
        entity.setHouseOwnerId(houseOwnerId);
        entity.setUserId(userId);
        entity.setRental(rental);
        entity.setWaterRate(waterRate);
        entity.setPowerRate(powerRate);
        entity.setFloor(floor);
        entity.setArea(area);
        entity.setRoadside(roadside);
        entity.setDetail(detail);
        entity.setRemark(remark);
        entity.setProvince(province);
        entity.setCity(city);
        entity.setDistrict(district);
        entity.setAddressDetail(addressDetail);
        entity.setStatus(status);
        targetService.updateById(entity);
        return ResultBody.ok();
    }

    @ApiOperation(value = "我的房源列表" , notes = "分页获取我的房源列表，该接口房东可用")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前页数" , name = "page" ,required = false),
            @ApiImplicitParam(value = "每页条数" , name = "limit" ,required = false)
    })
    @GetMapping("/getMyHouseList")
    public ResultBody<IPage> getMyHouseList(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                            @RequestParam(value = "limit",defaultValue = "10")Integer limit){
        //先判断是不是房东身份
        BaseUser user = userService.getUserById(OpenHelper.getUserId());
        if(ObjectUtils.isNotEmpty(user) && user.getUserType().equals(BaseConstants.USER_TYPE_HOUSE_OWNER)){
            //获取自身的房东id
            QueryWrapper<HouseOwner> houseOwnerQueryWrapper = new QueryWrapper<>();
            houseOwnerQueryWrapper.lambda().eq(HouseOwner::getUserId,user.getUserId());
            HouseOwner houseOwner = houseOwnerService.getOne(houseOwnerQueryWrapper);

            PageParams pageParams = new PageParams();
            pageParams.setLimit(limit);
            pageParams.setPage(page);

            QueryWrapper<House> houseQueryWrapper = new QueryWrapper<>();
            houseQueryWrapper.lambda().eq(House::getHouseOwnerId,houseOwner.getHouseOwnerId()).orderByDesc(House::getCreateTime);
            return ResultBody.ok().data(targetService.page(pageParams,houseQueryWrapper));
        }
        return ResultBody.failed().msg("身份信息有误");
    }

    @ApiOperation(value = "根据房源id获取房源信息（批量）" ,notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "houseIdList" ,value = "房源id列表" ,dataType = "int")
    })
    @GetMapping("/getHouseByIdBatch")
    public ResultBody getHouseByIdBatch(@RequestParam(value = "houseIdList")List<Long> houseIdList){
        return ResultBody.ok().data(targetService.getHouseByIdBatch(houseIdList));
    }
}
