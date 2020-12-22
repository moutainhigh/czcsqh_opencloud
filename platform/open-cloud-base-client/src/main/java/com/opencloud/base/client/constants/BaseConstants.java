package com.opencloud.base.client.constants;

import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * 通用权限常量
 *
 * @author liuyadu
 */
public class BaseConstants {

    /**
     * 服务名称
     */
    public static final String BASE_SERVER = "open-cloud-base-server";

    /**
     * 默认接口分类
     */
    public final static String DEFAULT_API_CATEGORY = "default";

    /**
     * 状态:0-无效 1-有效
     */
    public final static int ENABLED = 1;
    public final static int DISABLED = 0;

    /**
     * 客服账号
     */
    public final static Long ACCOUNT_SUPPORT = 10000L;

    /**
     * 账号状态
     * 0:禁用、1:正常、2:锁定
     */
    public final static int ACCOUNT_STATUS_DISABLE = 0;
    public final static int ACCOUNT_STATUS_NORMAL = 1;
    public final static int ACCOUNT_STATUS_LOCKED = 2;

    /**
     * 账号类型:
     * username:系统用户名、email：邮箱、mobile：手机号、qq：QQ号、weixin：微信号、weibo：微博
     */
    public final static String ACCOUNT_TYPE_USERNAME = "username";
    public final static String ACCOUNT_TYPE_EMAIL = "email";
    public final static String ACCOUNT_TYPE_MOBILE = "mobile";
    public final static String ACCOUNT_TYPE_WECHAT = "weixin";
    public final static String ACCOUNT_TYPE_MINIAPP = "miniApp";

    /**
     * 账号域
     */
    public static final String ACCOUNT_DOMAIN_ADMIN = "@admin.com";
    public static final String ACCOUNT_DOMAIN_PORTAL= "@portal.com";

    /**
     * userType
     */
    public final static String USER_TYPE_ADMIN = ",1,";
    public final static String USER_TYPE_OPERATOR = ",2,";
    public final static String USER_TYPE_SUPPORT = ",3,";
    public final static String USER_TYPE_CONSUMER = ",4,";
    public final static String USER_TYPE_HOUSE_OWNER = ",5,";


}
