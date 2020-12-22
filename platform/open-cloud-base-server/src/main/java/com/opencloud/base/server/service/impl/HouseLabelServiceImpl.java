package com.opencloud.base.server.service.impl;

import com.opencloud.base.client.model.entity.HouseLabel;
import com.opencloud.base.server.mapper.HouseLabelMapper;
import com.opencloud.base.server.service.HouseLabelService;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 * 房源标签关系表 服务实现类
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HouseLabelServiceImpl extends BaseServiceImpl<HouseLabelMapper, HouseLabel> implements HouseLabelService {

}
