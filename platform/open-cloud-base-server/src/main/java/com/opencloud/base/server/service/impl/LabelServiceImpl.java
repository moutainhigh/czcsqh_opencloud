package com.opencloud.base.server.service.impl;

import com.opencloud.base.client.model.entity.Label;
import com.opencloud.base.server.mapper.LabelMapper;
import com.opencloud.base.server.service.LabelService;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 * 房源标签基础表 服务实现类
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LabelServiceImpl extends BaseServiceImpl<LabelMapper, Label> implements LabelService {

}
