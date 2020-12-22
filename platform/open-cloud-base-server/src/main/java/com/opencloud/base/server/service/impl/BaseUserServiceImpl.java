package com.opencloud.base.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opencloud.base.client.constants.BaseConstants;
import com.opencloud.base.client.model.UserAccount;
import com.opencloud.base.client.model.entity.*;
import com.opencloud.base.server.mapper.BaseUserMapper;
import com.opencloud.base.server.service.*;
import com.opencloud.common.constants.CommonConstants;
import com.opencloud.common.exception.OpenAlertException;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.common.security.OpenAuthority;
import com.opencloud.common.security.OpenHelper;
import com.opencloud.common.security.OpenSecurityConstants;
import com.opencloud.common.security.OpenUserDetails;
import com.opencloud.common.utils.BeanConvertUtils;
import com.opencloud.common.utils.StringUtils;
import com.opencloud.common.utils.WebUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author: liuyadu
 * @date: 2018/10/24 16:33
 * @description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseUserServiceImpl extends BaseServiceImpl<BaseUserMapper, BaseUser> implements BaseUserService {

    @Autowired
    private BaseUserMapper baseUserMapper;
    @Autowired
    private BaseRoleService roleService;
    @Autowired
    private BaseAuthorityService baseAuthorityService;
    @Autowired
    private BaseAccountService baseAccountService;

    @Autowired
    private BaseDistributionRelationService distributionRelationService;

    @Autowired
    private BaseDistributionRelationService baseDistributionRelationService;

    private final String ACCOUNT_DOMAIN = BaseConstants.ACCOUNT_DOMAIN_ADMIN;

    /**
     * 添加系统用户
     *
     * @param baseUser
     * @return
     */
    @Override
    public void addUser(BaseUser baseUser) {
        List<BaseUser> oldBaseUserList = getOldUser(baseUser.getUserName(), baseUser.getMobile());
        if (!oldBaseUserList.isEmpty()) {
            for (int i = 0; i < oldBaseUserList.size(); i++) {
                BaseUser oldBaseUser = oldBaseUserList.get(i);
                if (oldBaseUser.getUserName().equals(baseUser.getUserName())) {
                    throw new OpenAlertException("用户名:" + baseUser.getUserName() + "已存在!");
                }
                if (oldBaseUser.getMobile().equals(baseUser.getMobile())) {
                    throw new OpenAlertException("手机号:" + baseUser.getMobile() + "已存在!");
                }
            }
        }
        baseUser.setCreateTime(new Date());
        baseUser.setUpdateTime(baseUser.getCreateTime());
        //保存系统用户信息
        baseUserMapper.insert(baseUser);
        //默认注册用户名账户
        baseAccountService.register(baseUser.getUserId(), baseUser.getUserName(), baseUser.getPassword(), BaseConstants.ACCOUNT_TYPE_USERNAME, baseUser.getStatus(), ACCOUNT_DOMAIN, null);
        if (StringUtils.matchEmail(baseUser.getEmail())) {
            //注册email账号登陆
            baseAccountService.register(baseUser.getUserId(), baseUser.getEmail(), baseUser.getPassword(), BaseConstants.ACCOUNT_TYPE_EMAIL, baseUser.getStatus(), ACCOUNT_DOMAIN, null);
        }
        if (StringUtils.matchMobile(baseUser.getMobile())) {
            //注册手机号账号登陆
            baseAccountService.register(baseUser.getUserId(), baseUser.getMobile(), baseUser.getPassword(), BaseConstants.ACCOUNT_TYPE_MOBILE, baseUser.getStatus(), ACCOUNT_DOMAIN, null);
        }
        //TODO
        //这里要等确定了角色的类型才能继续写
    }

    /**
     * 更新系统用户
     *
     * @param baseUser
     * @return
     */
    @Override
    public void updateUser(BaseUser baseUser) {
        if (baseUser == null || baseUser.getUserId() == null) {
            return;
        }
        List<BaseUser> oldBaseUserList = getOldUser(baseUser.getUserName(), baseUser.getMobile());
        if (!oldBaseUserList.isEmpty()) {
            for (int i = 0; i < oldBaseUserList.size(); i++) {
                BaseUser oldBaseUser = oldBaseUserList.get(i);
                if (oldBaseUser.getUserName().equals(baseUser.getUserName()) && oldBaseUser.getUserId().longValue() != baseUser.getUserId().longValue()) {
                    throw new OpenAlertException("用户名:" + baseUser.getUserName() + "已存在!");
                }
                if (oldBaseUser.getMobile() != null && baseUser.getMobile() != null && oldBaseUser.getMobile().equals(baseUser.getMobile()) && oldBaseUser.getUserId().longValue() != baseUser.getUserId().longValue()) {
                    throw new OpenAlertException("手机号:" + baseUser.getMobile() + "已存在!");
                }
            }
        }
        baseAccountService.updateMobileAccount(baseUser.getUserId(), baseUser.getMobile());

        if (baseUser.getStatus() != null) {
            baseAccountService.updateStatusByUserId(baseUser.getUserId(), ACCOUNT_DOMAIN, baseUser.getStatus());
        }
        baseUser.setUpdateTime(new Date());
        baseUserMapper.updateById(baseUser);
    }


    /**
     * 更新密码
     *
     * @param userId
     * @param password
     */
    @Override
    public void updatePassword(Long userId, String password) {
        baseAccountService.updatePasswordByUserId(userId, ACCOUNT_DOMAIN, password);
    }

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<BaseUser> findListPage(PageParams pageParams) {
        BaseUser query = pageParams.mapToObject(BaseUser.class);
        QueryWrapper<BaseUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(ObjectUtils.isNotEmpty(query.getUserId()), BaseUser::getUserId, query.getUserId())
                .eq(ObjectUtils.isNotEmpty(query.getUserType()), BaseUser::getUserType, query.getUserType())
                .eq(ObjectUtils.isNotEmpty(query.getUserName()), BaseUser::getUserName, query.getUserName())
                .eq(ObjectUtils.isNotEmpty(query.getMobile()), BaseUser::getMobile, query.getMobile());
        queryWrapper.orderByDesc("create_time");
        return baseUserMapper.selectPage(pageParams, queryWrapper);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<BaseUser> findAllList() {
        List<BaseUser> list = baseUserMapper.selectList(new QueryWrapper<>());
        return list;
    }

    /**
     * 依据系统用户Id查询系统用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public BaseUser getUserById(Long userId) {
        BaseUser user = baseUserMapper.selectById(userId);
        return user;
    }

    /**
     * 根据用户ID获取用户信息和权限
     *
     * @param userId
     * @return
     */
    @Override
    public UserAccount getUserAccount(Long userId) {
        // 用户权限列表
        List<OpenAuthority> authorities = Lists.newArrayList();
        // 用户角色列表
        List<Map> roles = Lists.newArrayList();
        List<BaseRole> rolesList = roleService.getUserRoles(userId);
        if (rolesList != null) {
            for (BaseRole role : rolesList) {
                Map roleMap = Maps.newHashMap();
                roleMap.put("roleId", role.getRoleId());
                roleMap.put("roleCode", role.getRoleCode());
                roleMap.put("roleName", role.getRoleName());
                // 用户角色详情
                roles.add(roleMap);
                // 加入角色标识
                OpenAuthority authority = new OpenAuthority(role.getRoleId().toString(), OpenSecurityConstants.AUTHORITY_PREFIX_ROLE + role.getRoleCode(), null, "role");
                authorities.add(authority);
            }
        }

        //查询系统用户资料
        BaseUser baseUser = getUserById(userId);


        // 加入用户权限
        List<OpenAuthority> userGrantedAuthority = baseAuthorityService.findAuthorityByUser(userId, CommonConstants.ROOT.equals(baseUser.getUserName()));
        if (userGrantedAuthority != null && userGrantedAuthority.size() > 0) {
            authorities.addAll(userGrantedAuthority);
        }
        UserAccount userAccount = new UserAccount();
        // 昵称
        userAccount.setNickName(baseUser.getNickName());

        //....
        //电话
        userAccount.setMobile(baseUser.getMobile());
        //性别
        userAccount.setSex(baseUser.getSex());
        //描述
        userAccount.setUserDesc(baseUser.getUserDesc());
        userAccount.setUserName(baseUser.getUserName());

        // 头像
        userAccount.setAvatar(baseUser.getAvatar());

        // 权限信息
        userAccount.setAuthorities(authorities);
        userAccount.setRoles(roles);


        //用户类型
        userAccount.setUserType(baseUser.getUserType());
        return userAccount;


    }


    /**
     * 依据登录名查询系统用户信息
     *
     * @param username
     * @return
     */
    @Override
    public BaseUser getUserByUsername(String username) {
        QueryWrapper<BaseUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseUser::getUserName, username);
        BaseUser saved = baseUserMapper.selectOne(queryWrapper);
        return saved;
    }




    /**
     * 支持系统用户名、手机号、email登陆
     *
     * @param account
     * @return
     */
    @Override
    public UserAccount login(String account) {
        if (StringUtils.isBlank(account)) {
            return null;
        }
        Map<String, String> parameterMap = WebUtils.getParameterMap(WebUtils.getHttpServletRequest());
        // 第三方登录标识
        String loginType = parameterMap.get("login_type");
        BaseAccount baseAccount = null;
        if (StringUtils.isNotBlank(loginType)) {
            baseAccount = baseAccountService.getAccount(account, loginType, ACCOUNT_DOMAIN);
        } else {
            // 非第三方登录

            //用户名登录
            baseAccount = baseAccountService.getAccount(account, BaseConstants.ACCOUNT_TYPE_USERNAME, ACCOUNT_DOMAIN);

            // 手机号登陆
            if (StringUtils.matchMobile(account)) {
                baseAccount = baseAccountService.getAccount(account, BaseConstants.ACCOUNT_TYPE_MOBILE, ACCOUNT_DOMAIN);
            }
            // 邮箱登陆
            if (StringUtils.matchEmail(account)) {
                baseAccount = baseAccountService.getAccount(account, BaseConstants.ACCOUNT_TYPE_EMAIL, ACCOUNT_DOMAIN);
            }
            Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]");
            Matcher matcher = pattern.matcher(account);
            if (account.length() == 28 && !matcher.find()) {
                baseAccount = baseAccountService.getAccount(account, BaseConstants.ACCOUNT_TYPE_WECHAT, ACCOUNT_DOMAIN);
                if(baseAccount == null){
                    baseAccount = baseAccountService.getAccount(account, BaseConstants.ACCOUNT_TYPE_MINIAPP, ACCOUNT_DOMAIN);
                }
            }

        }

        // 获取用户详细信息
        if (baseAccount != null) {
            //添加登录日志
            try {
                HttpServletRequest request = WebUtils.getHttpServletRequest();
                if (request != null) {
                    BaseAccountLogs log = new BaseAccountLogs();
                    log.setDomain(ACCOUNT_DOMAIN);
                    log.setUserId(baseAccount.getUserId());
                    log.setAccount(baseAccount.getAccount());
                    log.setAccountId(String.valueOf(baseAccount.getAccountId()));
                    log.setAccountType(baseAccount.getAccountType());
                    log.setLoginIp(WebUtils.getRemoteAddress(request));
                    log.setLoginAgent(request.getHeader(HttpHeaders.USER_AGENT));
                    baseAccountService.addLoginLog(log);
                }
            } catch (Exception e) {
                log.error("添加登录日志失败:{}", e);
            }
            // 用户权限信息
            UserAccount userAccount = getUserAccount(baseAccount.getUserId());
            // 复制账号信息
            BeanUtils.copyProperties(baseAccount, userAccount);
            return userAccount;
        }
        return null;
    }

    /**
     * 查询手机号是否存在
     */
    @Override
    public Integer checkMobile(String mobile) {
        QueryWrapper<BaseUser> queryWrapper = new QueryWrapper<BaseUser>();
        queryWrapper.lambda().eq(BaseUser::getMobile, mobile)
                .in(BaseUser::getStatus, 0, 1);
        Integer count = baseUserMapper.selectCount(queryWrapper);
        return count;
    }

    /**
     * 检测用户名是否存在
     *
     * @param userName
     * @return
     */
    public List<BaseUser> getOldUser(String userName, String mobile) {
        if (StringUtils.isBlank(userName)) {
            throw new OpenAlertException("用户名不能为空!!");
        }
        QueryWrapper<BaseUser> queryWrapper = new QueryWrapper<BaseUser>();
        queryWrapper.lambda().and(wrapper -> wrapper.eq(BaseUser::getUserName, userName).or().eq(BaseUser::getMobile, mobile))
                .ne(BaseUser::getStatus, 2);
        return baseUserMapper.selectList(queryWrapper);
    }


    @Override
    public BaseUser getRecommendByUserId(Long userId) {
        if (userId == null) {
            return new BaseUser();
        }
        QueryWrapper<BaseUser> queryWrapper = new QueryWrapper<BaseUser>();
        queryWrapper.lambda().eq(BaseUser::getUserId, userId)
                .select(BaseUser::getRecommendId, BaseUser::getRecommendId);
        BaseUser user = baseUserMapper.selectOne(queryWrapper);
        return user;
    }

    /**
     * 根据userId获取微信openid
     */

    public String getWeChatOpenIdByUserId(Long userId) {
        QueryWrapper<BaseAccount> accountQueryWrapper = new QueryWrapper<>();
        accountQueryWrapper.lambda().eq(BaseAccount::getUserId, userId).eq(BaseAccount::getAccountType, BaseConstants.ACCOUNT_TYPE_WECHAT);
        List<BaseAccount> accountList = baseAccountService.list(accountQueryWrapper);
        if (accountList != null && accountList.size() > 0) {
            BaseAccount temp = accountList.get(0);
            return temp.getAccount();
        }
        return "";
    }


    @Override
    public List<Long> getUserIdListByCondition(String conditionMapString) {
        //把json字符串转换成map
        Map conditionMap = JSON.parseObject(conditionMapString, Map.class);
        BaseUser query = BeanConvertUtils.mapToObject(conditionMap, BaseUser.class);
        //暂时提供对BaseUser的userRealName和mobile进行查询
        QueryWrapper<BaseUser> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().like(ObjectUtils.isNotEmpty(query.getUserName()), BaseUser::getUserName, query.getUserName())
                .like(ObjectUtils.isNotEmpty(query.getMobile()), BaseUser::getMobile, query.getMobile())
                .like(ObjectUtils.isNotEmpty(query.getUserName()), BaseUser::getUserName, query.getUserName())
                .eq(ObjectUtils.isNotEmpty(query.getStatus()), BaseUser::getStatus, query.getStatus());
        if (userQueryWrapper.isEmptyOfWhere()) {
            return new ArrayList<Long>();
        } else {
            List<BaseUser> userList = baseUserMapper.selectList(userQueryWrapper);
            List<Long> userIdList = userList.stream().map(BaseUser::getUserId).collect(Collectors.toList());
            return userIdList;
        }

    }

    @Override
    public List<BaseUser> getUserListByUserIdList(String userIdListString) {
        //把json字符串转成List
        List userIdList = JSON.parseArray(userIdListString);
        QueryWrapper<BaseUser> userQueryWrapper = new QueryWrapper<>();
        List<BaseUser> userList = new ArrayList<>();
        if (userIdList != null && userIdList.size() > 0) {
            userQueryWrapper.lambda().in(BaseUser::getUserId, userIdList);
            userList = baseUserMapper.selectList(userQueryWrapper);
        }
        return userList;
    }

    @Override
    public List<Long> getUserIdListByOrCondition(String conditionMapString) {
        //把json字符串转换成map
        Map conditionMap = JSON.parseObject(conditionMapString, Map.class);
        BaseUser query = BeanConvertUtils.mapToObject(conditionMap, BaseUser.class);
        if (StringUtils.isAllBlank(query.getUserName(), query.getMobile())) {
            return new ArrayList<Long>();
        }
        //暂时提供对BaseUser的userRealName和mobile进行查询
        QueryWrapper<BaseUser> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().eq(ObjectUtils.isNotEmpty(query.getStatus()), BaseUser::getStatus, 1)
                .and(wrapper -> wrapper.like(ObjectUtils.isNotEmpty(query.getMobile()), BaseUser::getMobile, query.getMobile()).or().like(ObjectUtils.isNotEmpty(query.getUserName()), BaseUser::getUserName, query.getUserName()));
        List<BaseUser> userList = baseUserMapper.selectList(userQueryWrapper);
        List<Long> userIdList = userList.stream().map(BaseUser::getUserId).collect(Collectors.toList());
        return userIdList;
    }


}
