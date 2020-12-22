package com.opencloud.base.server.controller.sale;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.entity.sale.OrderDetail;
import com.opencloud.base.server.service.sale.OrderDetailService;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.model.ResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 订单明细表 前端控制器
 *
 * @author liyueping
 * @date 2019-11-29
 */
@Api(value = "订单明细表", tags = "订单明细表")
@RestController
@RequestMapping("orderDetail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService targetService;

    /**
    * 获取分页数据
    *
    * @return
    */
    @ApiOperation(value = "获取分页数据", notes = "获取分页数据")
    @GetMapping(value = "/list")
    public ResultBody<IPage<OrderDetail>>list(@RequestParam(required = false) Map map){
        PageParams pageParams = new PageParams(map);
        OrderDetail query = pageParams.mapToObject(OrderDetail.class);
        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper();
        return ResultBody.ok().data(targetService.page(pageParams,queryWrapper));
    }

    /**
     * 根据ID查找数据
     */
    @ApiOperation(value = "根据ID查找数据", notes = "根据ID查找数据")
    @ResponseBody
    @RequestMapping("/get")
    public ResultBody<OrderDetail> get(@RequestParam("id") Long id){
        OrderDetail entity = targetService.getById(id);
        return ResultBody.ok().data(entity);
    }

    /**
    * 添加数据
    * @return
    */
    @ApiOperation(value = "添加数据", notes = "添加数据")
    @ApiImplicitParams({
         @ApiImplicitParam(name = "orderId", required = true, value = "订单id", paramType = "form"),
         @ApiImplicitParam(name = "userId", required = true, value = "用户id", paramType = "form"),
         @ApiImplicitParam(name = "productPackageId", required = true, value = "产品套餐id", paramType = "form"),
         @ApiImplicitParam(name = "productPackageName", required = true, value = "产品套餐名称", paramType = "form"),
         @ApiImplicitParam(name = "productPackageNum", required = true, value = "产品套餐数量", paramType = "form"),
         @ApiImplicitParam(name = "originalPrice", required = true, value = "订单总原价", paramType = "form"),
         @ApiImplicitParam(name = "couponAmount", required = true, value = "优惠券抵扣金额", paramType = "form"),
         @ApiImplicitParam(name = "discountedAmount", required = true, value = "优惠后金额", paramType = "form"),
        @ApiImplicitParam(name = "status", required = true, value = "状态:0-无效 1-有效", paramType = "form")
            })
    @PostMapping("/add")
    public ResultBody add(
        @RequestParam(value = "orderId") Long orderId,
        @RequestParam(value = "userId") Long userId,
        @RequestParam(value = "productPackageId") Long productPackageId,
        @RequestParam(value = "productPackageName") String productPackageName,
        @RequestParam(value = "productPackageNum") Integer productPackageNum,
        @RequestParam(value = "originalPrice") BigDecimal originalPrice,
        @RequestParam(value = "couponAmount") BigDecimal couponAmount,
        @RequestParam(value = "discountedAmount") BigDecimal discountedAmount,
        @RequestParam(value = "status") Integer status
            ){
        OrderDetail entity = new OrderDetail();
        entity.setOrderId(orderId);
        entity.setUserId(userId);
        entity.setProductPackageId(productPackageId);
        entity.setProductPackageName(productPackageName);
        entity.setProductPackageNum(productPackageNum);
        entity.setOriginalPrice(originalPrice);
        entity.setCouponAmount(couponAmount);
        entity.setDiscountedAmount(discountedAmount);
        entity.setStatus(status);
        targetService.save(entity);
        return ResultBody.ok();
    }

    /**
    * 更新数据
    * @return
    */
    @ApiOperation(value = "更新数据", notes = "更新数据")
    @ApiImplicitParams({
                    @ApiImplicitParam(name = "orderDetailId", required = true, value = "订单明细id", paramType = "form"),
                    @ApiImplicitParam(name = "orderId", required = true, value = "订单id", paramType = "form"),
                    @ApiImplicitParam(name = "userId", required = true, value = "用户id", paramType = "form"),
                    @ApiImplicitParam(name = "productPackageId", required = true, value = "产品套餐id", paramType = "form"),
                    @ApiImplicitParam(name = "productPackageName", required = true, value = "产品套餐名称", paramType = "form"),
                    @ApiImplicitParam(name = "productPackageNum", required = true, value = "产品套餐数量", paramType = "form"),
                    @ApiImplicitParam(name = "originalPrice", required = true, value = "订单总原价", paramType = "form"),
                    @ApiImplicitParam(name = "couponAmount", required = true, value = "优惠券抵扣金额", paramType = "form"),
                    @ApiImplicitParam(name = "discountedAmount", required = true, value = "优惠后金额", paramType = "form"),
                    @ApiImplicitParam(name = "status", required = true, value = "状态:0-无效 1-有效", paramType = "form")
        })
        @PostMapping("/update")
        public ResultBody add(
                @RequestParam(value = "orderDetailId") Long orderDetailId,
                @RequestParam(value = "orderId") Long orderId,
                @RequestParam(value = "userId") Long userId,
                @RequestParam(value = "productPackageId") Long productPackageId,
                @RequestParam(value = "productPackageName") String productPackageName,
                @RequestParam(value = "productPackageNum") Integer productPackageNum,
                @RequestParam(value = "originalPrice") BigDecimal originalPrice,
                @RequestParam(value = "couponAmount") BigDecimal couponAmount,
                @RequestParam(value = "discountedAmount") BigDecimal discountedAmount,
                @RequestParam(value = "status") Integer status
        ){
            OrderDetail entity = new OrderDetail();
                    entity.setOrderDetailId(orderDetailId);
                    entity.setOrderId(orderId);
                    entity.setUserId(userId);
                    entity.setProductPackageId(productPackageId);
                    entity.setProductPackageName(productPackageName);
                    entity.setProductPackageNum(productPackageNum);
                    entity.setOriginalPrice(originalPrice);
                    entity.setCouponAmount(couponAmount);
                    entity.setDiscountedAmount(discountedAmount);
                    entity.setStatus(status);
                targetService.updateById(entity);
                return ResultBody.ok();
        }

    /**
    * 删除数据
    * @return
    */
    @ApiOperation(value = "删除数据", notes = "删除数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, value = "id", paramType = "form")
    })
    @PostMapping("/remove")
    public ResultBody remove(
            @RequestParam(value = "id") Long id
    ){
            targetService.removeById(id);
            return ResultBody.ok();
      }

    /**
    * 批量删除数据
    * @return
    */
    @ApiOperation(value = "批量删除数据", notes = "批量删除数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", required = true, value = "多个用,号隔开", paramType = "form")
    })
    @PostMapping("/batch/remove")
    public ResultBody batchRemove(
                @RequestParam(value = "ids") String ids
            ){
            targetService.removeByIds(Arrays.asList(ids.split(",")));
            return ResultBody.ok();
     }


    /**
     * 通过orderId获取订单详情列表
     */
    @ApiOperation(value ="用订单id获取订单详情", notes = "用订单id获取订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam( name = "orderId" ,required = true ,value = "订单id" , paramType = "form")
    })
    @GetMapping("/getOrderDetailsByOrderId")
    public ResultBody getOrderDetailsByOrderId(Long orderId){
        return ResultBody.ok().data(targetService.getOrderDetailsByOrderId(orderId));
    }

    @PostMapping("getBuyPaperSumByUserIdList")
    public ResultBody<Map<Long, Integer>> getBuyPaperSumByUserIdList(@RequestParam(value = "userIdList") List<Long> userIdList) {
        Map<Long, Integer> resultMap = targetService.getBuyPaperSumByUserIdList(userIdList);
        return ResultBody.ok().data(resultMap);
    }
}
