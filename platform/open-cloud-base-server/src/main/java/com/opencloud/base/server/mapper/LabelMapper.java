package com.opencloud.base.server.mapper;

import com.opencloud.base.client.model.entity.Label;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;
/**
 * 房源标签基础表 Mapper 接口
 * @author yanjiajun
 * @date 2020-10-28
 */
@Mapper
public interface LabelMapper extends SuperMapper<Label> {

}
