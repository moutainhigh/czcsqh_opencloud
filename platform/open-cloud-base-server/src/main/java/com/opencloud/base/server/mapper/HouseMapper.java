package com.opencloud.base.server.mapper;

import com.opencloud.base.client.model.entity.House;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;
/**
 * 房源表 Mapper 接口
 * @author yanjiajun
 * @date 2020-10-19
 */
@Mapper
public interface HouseMapper extends SuperMapper<House> {

}
