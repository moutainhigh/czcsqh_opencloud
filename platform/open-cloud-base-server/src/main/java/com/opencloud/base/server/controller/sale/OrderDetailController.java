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
//@Api(value = "订单明细表", tags = "订单明细表")
@RestController
@RequestMapping("orderDetail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService targetService;

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
