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
 * 房源标签关系表
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value="HouseLabel对象", description="房源标签关系表")
public class HouseLabel extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @TableId(value = "house_label_id", type = IdType.ID_WORKER)
    private Long houseLabelId;

    @ApiModelProperty(value = "房源id")
    private Long houseId;

    @ApiModelProperty(value = "标签id")
    private Long labelId;

    @ApiModelProperty(value = "标签文本")
    private String labelText;


}
