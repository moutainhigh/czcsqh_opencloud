package com.opencloud.base.server.service.sale;


import com.opencloud.base.client.model.entity.sale.UserProductItem;
import com.opencloud.common.mybatis.base.service.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * 用户产品表 服务类
 *
 * @author liyueping
 * @date 2019-11-29
 */
public interface UserProductItemService extends IBaseService<UserProductItem> {

    /**
     * 查询用户试纸总数
     * @param id
     * @return
     */
    Integer getProductTotalByUserId(Long id);

    /**
     * 绑定设备时，增加对应的产品数量
     * @param productPackageId
     */
    void addWhenBindingDevice(Long productPackageId,Long deviceId ,Long userId ,Long orderId);

    /**
     * 设备退货时，删除掉该设备的用户拥有的产品数量
     */
    void delWhenReturnDevice(Long userId, Long deviceId, Long productPackageId);

    /**
     * 新产品id替换旧产品id
     */
    void exchangeDeviceId(Long oldDeviceId,Long newDeviceId);

    /**
     * Excel查询剩余试纸数
     */
    Map<Long,List<Object>> surplusProductItemNum(List<Long> deviceIds);

    /**
     * 处理过期的免费试纸
     */
    void dualExpiredProductItem();

    /**
     *
     */
    List<UserProductItem> getUserProductItemByDeviceId(Long deviceId);
}
