package com.opencloud.base.server.service.impl.sale;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.opencloud.base.client.model.entity.sale.WxPayOrder;
import com.opencloud.base.server.mapper.sale.WxPayOrderMapper;
import com.opencloud.base.server.service.sale.WxPayOrderService;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.common.utils.WeChatUtils.pay.constants.WXPayConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单表 服务实现类
 *
 * @author liyueping
 * @date 2019-11-28
 */
@Service
@SuppressWarnings("unchecked")
@Transactional(rollbackFor = Exception.class)
public class WxPayOrderServiceImpl extends BaseServiceImpl<WxPayOrderMapper, WxPayOrder> implements WxPayOrderService {

    @Autowired
    private WxPayOrderMapper wxPayOrderMapper;

    @Override
    public Boolean checkOrderPayStatus(Long orderId) {
        if(orderId == null){
            return false;
        }
        QueryWrapper<WxPayOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WxPayOrder::getOrderId,orderId).eq(WxPayOrder::getResult, WXPayConstants.SUCCESS);
        Integer count = wxPayOrderMapper.selectCount(queryWrapper);
        if(count == 0){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public WxPayOrder getByRealOrderNo(String realOrderNo) {
        QueryWrapper<WxPayOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WxPayOrder::getRealOrderNo,realOrderNo);
        WxPayOrder wxPayOrder = wxPayOrderMapper.selectOne(queryWrapper);
        return wxPayOrder;
    }

    @Override
    public void updatePayStatusByRealOrderNo(String realOrderNo,String resultJson) {
        QueryWrapper<WxPayOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WxPayOrder::getRealOrderNo,realOrderNo);
        WxPayOrder wxPayOrder = wxPayOrderMapper.selectOne(queryWrapper);
        wxPayOrder.setResult(WXPayConstants.SUCCESS);
        wxPayOrder.setResultJson(resultJson);
        wxPayOrderMapper.updateById(wxPayOrder);
    }
}
