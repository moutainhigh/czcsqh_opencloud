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
 * 家电基础表
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value="HomeAppliancesType对象", description="家电基础表")
public class HomeAppliancesType extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    private Long homeAppliancesTypeId;

    @ApiModelProperty(value = "家电名称")
    private String homeAppliancesName;

    @ApiModelProperty(value = "家电图标路径")
    private String homeAppliancesIcon;


}
