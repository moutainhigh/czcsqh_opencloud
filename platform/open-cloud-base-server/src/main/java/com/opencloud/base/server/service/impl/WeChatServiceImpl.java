package com.opencloud.base.server.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.opencloud.base.client.constants.BaseConstants;
import com.opencloud.base.client.model.entity.BaseAccount;
import com.opencloud.base.client.model.entity.BaseUser;
import com.opencloud.base.server.mapper.BaseAccountMapper;
import com.opencloud.base.server.service.BaseUserService;
import com.opencloud.base.server.service.WeChatService;
import com.opencloud.base.server.service.feign.LoginServiceClient;
import com.opencloud.common.model.ResultBody;
import com.opencloud.common.utils.WeChatUtils.TemplateData;
import com.opencloud.common.utils.WeChatUtils.WechatTemplate;
import com.opencloud.common.utils.WeChatUtils.miniApp.decrypt;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weixin.popular.api.SnsAPI;
import weixin.popular.api.TicketAPI;
import weixin.popular.api.TokenAPI;
import weixin.popular.bean.sns.Jscode2sessionResult;
import weixin.popular.bean.ticket.Ticket;
import weixin.popular.bean.token.Token;

import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class WeChatServiceImpl implements WeChatService {

    private static String WECHAT_ACCOUNT_TYPE = "weixin";
    private static String MINIAPP_ACCOUNT_TYPE = "miniApp";

    @Value("${wxopen_appid}")
    private String wxopenAppid;
    @Value("${wxopen_key}")
    private String wxopenKey;
    @Value("${wx_appid}")
    private String wxappid;
    @Value("${wx_appsecret}")
    private String wxappsecret;
    @Value("${wxpay_mch_id}")
    private String mch_id;
    @Value("${wxpay_key}")
    private String key;
    @Value("${wxopen_appname}")
    private String wxopenAppname;
    @Value("${wxpay_notify_url}")
    private String wxpayNotifyUrl;
    @Value("${wx_mini_appid}")
    private String wxMiniAppid;
    @Value("${wx_mini_appsecret}")
    private String wxMiniAppsecret;

    @Autowired
    BaseAccountMapper accountMapper;

    @Autowired
    private LoginServiceClient loginServiceClient;

    @Autowired
    private BaseUserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> wxminiLogin(String code) {
        Jscode2sessionResult jscode2sessionResult =null;
        jscode2sessionResult = SnsAPI.jscode2session(wxMiniAppid, wxMiniAppsecret, code);

        QueryWrapper<BaseAccount> accountQueryWrapper = new QueryWrapper<BaseAccount>();
        accountQueryWrapper.lambda().eq(BaseAccount::getAccountType, MINIAPP_ACCOUNT_TYPE)
                .eq(BaseAccount::getAccount, jscode2sessionResult.getOpenid());
        BaseAccount account = accountMapper.selectOne(accountQueryWrapper);
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("openid", jscode2sessionResult.getOpenid());
        returnMap.put("session_key", jscode2sessionResult.getSession_key());
        if (account != null) {
            BaseUser user = userService.getById(account.getUserId());
            returnMap.put("userType", user.getUserType());
            try {
                ResultBody<OAuth2AccessToken> resultBody = loginServiceClient.getLoginToken(account.getAccount(), account.getAccount() + "lanpei");
                log.info("===============================" + resultBody.toString());
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

    @Override
    public Map<String, String> getJSSDKConfig(String url) {
        String token = getAccessTokenFromRedis();
        if (token != null) {
            Ticket ticket = TicketAPI.ticketGetticket(token);
            SortedMap<String, String> signParams = new TreeMap<String, String>();
            signParams.put("noncestr", UUID.randomUUID().toString().replace("-", ""));
            signParams.put("jsapi_ticket", ticket.getTicket());
            signParams.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
            signParams.put("url", url);
            signParams.put("signature", sha1Encrypt(sortParams(signParams)));
            signParams.put("appid", wxappid);
            return signParams;
        }
        return new HashMap<>();
    }

    @Override
    public Map<String, String> sendWechatMessage(Long userId , Map param) {
        //先去找一下这个userId有没有openId，没有的话就不要浪费获取token的次数了
        QueryWrapper<BaseAccount> userAccountQueryWrapper = new QueryWrapper<>();
        userAccountQueryWrapper.lambda().eq(BaseAccount::getUserId,userId).eq(BaseAccount::getAccountType, BaseConstants.ACCOUNT_TYPE_WECHAT);
        BaseAccount baseAccount = accountMapper.selectOne(userAccountQueryWrapper);
        if(baseAccount == null){
            return null;
        }

        String templateId = param.get("templateId").toString();
        String token = getAccessTokenFromRedis();
        if (token != null) {
            String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + token;
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url); //建立HttpPost对象
            WechatTemplate wechatTemplate = new WechatTemplate();
            wechatTemplate.setTemplate_id(templateId);
            wechatTemplate.setTouser(baseAccount.getAccount());

            Map<String, TemplateData> m = new HashMap<String, TemplateData>();
            Iterator<Map.Entry<String, String>> entries = param.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                if(!entry.getKey().equals("templateId")){
                    m.put(entry.getKey(), new TemplateData(entry.getValue(), "#173177"));
                }
            }

            wechatTemplate.setData(m);

            String jsonString = JSON.toJSONString(wechatTemplate);

            try {
                httpPost.setEntity(new StringEntity(jsonString, "UTF-8"));
                HttpResponse response = httpclient.execute(httpPost);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return new HashMap<>();
    }


    /**
     * 发送微信模板消息
     *
     * @description 使用该方法前，请去查看msg_wechat_template表中自己想要使用的模板，然后确定自己要传入的参数
     *              ①template_id是必传的
     *              ②比如有{{first.DATA}}，则在param中放入一个为first的key以及对应value。
     * @param userIdList    用户 id列表
     * @param param         参数
     * @return
     */
    @Override
    public Map<String, String> sendWechatMessage(List<Long> userIdList, Map param) {
        userIdList.stream().forEach(item ->{
            sendWechatMessage(item , param);
        });
        return null;
    }

    @Override
    public String wxMiniDecrypt(String iv, String session_key, String encryptedData ,String type) {
        if("consumer".equals(type)){
            return decrypt.decrypt(wxMiniAppid,encryptedData,session_key,iv);
        }else if("doctor".equals(type)){
            //return decrypt.decrypt(wxMiniDoctorAppid,encryptedData,session_key,iv);
        }
        return null;
    }

    /*****************  以下区域是用于存放一些处理数据的方法，跟业务无关的方法      *********************/

    /**
     * 根据参数名称对参数进行字典排序 * @param params * @return
     */
    private String sortParams(SortedMap<String, String> params) {
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String, String>> es = params.entrySet();
        Iterator<Map.Entry<String, String>> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String k = entry.getKey();
            String v = entry.getValue();
            sb.append(k + "=" + v + "&");
        }
        return sb.substring(0, sb.lastIndexOf("&"));
    }

    /**
     * 使用SHA1算法对字符串进行加密 * @param str * @return
     */
    public static String sha1Encrypt(String str) {

        if (str == null || str.length() == 0) {
            return null;
        }

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};

        try {

            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;

            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }

            return new String(buf);

        } catch (Exception e) {
            return null;
        }
    }

    private String getAccessTokenFromRedis(){
        String accessToken = null;
        if(!(redisTemplate.hasKey("wx_access_token"))) {
            Token token = TokenAPI.token(wxappid, wxappsecret);
            if (token.getErrcode() == null) {
                accessToken = token.getAccess_token();
                redisTemplate.opsForValue().set("wx_access_token" , accessToken,100, TimeUnit.MINUTES);
            }
        }else {
            accessToken = (String) redisTemplate.opsForValue().get("wx_access_token");
        }
        return accessToken;
    }

}
