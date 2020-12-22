package com.opencloud.base.client.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.google.common.collect.Lists;
import com.opencloud.base.client.model.entity.BaseAccount;
import com.opencloud.common.security.OpenAuthority;

/**
 * @author: liuyadu
 * @date: 2018/11/12 11:35
 * @description:
 */
public class UserAccount extends BaseAccount implements Serializable {
    private static final long serialVersionUID = 6717800085953996702L;

    private Collection<Map> roles = Lists.newArrayList();
    /**
     * 用户权限
     */
    private Collection<OpenAuthority> authorities = Lists.newArrayList();
    /**
     * 第三方账号
     */
    private String thirdParty;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像id
     */
    private Long avatarId;

    /**
     * 头像
     */
    private String avatar;

    //******
    /**
     * 手机
     */
    private String mobile;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 用户描述
     */
    private String userDesc;



    /**
     * 用户账号
     */
    private String userName;

    /**
     用户出生日期
     */
    private Date birthTime;

    /**
     *
     */


    /**
     * 用户类型
     * @return
     */
    private String userType;

    /**
     * weCaht openId
     */
    private String weChatOpenId;

    /**
     * 审核信息
     */
    private String auditContent;


    public Collection<OpenAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<OpenAuthority> authorities) {
        this.authorities = authorities;
    }

    public String getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(String thirdParty) {
        this.thirdParty = thirdParty;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Long avatarId) {
        this.avatarId = avatarId;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Collection<Map> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Map> roles) {
        this.roles = roles;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getBirthTime() {
        return birthTime;
    }

    public void setBirthTime(Date birthTime) {
        this.birthTime = birthTime;
    }


    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getWeChatOpenId() {
        return weChatOpenId;
    }

    public void setWeChatOpenId(String weChatOpenId) {
        this.weChatOpenId = weChatOpenId;
    }

    public String getAuditContent() {
        return auditContent;
    }

    public void setAuditContent(String auditContent) {
        this.auditContent = auditContent;
    }
}
