package com.opencloud.base.server.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.opencloud.base.client.constants.BaseConstants;
import com.opencloud.base.client.model.entity.BaseAccount;
import com.opencloud.base.client.model.entity.BaseAccountLogs;
import com.opencloud.base.server.mapper.BaseAccountLogsMapper;
import com.opencloud.base.server.mapper.BaseAccountMapper;
import com.opencloud.base.server.service.BaseAccountService;
import com.opencloud.common.exception.OpenAlertException;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.common.security.OpenHelper;
import com.opencloud.common.security.OpenUserDetails;
import com.opencloud.common.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 通用账号
 *
 * @author liuyadu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseAccountServiceImpl extends BaseServiceImpl<BaseAccountMapper, BaseAccount> implements BaseAccountService {

    @Autowired
    private BaseAccountMapper baseAccountMapper;
    @Autowired
    private BaseAccountLogsMapper baseAccountLogsMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * 根据主键获取账号信息
     *
     * @param accountId
     * @return
     */
    @Override
    public BaseAccount getAccountById(Long accountId) {
        return baseAccountMapper.selectById(accountId);
    }

    /**
     * 获取账号信息
     *
     * @param account
     * @param accountType
     * @param domain
     * @return
     */
    @Override
    public BaseAccount getAccount(String account, String accountType, String domain) {
        QueryWrapper<BaseAccount> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseAccount::getAccount, account)
                .eq(BaseAccount::getAccountType, accountType)
                .eq(BaseAccount::getDomain, domain);
        return baseAccountMapper.selectOne(queryWrapper);

    }

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
    @Override
    public BaseAccount register(Long userId, String account, String password, String accountType, Integer status, String domain, String registerIp) {
        if (isExist(account, accountType, domain)) {
            // 账号已被注册
            throw new RuntimeException(String.format("account=[%s],domain=[%s]", account, domain));
        }
        //加密
        String encodePassword = passwordEncoder.encode(password);
        BaseAccount baseAccount = new BaseAccount(userId, account, encodePassword, accountType, domain, registerIp);
        baseAccount.setCreateTime(new Date());
        baseAccount.setUpdateTime(baseAccount.getCreateTime());
        baseAccount.setStatus(status);
        baseAccountMapper.insert(baseAccount);
        return baseAccount;
    }


    /**
     * 检测账号是否存在
     *
     * @param account
     * @param accountType
     * @param domain
     * @return
     */
    @Override
    public Boolean isExist(String account, String accountType, String domain) {
        QueryWrapper<BaseAccount> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseAccount::getAccount, account)
                .eq(BaseAccount::getAccountType, accountType)
                .eq(BaseAccount::getDomain, domain);
        int count = baseAccountMapper.selectCount(queryWrapper);
        return count > 0 ? true : false;
    }

    /**
     * 删除账号
     *
     * @param accountId
     * @return
     */
    @Override
    public int removeAccount(Long accountId) {
        return baseAccountMapper.deleteById(accountId);
    }


    /**
     * 更新账号状态
     *
     * @param accountId
     * @param status
     */
    @Override
    public int updateStatus(Long accountId, Integer status) {
        BaseAccount baseAccount = new BaseAccount();
        baseAccount.setAccountId(accountId);
        baseAccount.setUpdateTime(new Date());
        baseAccount.setStatus(status);
        return baseAccountMapper.updateById(baseAccount);
    }

    /**
     * 根据用户更新账户状态
     *
     * @param userId
     * @param domain
     * @param status
     */
    @Override
    public int updateStatusByUserId(Long userId, String domain, Integer status) {
        if (status == null) {
            return 0;
        }
        BaseAccount baseAccount = new BaseAccount();
        baseAccount.setUpdateTime(new Date());
        baseAccount.setStatus(status);
        QueryWrapper<BaseAccount> wrapper = new QueryWrapper();
        wrapper.lambda()
                .eq(BaseAccount::getDomain, domain)
                .eq(BaseAccount::getUserId, userId);
        return baseAccountMapper.update(baseAccount, wrapper);
    }

    /**
     * 重置用户密码
     *
     * @param userId
     * @param domain
     * @param password
     */
    @Override
    public int updatePasswordByUserId(Long userId, String domain, String password) {
        UpdateWrapper<BaseAccount> updateWrapper = new UpdateWrapper<BaseAccount>();
        updateWrapper.lambda().eq(BaseAccount::getUserId, userId)
                .ne(BaseAccount::getAccountType, "weixin")
                .set(BaseAccount::getPassword, passwordEncoder.encode(password))
                .set(BaseAccount::getUpdateTime, new Date());
        return baseAccountMapper.update(null, updateWrapper);
    }

    /**
     * 根据用户ID删除账号
     *
     * @param userId
     * @param domain
     * @return
     */
    @Override
    public int removeAccountByUserId(Long userId, String domain) {
        QueryWrapper<BaseAccount> wrapper = new QueryWrapper();
        wrapper.lambda()
                .eq(BaseAccount::getUserId, userId)
                .eq(BaseAccount::getDomain, domain);
        return baseAccountMapper.delete(wrapper);
    }


    /**
     * 添加登录日志
     *
     * @param log
     */
    @Override
    public void addLoginLog(BaseAccountLogs log) {
        QueryWrapper<BaseAccountLogs> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseAccountLogs::getAccountId, log.getAccountId())
                .eq(BaseAccountLogs::getUserId, log.getUserId());
        int count = baseAccountLogsMapper.selectCount(queryWrapper);
        log.setLoginTime(new Date());
        log.setLoginNums(count + 1);
        baseAccountLogsMapper.insert(log);
    }

    /**
     * 修改用户手机账号
     *
     * @param
     */
    @Override
    public void updateMobileAccount(Long userId, String mobile) {
        if (userId == null || !ObjectUtils.isNotEmpty(mobile)) {
            return;
        }
        QueryWrapper<BaseAccount> queryWrapper = new QueryWrapper<BaseAccount>();
        queryWrapper.lambda().eq(BaseAccount::getUserId, userId)
                .eq(BaseAccount::getAccountType, "mobile");
        BaseAccount baseAccount = baseAccountMapper.selectOne(queryWrapper);

        baseAccount.setAccount(mobile);
        baseAccountMapper.updateById(baseAccount);
    }

    /**
     * 绑定手机号
     * @param mobile
     * @param userId
     */
    @Override
    public void addAcount(String mobile, Long userId){
        QueryWrapper<BaseAccount> queryWrapper = new QueryWrapper<BaseAccount>();
        queryWrapper.lambda().eq(BaseAccount::getUserId, userId)
                .eq(BaseAccount::getAccountType, "username");
        BaseAccount userNameAccount = baseAccountMapper.selectOne(queryWrapper);
        if(userNameAccount != null){
            BaseAccount mobileAccount = new BaseAccount();
            mobileAccount.setUserId(userId);
            mobileAccount.setAccount(mobile);
            mobileAccount.setPassword(userNameAccount.getPassword());
            mobileAccount.setAccountType("mobile");
            mobileAccount.setDomain(userNameAccount.getDomain());
            mobileAccount.setStatus(1);
            baseAccountMapper.insert(mobileAccount);
        }

    }

    /**
     * 解绑手机号
     * @param mobile
     * @param userId
     */
    @Override
    public void removeAccount(String mobile, Long userId) {
        QueryWrapper<BaseAccount> queryWrapper = new QueryWrapper<BaseAccount>();
        queryWrapper.lambda().eq(BaseAccount::getUserId,userId)
                .eq(BaseAccount::getAccountType,"mobile");
        BaseAccount userMobileType = baseAccountMapper.selectOne(queryWrapper);
        if(userMobileType != null){
            baseAccountMapper.deleteById(userMobileType.getAccountId());
        }

    }

    /**
     * 修改密码
     * @param userId
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {

        QueryWrapper<BaseAccount> queryWrapper = new QueryWrapper<BaseAccount>();
        queryWrapper.lambda().eq(BaseAccount::getUserId, userId)
                .ne(BaseAccount::getAccountType, "weixin");
        List<BaseAccount> baseAccounts = baseAccountMapper.selectList(queryWrapper);
        String oPassword= null;
        for (BaseAccount baseAccount : baseAccounts) {
            oPassword = baseAccount.getPassword();
            if (!StringUtils.isBlank(oPassword)) {
                break;
            }
        }
        boolean matches = passwordEncoder.matches(oldPassword, oPassword);
        if(matches){
            //加密新密码
            String newPass = passwordEncoder.encode(newPassword);
            //修改旧密码
            for(BaseAccount baseAccount : baseAccounts) {
                baseAccount.setPassword(newPass);
                baseAccountMapper.updateById(baseAccount);
            }
        }else {
            throw new OpenAlertException("原密码错误");
        }


    }

    @Override
    public BaseAccount getMyWeChatOpenId() {
        OpenUserDetails userDetails = OpenHelper.getUser();
        QueryWrapper<BaseAccount> accountQueryWrapper = new QueryWrapper<>();
        accountQueryWrapper.lambda().eq(BaseAccount::getUserId,userDetails.getUserId()).eq(BaseAccount::getAccountType,BaseConstants.ACCOUNT_TYPE_WECHAT);
        return this.getOne(accountQueryWrapper);
    }

    @Override
    public void resetPassword(String mobile, String newPassword) {
        QueryWrapper<BaseAccount> accountQueryWrapper = new QueryWrapper<BaseAccount>();
        accountQueryWrapper.lambda().eq(BaseAccount::getAccount, mobile)
                .eq(BaseAccount::getAccountType, "mobile")
                .select(BaseAccount::getUserId);
        BaseAccount account = baseAccountMapper.selectOne(accountQueryWrapper);
        UpdateWrapper<BaseAccount> accountUpdateWrapper = new UpdateWrapper<BaseAccount>();
        accountUpdateWrapper.lambda().eq(BaseAccount::getUserId, account.getUserId())
                .ne(BaseAccount::getAccountType, "weixin")
                .set(BaseAccount::getPassword, passwordEncoder.encode(newPassword));
        baseAccountMapper.update(null, accountUpdateWrapper);
    }

    @Override
    public boolean checkMobileExist(String mobile) {
        QueryWrapper<BaseAccount> accountQueryWrapper = new QueryWrapper<BaseAccount>();
        accountQueryWrapper.lambda().eq(BaseAccount::getAccount, mobile);
        Integer count = baseAccountMapper.selectCount(accountQueryWrapper);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }
}
