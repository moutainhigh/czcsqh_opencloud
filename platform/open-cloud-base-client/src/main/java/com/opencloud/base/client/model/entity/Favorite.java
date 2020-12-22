package com.opencloud.base.client.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.opencloud.common.mybatis.base.entity.AbstractEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 收藏表
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value="Favorite对象", description="收藏表")
public class Favorite extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @TableId(value = "favorite_id", type = IdType.ID_WORKER)
    private Long favoriteId;

    @ApiModelProperty(value = "房源id")
    private Long houseId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @TableField(exist = false)
    private House house;
}
