package com.opencloud.base.client.service;

import com.opencloud.base.client.model.UserAccount;
import com.opencloud.common.model.ResultBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author liuyadu
 */
public interface IBaseDeveloperServiceClient {

    /**
     * 开发者登录
     *
     * @param username
     * @return
     */
    @PostMapping("/developer/login")
    ResultBody<UserAccount> developerLogin(@RequestParam(value = "username") String username);


}
