package com.opencloud.base.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.entity.Favorite;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.mybatis.base.service.IBaseService;

import java.util.List;

/**
 * 收藏表 服务类
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
public interface FavoriteService extends IBaseService<Favorite> {

    /**
     * 根据用户id，获取收藏的房源的列表
     * @param pageParams
     * @return
     */
    public IPage<Favorite> list(PageParams pageParams);

    public List<Favorite> getFavoriteByHouseIdList(List<Long> userIdList);

    /**
     * 验证用户是否有收藏该房源
     * @return
     */
    public Boolean checkFavorite(Long houseId);

}
