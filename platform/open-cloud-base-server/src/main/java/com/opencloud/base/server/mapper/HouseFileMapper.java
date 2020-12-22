package com.opencloud.base.server.mapper;

import com.opencloud.base.client.model.entity.HouseFile;
import com.opencloud.common.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;
/**
 * 房源文件表（图片，视频） Mapper 接口
 * @author yanjiajun
 * @date 2020-10-28
 */
@Mapper
public interface HouseFileMapper extends SuperMapper<HouseFile> {

}
