package com.opencloud.base.server.service.impl;

import com.opencloud.base.client.model.entity.HomeAppliancesType;
import com.opencloud.base.server.mapper.HomeAppliancesTypeMapper;
import com.opencloud.base.server.service.HomeAppliancesTypeService;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 * 家电基础表 服务实现类
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HomeAppliancesTypeServiceImpl extends BaseServiceImpl<HomeAppliancesTypeMapper, HomeAppliancesType> implements HomeAppliancesTypeService {

}
