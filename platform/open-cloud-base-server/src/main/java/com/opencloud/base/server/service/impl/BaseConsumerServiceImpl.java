package com.opencloud.base.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.opencloud.base.client.constants.BaseConstants;
import com.opencloud.base.client.model.entity.BaseAccount;
import com.opencloud.base.client.model.entity.BaseUser;
import com.opencloud.base.server.mapper.BaseUserMapper;
import com.opencloud.base.server.service.*;
import com.opencloud.base.server.service.feign.LoginServiceClient;
import com.opencloud.common.model.ResultBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class BaseConsumerServiceImpl implements BaseConsumerService {

    @Autowired
    private BaseUserService userService;
    @Autowired
    private BaseUserMapper userMapper;
    
    @Autowired
    private BaseAccountService accountService;
    
    @Autowired
    private BaseRoleService roleService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private LoginServiceClient loginServiceClient;
    
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private BaseDistributionRelationService distributionRelationService ;




    @Override
    public BaseUser addConsumer(BaseUser user) {
        QueryWrapper<BaseUser> userQueryWrapper = new QueryWrapper<BaseUser>();
        userQueryWrapper.lambda().eq(BaseUser::getUserName, user.getUserName());
        userQueryWrapper.select("MAX(user_name) AS userName");
        BaseUser lastUser = userMapper.selectOne(userQueryWrapper);
        if (lastUser != null) {
            user.setUserName(user.getUserName() + String.format("%0" + 4 + "d", Integer.valueOf(lastUser.getUserName().split(user.getUserName())[1]) + 1));
        } else {
            user.setUserName(user.getUserName() + "0001");
        }
        user.setNickName(user.getUserName());
        user.setStatus(1);
        user.setUserType(BaseConstants.USER_TYPE_CONSUMER);
        userService.addUser(user);
        // 给予权限
        roleService.saveUserRoles(user.getUserId(), BaseConstants.USER_TYPE_CONSUMER.replace(",",""));

        return user;
    }


    @Override
    public void bindingUserAndWxOpenid(Long userId, String openid) {
        BaseAccount account = new BaseAccount();
        account.setUserId(userId);
        account.setAccount(openid);
        log.info(openid + userId);
        account.setPassword(passwordEncoder.encode(openid + "lanpei"));
        account.setAccountType("weixin");
        account.setDomain("@admin.com");
        account.setStatus(1);
        accountService.save(account);
    }

    @Override
    public void bindingUserAndWxMiniOpenid(Long userId, String openid) {
        BaseAccount account = new BaseAccount();
        account.setUserId(userId);
        account.setAccount(openid);
        log.info(openid + userId);
        account.setPassword(passwordEncoder.encode(openid + "lanpei"));
        account.setAccountType(BaseConstants.ACCOUNT_TYPE_MINIAPP);
        account.setDomain("@admin.com");
        account.setStatus(1);
        accountService.save(account);
    }

    @Override
    public Map<String, Object> getAccountByOpenid(String openid) {
        QueryWrapper<BaseAccount> accountQueryWrapper = new QueryWrapper<BaseAccount>();
        accountQueryWrapper.lambda().eq(BaseAccount::getAccountType, BaseConstants.ACCOUNT_TYPE_WECHAT)
                                    .eq(BaseAccount::getAccount, openid);
        BaseAccount account = accountService.getOne(accountQueryWrapper);
        Map<String, Object> returnMap = new HashMap<String, Object>();
        if (account != null) {
            try {
                ResultBody<OAuth2AccessToken> resultBody = loginServiceClient.getLoginToken(account.getAccount(), account.getAccount() + "lanpei");
                returnMap.put("token", resultBody.getData().getValue());
                return returnMap;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            returnMap.put("token", "");
            return returnMap;
        }
        return returnMap;
    }

}
