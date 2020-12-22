package com.opencloud.base.client.service;

import com.opencloud.common.model.ResultBody;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface ILoginServiceClient {

    @PostMapping("/login/token")
    public ResultBody<OAuth2AccessToken> getLoginToken(@RequestParam("username") String username, @RequestParam("password") String password) throws Exception;
}
