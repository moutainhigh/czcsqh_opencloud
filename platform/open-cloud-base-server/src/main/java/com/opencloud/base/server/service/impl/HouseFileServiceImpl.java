package com.opencloud.base.server.service.impl;

import com.opencloud.base.client.model.entity.HouseFile;
import com.opencloud.base.server.mapper.HouseFileMapper;
import com.opencloud.base.server.service.HouseFileService;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 * 房源文件表（图片，视频） 服务实现类
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HouseFileServiceImpl extends BaseServiceImpl<HouseFileMapper, HouseFile> implements HouseFileService {

}
