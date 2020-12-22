package com.opencloud.base.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.opencloud.base.client.constants.BaseConstants;
import com.opencloud.base.client.model.entity.BaseUser;
import com.opencloud.base.client.model.entity.House;
import com.opencloud.base.client.model.entity.Subscribe;
import com.opencloud.base.server.mapper.SubscribeMapper;
import com.opencloud.base.server.service.BaseUserService;
import com.opencloud.base.server.service.HouseService;
import com.opencloud.base.server.service.SubscribeService;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.common.security.OpenHelper;
import com.opencloud.common.security.OpenUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 预约表 服务实现类
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SubscribeServiceImpl extends BaseServiceImpl<SubscribeMapper, Subscribe> implements SubscribeService {

    @Autowired
    private BaseUserService userService;
    @Autowired
    private HouseService houseService;

    @Override
    public IPage<Subscribe> list(PageParams pageParams) {
        BaseUser user = userService.getUserById(OpenHelper.getUserId());
        QueryWrapper<Subscribe> subscribeQueryWrapper = new QueryWrapper<>();
        if(user.getUserType().equals(BaseConstants.USER_TYPE_CONSUMER)){
            //用户分支
            subscribeQueryWrapper.lambda().eq(Subscribe::getUserId,user.getUserId()).ne(Subscribe::getStatus,-1).orderByDesc(Subscribe::getCreateTime);
        }else if(user.getUserType().equals(BaseConstants.USER_TYPE_HOUSE_OWNER)){
            //房东分支
            subscribeQueryWrapper.lambda().eq(Subscribe::getHouseOwnerId,user.getUserId()).ne(Subscribe::getStatusHouseOwner,-1).orderByDesc(Subscribe::getCreateTime);
        }else if(user.getUserType().equals(BaseConstants.USER_TYPE_SUPPORT)){
            //客服分支
        }
        IPage<Subscribe> resultPage = this.page(pageParams,subscribeQueryWrapper);
        List<Subscribe> subscribeList = resultPage.getRecords();
        if(ObjectUtils.isEmpty(subscribeList)){
            return resultPage;
        }
        //拼接房源信息
        List<Long> houseIdList = subscribeList.stream().map(Subscribe::getHouseId).collect(Collectors.toList());
        QueryWrapper<House> houseQueryWrapper = new QueryWrapper<>();
        houseQueryWrapper.lambda().in(House::getHouseId,houseIdList);
        List<House> houseList = houseService.list(houseQueryWrapper);
        if(ObjectUtils.isEmpty(houseList)){
            return resultPage;
        }
        for(int i=0;i<houseList.size();i++){
            House tempHouse = houseList.get(i);
            for(int j=0;j<subscribeList.size();j++){
                Subscribe tempSubscribe = subscribeList.get(j);
                if(tempHouse.getHouseId().equals(tempSubscribe.getHouseId())){
                    tempSubscribe.setHouse(tempHouse);
                }
            }
        }
        return resultPage;
    }

    @Override
    public void add(Long houseId) {
        House house = houseService.getById(houseId);

        Subscribe subscribe = new Subscribe();
        subscribe.setHouseId(house.getHouseId());
        subscribe.setHouseOwnerId(house.getHouseOwnerId());
        subscribe.setUserId(OpenHelper.getUserId());
        subscribe.setStatus(1);
        subscribe.setStatusHouseOwner(0);

        this.save(subscribe);
    }


}
