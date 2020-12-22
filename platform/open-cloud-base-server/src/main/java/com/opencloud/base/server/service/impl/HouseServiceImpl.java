package com.opencloud.base.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.opencloud.base.client.model.entity.*;
import com.opencloud.base.server.mapper.HouseMapper;
import com.opencloud.base.server.service.*;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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

    @Override
    public IPage<House> findListPage(PageParams pageParams) {
        House query = pageParams.mapToObject(House.class);
        Object minRental = pageParams.getRequestMap().get("minRental");
        Object maxRental = pageParams.getRequestMap().get("maxRental");
        BigDecimal min = minRental == null ? BigDecimal.ZERO : new BigDecimal(minRental.toString());
        BigDecimal max = maxRental == null ? new BigDecimal(99999) : new BigDecimal(maxRental.toString());
        //对租赁类型、户型、租金做筛选
        QueryWrapper<House> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(query.getRentType()),House::getRentType,query.getRentType())
                            .eq(ObjectUtils.isNotEmpty(query.getLayout()),House::getLayout,query.getLayout())
                            .ge(ObjectUtils.isNotEmpty(minRental),House::getRental,min)
                            .le(ObjectUtils.isNotEmpty(maxRental),House::getRental,max);
        return houseMapper.selectPage(pageParams,queryWrapper);
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
