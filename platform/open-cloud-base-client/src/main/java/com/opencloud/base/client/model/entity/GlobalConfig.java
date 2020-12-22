package com.opencloud.base.client.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.opencloud.common.mybatis.base.entity.AbstractEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 全局配置
 *
 * @author yanjiajun
 * @date 2020-12-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("global_config")
@ApiModel(value="GlobalConfig对象", description="全局配置")
public class GlobalConfig extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @TableId(value = "global_config_id", type = IdType.ID_WORKER)
    private Long globalConfigId;

    @ApiModelProperty(value = "配置名")
    private String globalName;

    @ApiModelProperty(value = "配置值")
    private String globalValue;

}
