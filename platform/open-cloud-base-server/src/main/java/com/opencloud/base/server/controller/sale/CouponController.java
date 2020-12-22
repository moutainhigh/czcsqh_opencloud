package com.opencloud.base.server.controller.sale;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.model.ResultBody;
import com.opencloud.common.model.ServiceResultBody;
import com.opencloud.common.security.OpenHelper;
import com.opencloud.base.client.model.entity.sale.Coupon;
import com.opencloud.base.client.model.entity.sale.CouponProductPackage;
import com.opencloud.base.client.model.entity.sale.Order;
import com.opencloud.base.server.service.sale.CouponProductPackageService;
import com.opencloud.base.server.service.sale.CouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 优惠券表 前端控制器
 *
 * @author liyueping
 * @date 2020-03-09
 */
@Api(value = "优惠券表", tags = "优惠券表")
@RestController
@RequestMapping("coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponProductPackageService couponProductPackageService;


    /**
     * 获取订单分页数据
     *
     * @return
     */
    @ApiOperation(value = "获取优惠券分页数据", notes = "获取优惠券分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", required = false, value = "页码", paramType = "form", dataType = "int", example = "1"),
            @ApiImplicitParam(name = "limit", required = false, value = "显示条数", paramType = "form", dataType = "int", example = "10"),
            @ApiImplicitParam(name = "sort", required = false, value = "排序字段", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "order", required = false, value = "排序方向", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "couponName", required = false, value = "优惠券名称", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "couponTypeId", required = false, value = "优惠券类型id", paramType = "form", dataType = "int", example = "1"),
            @ApiImplicitParam(name = "couponTriggerId", required = false, value = "优惠券类型id", paramType = "form", dataType = "int", example = "1"),
            @ApiImplicitParam(name = "status", required = false, value = "状态:0-下架 1-上架", paramType = "form", dataType = "int", example = "1")})
    @GetMapping(value = "/list")
    public ResultBody<IPage<Order>> list(@RequestParam(required = false) Map map) {
        return ResultBody.ok().data(couponService.findListPage(new PageParams(map)));
    }

    /**
     * 根据ID查找数据
     */
    @ApiOperation(value = "根据ID查找数据", notes = "根据ID查找数据")
    @ResponseBody
    @GetMapping("/get")
    public ResultBody<Coupon> get(@RequestParam("id") Long id) {
        Coupon entity = couponService.getById(id);
        return ResultBody.ok().data(entity);
    }

    /**
     * 添加数据
     *
     * @return
     */
    @ApiOperation(value = "添加数据", notes = "添加数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "couponName", required = true, value = "优惠券名称", paramType = "form"),
            @ApiImplicitParam(name = "couponTotal", required = true, value = "优惠券总数量", paramType = "form"),
            @ApiImplicitParam(name = "couponUserGetTotal", required = true, value = "每人限领取数量，0为无限制", paramType = "form"),
            @ApiImplicitParam(name = "couponUseTimeJson", required = true, value = "优惠券使用时间json", paramType = "form"),
            @ApiImplicitParam(name = "couponTypeId", required = true, value = "优惠券类型id", paramType = "form"),
            @ApiImplicitParam(name = "couponDiscountJson", required = true, value = "优惠券减免条件和金额的json", paramType = "form"),
            @ApiImplicitParam(name = "imageId", required = true, value = "优惠券小图id", paramType = "form"),
            @ApiImplicitParam(name = "imageUrl", required = true, value = "优惠券小图地址", paramType = "form"),
            @ApiImplicitParam(name = "allProductPackage", required = true, value = "是否适用于全部商品", paramType = "form"),
            @ApiImplicitParam(name = "couponTriggerId", required = true, value = "优惠券触发条件id", paramType = "form"),
            @ApiImplicitParam(name = "couponTriggerJson", required = true, value = "优惠券触发条件所需参数", paramType = "form"),
            @ApiImplicitParam(name = "couponDesc", required = true, value = "优惠券客户描述", paramType = "form"),
            @ApiImplicitParam(name = "couponRemark", required = true, value = "优惠券备注信息", paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, value = "状态:0-下架 1-上架", paramType = "form"),
            @ApiImplicitParam(name = "productPackageIdListStr", required = true, value = "触发条件的套餐ids", paramType = "form")
    })
    @PostMapping("/add")
    public ResultBody add(
            @RequestParam(value = "couponName") String couponName,
            @RequestParam(value = "couponTotal") Integer couponTotal,
            @RequestParam(value = "couponUserGetTotal") Integer couponUserGetTotal,
            @RequestParam(value = "couponUseTimeJson") String couponUseTimeJson,
            @RequestParam(value = "couponTypeId") Long couponTypeId,
            @RequestParam(value = "couponDiscountJson") String couponDiscountJson,
            @RequestParam(value = "imageId") Long imageId,
            @RequestParam(value = "imageUrl") String imageUrl,
            @RequestParam(value = "allProductPackage") Integer allProductPackage,
            @RequestParam(value = "couponTriggerId") Long couponTriggerId,
            @RequestParam(value = "couponTriggerJson") String couponTriggerJson,
            @RequestParam(value = "couponDesc") String couponDesc,
            @RequestParam(value = "couponRemark") String couponRemark,
            @RequestParam(value = "status") Integer status,
            @RequestParam(value = "productPackageIdListStr") String productPackageIdListStr
    ) {
        Coupon entity = new Coupon();
        entity.setCouponName(couponName);
        entity.setCouponTotal(couponTotal);
        entity.setCouponUsedTotal(0);
        entity.setCouponUserGetTotal(couponUserGetTotal);
        entity.setCouponUseTimeJson(couponUseTimeJson);
        entity.setCouponTypeId(couponTypeId);
        entity.setCouponDiscountJson(couponDiscountJson);
        entity.setAdminId(OpenHelper.getUserId());
        entity.setImageId(imageId);
        entity.setImageUrl(imageUrl);
        entity.setAllProductPackage(allProductPackage);
        entity.setCouponTriggerId(couponTriggerId);
        entity.setCouponTriggerJson(couponTriggerJson);
        entity.setCouponDesc(couponDesc);
        entity.setCouponRemark(couponRemark);
        entity.setStatus(status);
        couponService.save(entity);
        //保存适用套餐
        List<CouponProductPackage> saveCouponProductPackage = new ArrayList<>();
        List list = JSONObject.parseArray(productPackageIdListStr);
        for (int i = 0; i < list.size(); i++) {
            Long productPackageId = Long.parseLong(list.get(i).toString());
            CouponProductPackage couponProductPackage = new CouponProductPackage();
            couponProductPackage.setCouponId(entity.getCouponId());
            couponProductPackage.setProductPackageId(productPackageId);
            saveCouponProductPackage.add(couponProductPackage);
        }
        if(saveCouponProductPackage.size()>0){
            couponProductPackageService.saveBatch(saveCouponProductPackage);
        }

        return ResultBody.ok();
    }

    /**
     * 更新数据
     *
     * @return
     */
    @ApiOperation(value = "更新数据", notes = "更新数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "couponId", required = true, value = "优惠券id", paramType = "form"),
            @ApiImplicitParam(name = "couponUseTimeJson", required = true, value = "优惠券使用时间json", paramType = "form"),
            @ApiImplicitParam(name = "couponDesc", required = true, value = "优惠券客户描述", paramType = "form"),
            @ApiImplicitParam(name = "couponRemark", required = true, value = "优惠券备注信息", paramType = "form")
    })
    @PostMapping("/update")
    public ResultBody update(
            @RequestParam(value = "couponId") Long couponId,
            @RequestParam(value = "couponUseTimeJson") String couponUseTimeJson,
            @RequestParam(value = "couponDesc") String couponDesc,
            @RequestParam(value = "couponRemark") String couponRemark
    ) {
        Coupon entity = new Coupon();
        entity.setCouponId(couponId);
        entity.setCouponUseTimeJson(couponUseTimeJson);
        entity.setCouponDesc(couponDesc);
        entity.setCouponRemark(couponRemark);
        couponService.updateById(entity);
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
        couponService.removeById(id);
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
        couponService.removeByIds(Arrays.asList(ids.split(",")));
        return ResultBody.ok();
    }


