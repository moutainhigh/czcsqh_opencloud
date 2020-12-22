package com.opencloud.base.server.service.impl;

import com.opencloud.base.client.model.entity.GlobalConfig;
import com.opencloud.base.server.mapper.GlobalConfigMapper;
import com.opencloud.base.server.service.GlobalConfigService;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 * 全局配置 服务实现类
 *
 * @author yanjiajun
 * @date 2020-12-21
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GlobalConfigServiceImpl extends BaseServiceImpl<GlobalConfigMapper, GlobalConfig> implements GlobalConfigService {

}
