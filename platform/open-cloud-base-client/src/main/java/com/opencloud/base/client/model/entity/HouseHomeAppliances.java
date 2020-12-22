package com.opencloud.base.client.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.opencloud.common.mybatis.base.entity.AbstractEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value="HouseHomeAppliances对象", description="")
public class HouseHomeAppliances extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @TableId(value = "house_home_appliances_id", type = IdType.ID_WORKER)
    private Long houseHomeAppliancesId;

    @ApiModelProperty(value = "房源id")
    private Long houseId;

    @ApiModelProperty(value = "家电id")
    private Long homeAppliancesTypeId;


}