//    @ApiOperation(value = "增加优惠券投放量", notes = "增加优惠券投放量")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "couponId",required = true,value = "优惠券id"),
//            @ApiImplicitParam(name = "addCouponTotalNum",required = true,value = "增加的投放量")
//    })
//    @PostMapping("/addCouponTotal")
//    public ResultBody addCouponTotal(@RequestParam(value = "couponId")Long couponId, @RequestParam(value = "addCouponTotalNum")Integer addCouponTotalNum){
//        Coupon coupon = couponService.getById(couponId);
//        coupon.setCouponTotal(coupon.getCouponTotal() + addCouponTotalNum);
//        couponService.updateById(coupon);
//        return ResultBody.ok();
//    }

    @ApiOperation(value = "优惠券上下架", notes = "优惠券上下架")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "couponId",required = true,value = "优惠券id"),
            @ApiImplicitParam(name = "couponStatus",required = true,value = "要改变的状态")
    })
    @PostMapping("/updateStatus")
    public ResultBody updateStatus(@RequestParam(value = "couponId")Long couponId, @RequestParam(value = "couponStatus")Integer couponStatus){
        Coupon coupon = couponService.getById(couponId);
        coupon.setStatus(couponStatus);
        couponService.updateById(coupon);
        return ResultBody.ok();
    }

    @ApiOperation(value = "发放优惠券", notes = "发放优惠券")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "couponId",required = true,value = "优惠券id"),
            @ApiImplicitParam(name = "userIdListStr",required = true,value = "用户id数组的JSON格式")
    })
    @PostMapping("/giveCoupon")
    public ResultBody giveCoupon(@RequestParam(value = "couponId")Long couponId, @RequestParam(value = "userIdListStr")String userIdListStr){
        ServiceResultBody result =  couponService.giveCoupon(couponId,userIdListStr);
        if(result.getCode() == 0){
            return ResultBody.ok();
        }else {
            return ResultBody.failed().msg(result.getMessage());
        }

    }
}
