package com.opencloud.base.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.opencloud.base.client.model.entity.BaseDistributionRelation;
import com.opencloud.base.client.model.entity.BaseUser;
import com.opencloud.base.server.mapper.BaseDistributionRelationMapper;
import com.opencloud.base.server.service.BaseDistributionRelationService;
import com.opencloud.base.server.service.BaseUserService;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分销关系闭包表 服务实现类
 *
 * @author jiajun.yan
 * @date 2019-11-13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseDistributionRelationServiceImpl extends BaseServiceImpl<BaseDistributionRelationMapper, BaseDistributionRelation> implements BaseDistributionRelationService {

    @Autowired
    private BaseDistributionRelationMapper baseDistributionRelationMapper;
    @Autowired
    private BaseUserService userService;

    @Override
    public void addNewDescendant(Long userId, Long ancestorId, Integer type) {
        //先查出新节点的父亲节点的所有记录
        BaseUser user = userService.getUserById(userId);
        QueryWrapper<BaseDistributionRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(BaseDistributionRelation::getDescendantId, ancestorId).eq(BaseDistributionRelation::getRelationType, 1);
        List<BaseDistributionRelation> ancestorList = baseDistributionRelationMapper.selectList(queryWrapper);
        List<BaseDistributionRelation> newNodeList = new ArrayList<>();
        for (int i = 0; i < ancestorList.size(); i++) {
            BaseDistributionRelation distributionRelation = ancestorList.get(i);
            BaseDistributionRelation newNode = new BaseDistributionRelation();
            newNode.setAncestorId(distributionRelation.getAncestorId());
            newNode.setDistance(distributionRelation.getDistance() + 1);
            newNode.setDescendantId(userId);
            newNode.setRelationType(type);
            newNode.setUserType(user.getUserType());
            newNodeList.add(newNode);
        }
        //然后再生成一个自己到自己的节点
        BaseDistributionRelation newNode = new BaseDistributionRelation();
        newNode.setAncestorId(userId);
        newNode.setDescendantId(userId);
        newNode.setDistance(0);
        newNode.setRelationType(type);
        newNode.setUserType(user.getUserType());
        newNodeList.add(newNode);
        //批量保存
        saveOrUpdateBatch(newNodeList);
    }

    @Override
    public List<BaseDistributionRelation> getAllDistributionRelation(Long userId, Integer type) {
        QueryWrapper<BaseDistributionRelation> queryWrapper = new QueryWrapper<BaseDistributionRelation>();
        queryWrapper.lambda().eq(BaseDistributionRelation::getDescendantId, userId)//.eq(BaseDistributionRelation::getRelationType,type)
                .orderByDesc(BaseDistributionRelation::getDistance);
        List<BaseDistributionRelation> distributionRelationList = baseDistributionRelationMapper.selectList(queryWrapper);
        return distributionRelationList;
    }

    /**
     * 病人换医生
     *
     * @param userId       被转移的人userId
     * @param targetUserId 目标的userId
     */

    @Override
    public void transfer(Long userId, Long targetUserId, Integer type) {
        //先把被转移的人的闭包关系记录找出来，全部删掉
        List<BaseDistributionRelation> patientRelationList = getAllDistributionRelation(userId, type);
        List<Long> ids = patientRelationList.stream().map(BaseDistributionRelation::getDistributionRelationId).collect(Collectors.toList());
        if (ids.size() > 0) {
            this.removeByIds(ids);
        }
        //再把被转移的人添加到目标下
        addNewDescendant(userId, targetUserId, type);
    }

    /**
     * 本方法用于重置整个关系闭包表
     *
     * @param userId
     */
    @Override
    public void resetDistributionRelationTable(Long userId) {
        //从上而下的生成，递归生成
        //从客服10000开始
        QueryWrapper<BaseUser> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().eq(BaseUser::getUserId, userId);
        //拿到这个人
        BaseUser user = userService.getOne(userQueryWrapper);
        //如果是10000，把自身添加进去即可
        if (user.getUserId().equals(10000L)) {
            addNewDescendant(user.getUserId(), user.getUserId(), 1);
        }

        Long ancestorId = user.getRecommendId();
        transfer(user.getUserId(), ancestorId, 1);
        //然后找到自己的下级，递归执行
        userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().eq(BaseUser::getRecommendId, user.getUserId());
        List<BaseUser> userList = userService.list(userQueryWrapper);
        for (int i = 0; i < userList.size(); i++) {
            BaseUser tempUser = userList.get(i);
            resetDistributionRelationTable(tempUser.getUserId());
        }
    }


}
