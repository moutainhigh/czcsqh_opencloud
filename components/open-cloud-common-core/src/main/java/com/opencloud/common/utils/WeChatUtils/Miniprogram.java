package com.opencloud.common.utils.WeChatUtils;

/**
 * 模板消息跳转小程序, 封装参数的类
 * @Author MaoLG
 * @Date 2018/11/13  17:23
 */
public class Miniprogram {

    private String appid;

    private String  pagepath;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPagepath() {
        return pagepath;
    }

    public void setPagepath(String pagepath) {
        this.pagepath = pagepath;
    }
}