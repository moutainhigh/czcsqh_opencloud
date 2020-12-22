package com.opencloud.base.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.opencloud.base.client.model.entity.Favorite;
import com.opencloud.base.client.model.entity.House;
import com.opencloud.base.server.mapper.FavoriteMapper;
import com.opencloud.base.server.service.FavoriteService;
import com.opencloud.base.server.service.HouseService;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.common.security.OpenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 收藏表 服务实现类
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FavoriteServiceImpl extends BaseServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

    @Autowired
    private HouseService houseService;

    @Override
    public IPage<Favorite> list(PageParams pageParams) {
        QueryWrapper<Favorite> favoriteQueryWrapper = new QueryWrapper<>();
        favoriteQueryWrapper.lambda().eq(Favorite::getUserId,OpenHelper.getUserId());
        IPage<Favorite> resultPage = this.page(pageParams,favoriteQueryWrapper);

        if(ObjectUtils.isEmpty(resultPage.getRecords().size())){
            return resultPage;
        }
        //拼接house信息
        List<Favorite> favoriteList = resultPage.getRecords();
        List<Long> houseIdList =favoriteList.stream().map(Favorite::getHouseId).collect(Collectors.toList());
        QueryWrapper<House> houseQueryWrapper = new QueryWrapper<>();
        houseQueryWrapper.lambda().in(House::getHouseId , houseIdList);
        List<House> houseList = houseService.list(houseQueryWrapper);
        for(int i=0;i<houseList.size();i++){
            House tempHouse = houseList.get(i);
            for(int j=0;j<favoriteList.size();j++){
                Favorite tempFavorite = favoriteList.get(j);
                if(tempHouse.getHouseId().equals(tempFavorite.getHouseId())){
                    tempFavorite.setHouse(tempHouse);
                }
            }
        }
        return resultPage;
    }

    @Override
    public List<Favorite> getFavoriteByHouseIdList(List<Long> userIdList) {
        Long userId = OpenHelper.getUserId();
        QueryWrapper<Favorite> favoriteQueryWrapper = new QueryWrapper<>();
        favoriteQueryWrapper.lambda().in(Favorite::getHouseId).eq(Favorite::getUserId,userId);
        return this.list(favoriteQueryWrapper);
    }

    @Override
    public Boolean checkFavorite(Long houseId) {
        QueryWrapper<Favorite> favoriteQueryWrapper = new QueryWrapper<>();
        favoriteQueryWrapper.lambda().eq(Favorite::getHouseId,houseId).eq(Favorite::getUserId,OpenHelper.getUserId());
        Favorite favorite = this.getOne(favoriteQueryWrapper);
        return favorite == null ? false :true ;
    }
}
