package com.opencloud.base.client.model.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.opencloud.common.annotation.TableAlias;
import com.opencloud.common.mybatis.base.entity.AbstractEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 系统用户-基础信息
 *
 * @author liuyadu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableAlias("user")
@TableName("base_user")
public class BaseUser extends AbstractEntity {
    private static final long serialVersionUID = -735161640894047414L;

    @ApiModelProperty(value = "用户ID")
    @TableId(value = "user_id", type = IdType.ID_WORKER)
    private Long userId;

    @ApiModelProperty(value = "登陆账号")
    private String userName;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "性别：0-保密，1-男性，2-女性")
    private Integer sex;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "用户类型:super-超级管理员 normal-普通管理员")
    private String userType;

    @ApiModelProperty(value = "描述")
    private String userDesc;

    @ApiModelProperty(value = "推荐人id")
    private Long recommendId;

    @ApiModelProperty(value = "状态:0-禁用 1-正常 2-待审核")
    private Integer status;

    /**
     * 密码
     */
    @JsonIgnore
    @TableField(exist = false)
    private String password;

    @ApiModelProperty(value = "推荐者名称")
    @TableField(exist = false)
    private String recommendName;

    @TableField(exist = false)
    private List<Map<String, Object>> messages;

}
