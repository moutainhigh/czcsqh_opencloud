package com.opencloud.base.server.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.model.UserAccount;
import com.opencloud.base.client.model.entity.BaseUser;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.mybatis.base.service.IBaseService;

/**
 * 系统用户资料管理
 *
 * @author: liuyadu
 * @date: 2018/10/24 16:38
 * @description:
 */
public interface BaseUserService extends IBaseService<BaseUser> {

    /**
     * 添加用户信息
     * @param baseUser
     * @return
     */
    void addUser(BaseUser baseUser);

    /**
     * 更新系统用户
     *
     * @param baseUser
     * @return
     */
    void updateUser(BaseUser baseUser);


    /**
     * 更新密码
     *
     * @param userId
     * @param password
     */
    void updatePassword(Long userId, String password);

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<BaseUser> findListPage(PageParams pageParams);

    /**
     * 查询列表
     *
     * @return
     */
    List<BaseUser> findAllList();


    /**
     * 根据用户ID获取用户信息
     *
     * @param userId
     * @return
     */
    BaseUser getUserById(Long userId);

    /**
     * 获取用户权限
     *
     * @param userId
     * @return
     */
    UserAccount getUserAccount(Long userId);

    /**
     * 依据登录名查询系统用户信息
     *
     * @param username
     * @return
     */
    BaseUser getUserByUsername(String username);


    /**
     * 支持密码、手机号、email登陆
     * 其他方式没有规则，无法自动识别。需要单独开发
     *
     * @param account 登陆账号
     * @return
     */
    UserAccount login(String account);

    /**
     * 查询手机号是否存在
     */
    Integer checkMobile(String mobile);



    /**
     * 获取人员的上级id
     *
     * @param
     * @return
     */
    BaseUser getRecommendByUserId(Long userId);

    /**
     * 取所有符合条件的userId
     */
    List<Long> getUserIdListByCondition(String conditionMapString);

    /**
     * 根据传入的userIdList获取对应的userList
     */
    List<BaseUser> getUserListByUserIdList(String userIdListString);

    /**
     * 取所有符合条件的userId
     */
    List<Long> getUserIdListByOrCondition(String conditionMapString);


}
