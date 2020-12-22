package com.opencloud.common.model;

import java.io.Serializable;

public class ServiceResultBody<T> implements Serializable {
    
    private static final long serialVersionUID = -6190689122701100762L;
    
    /**
     * 响应编码:0-请求处理成功,其余为错误
     */
    private int code = 0;
    
    /**
     * 提示消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    
    public ServiceResultBody(int code, String message, T data) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public ServiceResultBody(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
}
