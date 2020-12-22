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
 * 预约表
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value="Subscribe对象", description="预约表")
public class Subscribe extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @TableId(value = "subscribe_id", type = IdType.ID_WORKER)
    private Long subscribeId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "房源id")
    private Long houseId;

    @ApiModelProperty(value = "房东id")
    private Long houseOwnerId;

    @ApiModelProperty(value = "预约状态,0-用户已取消，1-正常，-1 -用户不可视")
    private Integer status;

    @ApiModelProperty(value = "受约状态，0-待处理，1-已接受，2-已拒绝。-1房东不可视")
    private Integer statusHouseOwner;

    @TableField(exist = false)
    private House house;
}
