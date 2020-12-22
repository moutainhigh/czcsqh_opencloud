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
 * 房东表
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value="HouseOwner对象", description="房东表")
public class HouseOwner extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "房东id")
    @TableId(value = "house_owner_id", type = IdType.ID_WORKER)
    private Long houseOwnerId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "房东状态，0-未审核通过，1-审核通过")
    private Integer status;


}
