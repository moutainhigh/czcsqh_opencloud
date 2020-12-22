package com.opencloud.base.server.service.impl;

import com.opencloud.base.client.model.entity.HouseOwner;
import com.opencloud.base.server.mapper.HouseOwnerMapper;
import com.opencloud.base.server.service.HouseOwnerService;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 * 房东表 服务实现类
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HouseOwnerServiceImpl extends BaseServiceImpl<HouseOwnerMapper, HouseOwner> implements HouseOwnerService {

}
