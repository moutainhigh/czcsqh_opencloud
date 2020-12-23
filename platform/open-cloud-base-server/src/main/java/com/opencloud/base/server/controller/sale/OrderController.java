package com.opencloud.base.server.controller.sale;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.entity.sale.Order;
import com.opencloud.base.client.model.entity.sale.ProductPackage;
import com.opencloud.base.server.service.sale.OrderService;
import com.opencloud.base.server.service.sale.ProductPackageService;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.model.ResultBody;
import com.opencloud.common.security.OpenHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 订单表 前端控制器
 *
 * @author liyueping
 * @date 2019-11-28
 */
//@Api(value = "订单表", tags = "订单表")
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductPackageService productPackageService;



    /**
    * 获取订单分页数据
    *
    * @return
    */
    @ApiOperation(value = "获取订单分页数据", notes = "获取订单分页数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", required = false, value = "页码", paramType = "form", dataType = "int", example = "1"),
        @ApiImplicitParam(name = "limit", required = false, value = "显示条数", paramType = "form", dataType = "int", example = "10"),
        @ApiImplicitParam(name = "sort", required = false, value = "排序字段", paramType = "form", dataType = "string"),
        @ApiImplicitParam(name = "order", required = false, value = "排序方向", paramType = "form", dataType = "string"),
        @ApiImplicitParam(name = "orderNo", required = false, value = "订单号", paramType = "form", dataType = "string"),
        @ApiImplicitParam(name = "userRealName", required = false, value = "用户真实姓名", paramType = "form", dataType = "string"),
        @ApiImplicitParam(name = "productPackageId", required = false, value = "购买套餐id", paramType = "form", dataType = "int", example = "1"),
        @ApiImplicitParam(name = "orderStatus", required = false, value = "订单状态", paramType = "form", dataType = "int", example = "1"),
        @ApiImplicitParam(name = "payTimeStart", required = false, value = "支付时间起始", paramType = "form", dataType = "string"),
        @ApiImplicitParam(name = "payTimeEnd", required = false, value = "支付时间结束", paramType = "form", dataType = "string"),
        @ApiImplicitParam(name = "recommendId", required = false, value = "上级返利人员id", paramType = "form", dataType = "int", example = "1"),
        @ApiImplicitParam(name = "see", required = false, value = "可查看数据量", paramType = "form", dataType = "string"),
        @ApiImplicitParam(name = "deviceCode", required = false, value = "设备唯一编码", paramType = "form", dataType = "string") })
    @GetMapping(value = "/list")
    public ResultBody<IPage<Order>> list(@RequestParam(required = false) Map map){
        return ResultBody.ok().data(orderService.findListPage(new PageParams(map)));
    }
    
    /**
     * 获取已支付的订单分页数据
     *
     * @return
     */
     @ApiOperation(value = "获取已支付的订单分页数据", notes = "获取已支付的订单分页数据")
     @ApiImplicitParams({
         @ApiImplicitParam(name = "page", required = false, value = "页码", paramType = "form", dataType = "int", example = "1"),
         @ApiImplicitParam(name = "limit", required = false, value = "显示条数", paramType = "form", dataType = "int", example = "10"),
         @ApiImplicitParam(name = "sort", required = false, value = "排序字段", paramType = "form", dataType = "string"),
         @ApiImplicitParam(name = "order", required = false, value = "排序方向", paramType = "form", dataType = "string") })
     @GetMapping(value = "/listPayDone")
     public ResultBody<IPage<Order>> listPayDone(@RequestParam(required = false) Map map) {
         return ResultBody.ok().data(orderService.listPayDone(new PageParams(map)));
     }


    /**
     * 获取所有订单数据
     *
     * @return
     */
     @ApiOperation(value = "获取所有订单数据", notes = "获取所有订单数据")
     @ApiImplicitParams({
         @ApiImplicitParam(name = "orderNo", required = false, value = "订单号", paramType = "form", dataType = "string"),
         @ApiImplicitParam(name = "userRealName", required = false, value = "用户真实姓名", paramType = "form", dataType = "string"),
         @ApiImplicitParam(name = "productPackageId", required = false, value = "购买套餐id", paramType = "form", dataType = "int", example = "1"),
         @ApiImplicitParam(name = "orderStatus", required = false, value = "订单状态", paramType = "form", dataType = "int", example = "1"),
         @ApiImplicitParam(name = "payTimeStart", required = false, value = "支付时间起始", paramType = "form", dataType = "string"),
         @ApiImplicitParam(name = "payTimeEnd", required = false, value = "支付时间结束", paramType = "form", dataType = "string"),
         @ApiImplicitParam(name = "recommendId", required = false, value = "上级返利人员id", paramType = "form", dataType = "int", example = "1") })
     @GetMapping(value = "/allList")
     public ResultBody<List<Order>> allList(@RequestParam(required = false) Map map){
         return ResultBody.ok().data(orderService.allList(new PageParams(map)));
     }

    /**
     * 根据ID查找数据
     */
    @ApiOperation(value = "根据ID查找数据", notes = "根据ID查找数据")
    @ResponseBody
    @GetMapping("/get")
    public ResultBody<Map<String, Object>> get(@RequestParam("id") Long id){
        Map<String, Object> orderMap = orderService.getOrderById(id);
        return ResultBody.ok().data(orderMap);
    }

    
    /**
     * 确认支付
     * @return
     */
     @ApiOperation(value = "确认支付", notes = "确认支付")
     @ApiImplicitParams({
          @ApiImplicitParam(name = "orderNo", required = true, value = "订单编号", paramType = "form", dataType = "string", example = "1")
             })
     @ResponseBody
     @PostMapping("/payDone")
     public ResultBody payDone(
         @RequestParam(value = "orderNo") String orderNo
             ){
         orderService.payDone(orderNo);
         return ResultBody.ok();
     }
     


    /**
     * 前台创建订单
     */
    @ApiOperation(value = "前端app生成订单", notes = "前端app生成订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userAddressId",required = true,value = "用户收货地址id",paramType = "form"),
            @ApiImplicitParam(name="productPackageId",required = true,value = "套餐id",paramType = "form"),
            @ApiImplicitParam(name="userCouponId",required = false,value = "用户优惠券id",paramType = "form"),
            @ApiImplicitParam(name="productPackageNum",required = true,value = "购买套餐的数量",paramType = "form"),
            @ApiImplicitParam(name="orderRemark",required = true,value = "订单备注",paramType = "form"),
    })
    @PostMapping("/createOrder")
    public ResultBody createOrder(@RequestParam(value = "userAddressId",required = true) Long userAddressId,
                                  @RequestParam(value = "productPackageId",required = true)Long productPackageId,
                                  @RequestParam(value = "userCouponId",required = false)Long userCouponId,
                                  @RequestParam(value = "orderRemark",required = false)String orderRemark,
                                  @RequestParam(value = "productPackageNum",required = true)Integer productPackageNum){
        Map result = orderService.createOrder(userAddressId, productPackageId, userCouponId, productPackageNum, orderRemark);
        if("1".equals(result.get("code").toString())){
            return ResultBody.ok().data(result.get("data"));
        }else {
            return ResultBody.failed().msg(result.get("msg").toString());
        }
    }

    /**
     * 获取自己所有的订单
     */
    @ApiOperation(value="获取自己所有的订单",notes = "获取自己所有的订单")
    @GetMapping("/getMyAllOrders")
    public ResultBody getMyAllOrders(){
        return ResultBody.ok().data(orderService.getMyAllOrders());
    }

    /**
     * 取消订单
     */
    @ApiOperation(value ="取消订单",notes = "取消订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId",required = true,value = "订单号")
    })
    @PostMapping("/cancelOrder")
    public ResultBody cancelOrder(Long orderId){
        Boolean result = orderService.cancelOrder(orderId);
        return result?ResultBody.ok():ResultBody.failed().msg("取消订单失败");
    }

    
    /**
     * 获取对应条件的订单数量
     *
     * @return
     */
     @ApiOperation(value = "获取对应条件的订单数量", notes = "获取对应条件的订单数量")
     @ApiImplicitParams({
         @ApiImplicitParam(name = "productPackageId", required = false, value = "购买套餐id", paramType = "form", dataType = "int", example = "1") })
     @GetMapping(value = "/countOrder")
     public ResultBody<Integer> countOrder(@RequestParam(required = false) Map map){
         return ResultBody.ok().data(orderService.countOrder(new PageParams(map)));
     }

}
