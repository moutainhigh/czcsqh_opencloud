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
 * 房源文件表（图片，视频）
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value="HouseFile对象", description="房源文件表（图片，视频）")
public class HouseFile extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @TableId(value = "house_file_id", type = IdType.ID_WORKER)
    private Long houseFileId;

    private Long houseId;

    @ApiModelProperty(value = "文件类型")
    private String type;

    @ApiModelProperty(value = "文件路径")
    private String fileUrl;

    @ApiModelProperty(value = "文件状态")
    private Integer status;


}
