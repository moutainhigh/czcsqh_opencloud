package com.opencloud.base.server.service;

import com.opencloud.base.client.model.entity.BaseAccount;
import com.opencloud.base.client.model.entity.BaseAccountLogs;
import com.opencloud.common.mybatis.base.service.IBaseService;

/**
 * 系统用户登录账号管理
 * 支持多账号登陆
 *
 * @author liuyadu
 */
public interface BaseAccountService extends IBaseService<BaseAccount> {


    /**
     * 根据主键获取账号信息
     *
     * @param accountId
     * @return
     */
    BaseAccount getAccountById(Long accountId);


    /**
     * 获取账号信息
     *
     * @param account
     * @param accountType
     * @param domain
     * @return
     */
    BaseAccount getAccount(String account, String accountType, String domain);


    /**
     * 注册账号
     *
     * @param userId
     * @param account
     * @param password
     * @param accountType
     * @param status
     * @param domain
     * @param registerIp
     * @return
     */
    BaseAccount register(Long userId, String account, String password, String accountType, Integer status, String domain, String registerIp);


    /**
     * 检查账号是否存在
     *
     * @param account
     * @param accountType
     * @param domain
     * @return
     */
    Boolean isExist(String account, String accountType, String domain);


    /**
     * 删除账号
     *
     * @param accountId
     * @return
     */
    int removeAccount(Long accountId);

    /**
     * 更新账号状态
     *
     * @param accountId
     * @param status
     */
    int updateStatus(Long accountId, Integer status);

    /**
     * 根据用户更新账户状态
     *
     * @param userId
     * @param domain
     * @param status
     */
    int updateStatusByUserId(Long userId, String domain, Integer status);

    /**
     * 重置用户密码
     *
     * @param userId
     * @param domain
     * @param password
     */
    int updatePasswordByUserId(Long userId, String domain, String password);

    /**
     * 根据用户ID删除账号
     *
     * @param userId
     * @param domain
     * @return
     */
    int removeAccountByUserId(Long userId, String domain);

    /**
     * 添加登录日志
     *
     * @param log
     */
    void addLoginLog(BaseAccountLogs log);

    /**
     * 修改用户手机账号
     *
     * @param
     */
    void updateMobileAccount(Long userId, String mobile);

    /**
     * 新增绑定手机用户
     * @param mobile
     * @param userId
     */
    void addAcount(String mobile, Long userId);

    /**
     * 解绑手机 删除 手机用户
     */
    void removeAccount(String mobile,Long userId);

    /**
     * 修改用户密码
     */
    void changePassword(Long userId,String oldPassword,String newPassword);


    /**
     *
     */
    BaseAccount getMyWeChatOpenId();

    /**
     * 重置密码
     */
    void resetPassword(String mobile, String newPassword);

    /**
     * 校验手机号是否存在
     */
    boolean checkMobileExist(String mobile);
}
