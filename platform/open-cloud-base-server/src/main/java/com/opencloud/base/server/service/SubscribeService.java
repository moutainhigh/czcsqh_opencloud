package com.opencloud.base.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.entity.Subscribe;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.mybatis.base.service.IBaseService;

/**
 * 预约表 服务类
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
public interface SubscribeService extends IBaseService<Subscribe> {

    public IPage<Subscribe> list(PageParams pageParams);

    public void add(Long houseId);
}
