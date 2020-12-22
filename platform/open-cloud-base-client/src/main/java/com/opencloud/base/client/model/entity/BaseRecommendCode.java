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

/**
 * 
 *
 * @author jiajun.yan
 * @date 2019-11-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value="BaseRecommendCode对象", description="")
public class BaseRecommendCode extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "短随机码")
    @TableId(value = "short_code", type = IdType.ID_WORKER)
    private String shortCode;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @TableField(exist = false)
    private String registerUrl;
}
