package com.opencloud.base.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.opencloud.base.client.model.entity.*;
import com.opencloud.base.server.mapper.HouseMapper;
import com.opencloud.base.server.service.*;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.common.security.OpenHelper;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 房源表 服务实现类
 *
 * @author yanjiajun
 * @date 2020-10-19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HouseServiceImpl extends BaseServiceImpl<HouseMapper, House> implements HouseService {

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private HouseHomeAppliancesService houseHomeAppliancesService;

    @Autowired
    private HouseOwnerService houseOwnerService;

    @Autowired
    private HouseFileService houseFileService;

    @Autowired
    private HouseLabelService houseLabelService;

    @Autowired
    private FavoriteService favoriteService;

    @Override
    public IPage<House> findListPage(PageParams pageParams) {
        House query = pageParams.mapToObject(House.class);
        Object minRental = pageParams.getRequestMap().get("minRental");
        Object maxRental = pageParams.getRequestMap().get("maxRental");
        BigDecimal min = ObjectUtils.isEmpty(minRental) ? BigDecimal.ZERO : new BigDecimal(minRental.toString());
        BigDecimal max = ObjectUtils.isEmpty(maxRental) ? new BigDecimal(9999999) : new BigDecimal(maxRental.toString());
        //对租赁类型、户型、租金做筛选
        QueryWrapper<House> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(query.getRentType()),House::getRentType,query.getRentType())
                            .eq(ObjectUtils.isNotEmpty(query.getLayout()),House::getLayout,query.getLayout())
                            .like(ObjectUtils.isNotEmpty(query.getDistrict()),House::getDistrict,query.getDistrict())
                            .eq(ObjectUtils.isNotEmpty(query.getLayout()),House::getLayout,query.getLayout())
                            .ge(ObjectUtils.isNotEmpty(minRental),House::getRental,min)
                            .le(ObjectUtils.isNotEmpty(maxRental),House::getRental,max);
        IPage resultPage = houseMapper.selectPage(pageParams,queryWrapper);
        List<House> houseList = resultPage.getRecords();
        if(ObjectUtils.isNotEmpty(houseList)){
            //拼接信息
            //封面图
            List<Long> houseIdList = houseList.stream().map(House::getHouseId).collect(Collectors.toList());
            QueryWrapper<HouseFile> houseFileQueryWrapper = new QueryWrapper<>();
            houseFileQueryWrapper.lambda().eq(HouseFile::getType,1).in(HouseFile::getHouseId,houseIdList);
            List<HouseFile> houseFileList = houseFileService.list(houseFileQueryWrapper);
            for(int i=0;i<houseFileList.size();i++){
                HouseFile tempHouseFile = houseFileList.get(i);
                for(int j=0;j<houseList.size();j++){
                    House tempHouse = houseList.get(j);
                    if(tempHouseFile.getHouseId().equals(tempHouse.getHouseId())){
                        tempHouse.setCoverImgUrl(tempHouseFile.getFileUrl());
                    }
                }
            }
            //房源标签
            QueryWrapper<HouseLabel> houseLabelQueryWrapper = new QueryWrapper<>();
            houseLabelQueryWrapper.lambda().in(HouseLabel::getHouseId,houseIdList);
            List<HouseLabel> houseLabelList = houseLabelService.list(houseLabelQueryWrapper);

            for(int i=0;i<houseList.size();i++){
                House tempHouse = houseList.get(i);
                List<String> labelList = new ArrayList<>();
                for(int j=0;j<houseLabelList.size();j++){
                    HouseLabel tempHouseLabel = houseLabelList.get(j);
                    if(tempHouseLabel.getHouseId().equals(tempHouse.getHouseId())){
                        labelList.add(tempHouseLabel.getLabelText());
                    }
                }
                tempHouse.setLabel(labelList);
            }
            //是否已收藏
            if(OpenHelper.getUserId() != null){
                QueryWrapper<Favorite> favoriteQueryWrapper = new QueryWrapper<>();
                favoriteQueryWrapper.lambda().eq(Favorite::getUserId,OpenHelper.getUserId()).in(Favorite::getHouseId,houseIdList);
                List<Favorite> favoriteList = favoriteService.list(favoriteQueryWrapper);
                for(int i=0;i<favoriteList.size();i++){
                    Favorite tempFavorite =favoriteList.get(i);
                    for(int j=0;j<houseList.size();j++){
                        House tempHouse = houseList.get(j);
                        if(tempFavorite.getHouseId().equals(tempHouse.getHouseId())){
                            tempHouse.setIsFavorite("true");
                        }
                    }
                }
            }
        }
        return resultPage;
    }

    @Override
    public House getDetailById(Long houseId) {
        House house = this.getById(houseId);
        //然后把房源的配置查出来
        QueryWrapper<HouseHomeAppliances> houseHomeAppliancesQueryWrapper = new QueryWrapper<>();
        houseHomeAppliancesQueryWrapper.lambda().eq(HouseHomeAppliances::getHouseId,house.getHouseId());
        List<HouseHomeAppliances> houseHomeAppliances = houseHomeAppliancesService.list(houseHomeAppliancesQueryWrapper);
        //然后把房东信息查出来
        QueryWrapper<HouseOwner> houseOwnerQueryWrapper = new QueryWrapper<>();
        houseOwnerQueryWrapper.lambda().eq(HouseOwner::getHouseOwnerId,house.getHouseOwnerId());
        HouseOwner houseOwner = houseOwnerService.getOne(houseOwnerQueryWrapper);
        //然后把房源的图片和视频查出来
        QueryWrapper<HouseFile> houseFileQueryWrapper = new QueryWrapper<>();
        houseFileQueryWrapper.lambda().eq(HouseFile::getHouseId,house.getHouseId());
        List<HouseFile> houseFiles = houseFileService.list(houseFileQueryWrapper);
        //然后把房源的标签集查出来
        QueryWrapper<HouseLabel> houseLabelQueryWrapper = new QueryWrapper<>();
        houseLabelQueryWrapper.lambda().eq(HouseLabel::getHouseId,house.getHouseId());
        List<HouseLabel> houseLabels = houseLabelService.list(houseLabelQueryWrapper);

        return null;
    }
}
