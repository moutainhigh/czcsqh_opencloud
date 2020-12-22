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
 * 分销关系闭包表
 *
 * @author jiajun.yan
 * @date 2019-11-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value="BaseDistributionRelation对象", description="分销关系闭包表")
public class BaseDistributionRelation extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "分销关系表主键id")
    @TableId(value = "distribution_relation_id", type = IdType.ID_WORKER)
    private Long distributionRelationId;

    @ApiModelProperty(value = "祖先id")
    private Long ancestorId;

    @ApiModelProperty(value = "后代id")
    private Long descendantId;

    @ApiModelProperty(value = "祖先与后代的距离")
    private Integer distance;

    @ApiModelProperty(value = "关系类型，1 - 不含用户的关系链，2 - 包含用户的关系链")
    private Integer relationType;

    @ApiModelProperty(value = "用户代码")
    private String userType;

    //角色id
    @TableField(exist = false)
    private Long roleId;

    //流通顺序号
    @TableField(exist = false)
    private Long circulationSerialNum;

    //用户手机号
    @TableField(exist = false)
    private String mobile;

    @TableField(exist = false)
    private Integer count;
}
