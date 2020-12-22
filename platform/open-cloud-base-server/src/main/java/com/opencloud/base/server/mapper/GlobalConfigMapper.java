package com.opencloud.base.server.mapper;

import com.opencloud.base.client.model.entity.GlobalConfig;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;
/**
 * 全局配置 Mapper 接口
 * @author yanjiajun
 * @date 2020-12-21
 */
@Mapper
public interface GlobalConfigMapper extends SuperMapper<GlobalConfig> {

}
