package com.opencloud.base.server.service.impl;

import com.opencloud.base.client.model.entity.HouseHomeAppliances;
import com.opencloud.base.server.mapper.HouseHomeAppliancesMapper;
import com.opencloud.base.server.service.HouseHomeAppliancesService;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HouseHomeAppliancesServiceImpl extends BaseServiceImpl<HouseHomeAppliancesMapper, HouseHomeAppliances> implements HouseHomeAppliancesService {

}
