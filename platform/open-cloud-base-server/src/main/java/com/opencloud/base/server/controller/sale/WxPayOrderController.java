package com.opencloud.base.server.controller.sale;

import com.alibaba.fastjson.JSONObject;
import com.opencloud.base.client.model.entity.sale.WxPayOrder;
import com.opencloud.base.server.service.sale.WxPayOrderService;
import com.opencloud.common.model.ResultBody;
import com.opencloud.common.utils.BeanConvertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单表 前端控制器
 *
 * @author liyueping
 * @date 2019-11-28
 */
//@Api(value = "微信支付实际单号表", tags = "微信支付实际单号表")
@RestController
@RequestMapping("wxPayOrder")
public class WxPayOrderController{

    @Autowired
    private WxPayOrderService wxPayOrderService;

    @ApiOperation(value = "校验订单在是否已微信支付",notes = "校验订单在是否已微信支付")
    @ResponseBody
    @GetMapping("/checkOrderPayStatus")
    public ResultBody<Boolean> checkOrderPayStatus(@RequestParam(value = "orderId")Long orderId){
        return ResultBody.ok().data(wxPayOrderService.checkOrderPayStatus(orderId));
    }

    @ApiOperation(value= "其他模块调用保存",notes = "其他模块调用保存")
    @PostMapping("/add")
    public ResultBody add(@RequestParam(value = "objectJsonStr")String objectJsonStr){
        //JSON转map
        JSONObject jsonObject = JSONObject.parseObject(objectJsonStr);
        //map转对象
        WxPayOrder wxPayOrder = BeanConvertUtils.mapToObject(jsonObject, WxPayOrder.class);
        wxPayOrderService.save(wxPayOrder);
        return ResultBody.ok();
    }

    @ApiOperation(value="通过实际单号查询表记录",notes = "通过实际单号查询表记录")
    @ResponseBody
    @GetMapping("/getByRealOrderNo")
    public ResultBody getByRealOrderNo(@RequestParam(value = "realOrderNo")String realOrderNo){
        return ResultBody.ok().data(wxPayOrderService.getByRealOrderNo(realOrderNo));
    }

    @ApiOperation(value = "修改记录为已支付",notes = "修改记录为已支付")
    @ResponseBody
    @PostMapping("/updatePayStatusByRealOrderNo")
    public ResultBody updatePayStatusByRealOrderNo(@RequestParam(value = "realOrderNo")String realOrderNo,@RequestParam(value = "resultJson")String resultJson){
        wxPayOrderService.updatePayStatusByRealOrderNo(realOrderNo,resultJson);
        return ResultBody.ok();
    }
}
