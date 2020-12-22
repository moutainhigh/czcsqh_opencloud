package com.opencloud.common.utils.WeChatUtils;

/**
 * 模板消息的内容,
 * 封装模板消息具体内容的类
 * @Author MaoLG
 * @Date 2018/11/13  16:32
 */
public class TemplateData {

    private String value;
    private String color = "#173177";

    public TemplateData(String value){
        this.value = value;
    }

    public TemplateData(String value,String color){
        this.value = value;
        this.color = color;
    }

    public static TemplateData createNewTemplateData(String value){
        return new TemplateData(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}