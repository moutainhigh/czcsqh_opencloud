package com.opencloud.base.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.entity.House;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.mybatis.base.service.IBaseService;

/**
 * 房源表 服务类
 *
 * @author yanjiajun
 * @date 2020-10-19
 */
public interface HouseService extends IBaseService<House> {

    IPage<House> findListPage(PageParams pageParams);


    /**
     *  根据房源id获取房源的详细信息
     *  房源图片、房东信息等信息会放在附加信息中
     */
    House getDetailById(Long houseId);
}
