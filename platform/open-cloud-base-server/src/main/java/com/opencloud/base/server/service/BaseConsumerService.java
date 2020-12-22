package com.opencloud.base.server.service;

import com.opencloud.base.client.model.entity.BaseUser;

import java.util.Map;

public interface BaseConsumerService {


    BaseUser addConsumer(BaseUser user);


    
    /**
     * 绑定用户账号和openid的关系
     * 
     * @param userId
     * @return
     */
    void bindingUserAndWxOpenid(Long userId, String openid);

    /**
     * 绑定用户账号和微信小程序openid的关系
     *
     * @param userId
     * @return
     */
    void bindingUserAndWxMiniOpenid(Long userId, String openid);

    
    /**
     * 通过openid获取是否绑定了用户
     * 
     * @param userId
     * @return
     */
    Map<String, Object> getAccountByOpenid(String openid);
}
