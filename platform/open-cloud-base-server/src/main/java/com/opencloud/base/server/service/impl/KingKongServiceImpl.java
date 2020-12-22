package com.opencloud.base.server.service.impl;

import com.opencloud.base.client.model.entity.KingKong;
import com.opencloud.base.server.mapper.KingKongMapper;
import com.opencloud.base.server.service.KingKongService;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 * 金刚区 服务实现类
 *
 * @author yanjiajun
 * @date 2020-12-22
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class KingKongServiceImpl extends BaseServiceImpl<KingKongMapper, KingKong> implements KingKongService {

}
