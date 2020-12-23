package com.opencloud.base.client.model.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
 * 房源表
 *
 * @author yanjiajun
 * @date 2020-10-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(value="House对象", description="房源表")
public class House extends AbstractEntity {

    private static final long serialVersionUID=1L;

    @TableId(value = "house_id", type = IdType.ID_WORKER)
    private Long houseId;

    @ApiModelProperty(value = "房东id")
    private Long houseOwnerId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "户型")
    private String layout;

    @ApiModelProperty(value = "租赁方式，整租、合租")
    private String rentType;

    @ApiModelProperty(value = "租赁周期，月")
    private String rentCycle;

    @ApiModelProperty(value = "租金")
    private BigDecimal rental;

    @ApiModelProperty(value = "水费")
    private BigDecimal waterRate;

    @ApiModelProperty(value = "电费")
    private BigDecimal powerRate;

    @ApiModelProperty(value = "方位，朝向")
    private String position;

    @ApiModelProperty(value = "楼层")
    private Integer floor;

    @ApiModelProperty(value = "面积")
    private BigDecimal area;

    @ApiModelProperty(value = "路边距离")
    private String roadside;

    @ApiModelProperty(value = "房源详情，富文本")
    private String detail;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "房源所在省份")
    private String province;

    @ApiModelProperty(value = "房源所在城市")
    private String city;

    @ApiModelProperty(value = "房源所在区域")
    private String district;

    @ApiModelProperty(value = "房源具体地址")
    private String addressDetail;

    @ApiModelProperty(value = "房源状态")
    private Integer status;

    //封面地址
    @TableField(exist = false)
    private String coverImgUrl;

    //标签
    @TableField(exist = false)
    private List<String> label;

    //附带的一些信息
    @TableField(exist = false)
    private List<Map> attachInfo;

    //是否收藏了
    @TableField(exist = false)
    private String isFavorite;
}
