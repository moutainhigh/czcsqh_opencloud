package com.opencloud.base.client.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.opencloud.common.mybatis.base.entity.AbstractEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 金刚区
 *
 * @author yanjiajun
 * @date 2020-12-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("king_kong")
@ApiModel(value="KingKong对象", description="金刚区")
public class KingKong extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @TableId(value = "king_kong_id", type = IdType.ID_WORKER)
    private Long kingKongId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "颜色")
    private String bgColor;

    @ApiModelProperty(value = "路径")
    private String imgSrc;

    @ApiModelProperty(value = "路径")
    private Integer sort;
}
