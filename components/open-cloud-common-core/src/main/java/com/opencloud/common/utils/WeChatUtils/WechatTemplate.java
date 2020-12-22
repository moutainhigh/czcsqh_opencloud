package com.opencloud.common.utils.WeChatUtils;

import java.util.Map;

/**
 * 包含Miniprogram, TemplateData
 * 参数的类, 调用微信发送模板消息的参数类
 * @Author MaoLG
 * @Date 2018/11/13  16:33
 */
public class WechatTemplate {
    private String touser;

    private Miniprogram miniprogram;

    private String template_id;

    private String url;

    private Map<String, TemplateData> data;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, TemplateData> getData() {
        return data;
    }

    public Miniprogram getMiniprogram() {
        return miniprogram;
    }

    public void setMiniprogram(Miniprogram miniprogram) {
        this.miniprogram = miniprogram;
    }

    public void setData(Map<String, TemplateData> data) {
        this.data = data;
    }
}