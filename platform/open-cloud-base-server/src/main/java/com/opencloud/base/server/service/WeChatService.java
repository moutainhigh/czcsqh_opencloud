package com.opencloud.base.server.service;


import weixin.popular.bean.paymch.MchBaseResult;
import weixin.popular.bean.paymch.MchOrderInfoResult;
import weixin.popular.bean.paymch.UnifiedorderResult;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface WeChatService {



    Map<String, Object> wxminiLogin(String code);

    Map<String,String> getJSSDKConfig(String url);

    Map<String, String> sendWechatMessage(Long userId , Map param);

    Map<String,String> sendWechatMessage(List<Long> userIdList,Map param);

    /**
     * 小程序解密密文
     */
    String wxMiniDecrypt(String iv,String session_key,String encryptedData ,String type);
}
