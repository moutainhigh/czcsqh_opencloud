package com.opencloud.base.server.service;

import com.opencloud.base.client.model.entity.BaseDistributionRelation;
import com.opencloud.common.mybatis.base.service.IBaseService;

import java.util.List;

/**
 * 分销关系闭包表 服务类
 *
 * @author jiajun.yan
 * @date 2019-11-13
 */
public interface BaseDistributionRelationService extends IBaseService<BaseDistributionRelation> {
    
    void addNewDescendant( Long userId ,Long ancestorId ,Integer type);
    
    List<BaseDistributionRelation> getAllDistributionRelation(Long userId ,Integer type);

    void transfer(Long patientUserId, Long doctorUserId ,Integer type);

    void resetDistributionRelationTable(Long userId);

}
