package com.opencloud.base.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.base.client.constants.BaseConstants;
import com.opencloud.base.client.model.UserAccount;
import com.opencloud.base.client.model.entity.BaseAccount;
import com.opencloud.base.client.model.entity.BaseRole;
import com.opencloud.base.client.model.entity.BaseUser;
import com.opencloud.base.client.service.IBaseUserServiceClient;
import com.opencloud.base.server.service.*;
import com.opencloud.base.server.service.feign.LoginServiceClient;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.model.ResultBody;
import com.opencloud.common.security.OpenHelper;
import com.opencloud.common.utils.StringUtils;
import com.opencloud.common.utils.UUID8Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统用户信息
 *
 * @author liuyadu
 */
@Api(tags = "系统用户管理")
@RestController
public class BaseUserController implements IBaseUserServiceClient {
    @Autowired
    private BaseUserService baseUserService;
    @Autowired
    private BaseRoleService baseRoleService;
    @Autowired
    private BaseAccountService baseAccountService;
    @Autowired
    private BaseDistributionRelationService distributionRelationService;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private WeChatService weChatService;
    @Autowired
    private LoginServiceClient loginServiceClient;
    @Autowired
    private BaseConsumerService consumerService;

    /**
     * 获取登录账号信息
     *
     * @param username 登录名
     * @return
     */
    @ApiOperation(value = "获取账号登录信息", notes = "仅限系统内部调用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", required = true, value = "登录名", paramType = "path"),
    })
    @PostMapping("/user/login")
    @Override
    public ResultBody<UserAccount> userLogin(@RequestParam(value = "username") String username) {
        UserAccount account = baseUserService.login(username);
        return ResultBody.ok().data(account);
    }

    /**
     * 系统分页用户列表
     *
     * @return
     */
    @ApiOperation(value = "系统分页用户列表", notes = "系统分页用户列表")
    @GetMapping("/user")
    public ResultBody<IPage<BaseUser>> getUserList(@RequestParam(required = false) Map map) {
        return ResultBody.ok().data(baseUserService.findListPage(new PageParams(map)));
    }

    /**
     * 获取所有用户列表
     *
     * @return
     */
    @ApiOperation(value = "获取所有用户列表", notes = "获取所有用户列表")
    @GetMapping("/user/all")
    public ResultBody<List<BaseRole>> getUserAllList() {
        return ResultBody.ok().data(baseUserService.findAllList());
    }

    /**
     * 添加系统用户
     *
     * @param userName
     * @param password
     * @param nickName
     * @param status
     * @param userType
     * @param email
     * @param mobile
     * @param userDesc
     * @param avatar
     * @return
     */
    @ApiOperation(value = "添加系统用户", notes = "添加系统用户")
    @PostMapping("/user/add")
    public ResultBody<Long> addUser(
            @RequestParam(value = "userName") String userName,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "nickName") String nickName,
            @RequestParam(value = "status") Integer status,
            @RequestParam(value = "userType") String userType,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "userDesc", required = false) String userDesc,
            @RequestParam(value = "avatar", required = false) String avatar
    ) {

        BaseUser user = new BaseUser();
        user.setUserName(userName);
        user.setPassword(password);
        user.setNickName(nickName);
        user.setUserType(userType);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setUserDesc(userDesc);
        user.setAvatar(avatar);
        user.setStatus(status);
        baseUserService.addUser(user);
        return ResultBody.ok();
    }

    /**
     * 更新系统用户
     *
     * @param userId
     * @param nickName
     * @param status
     * @param userType
     * @param email
     * @param mobile
     * @param userDesc
     * @param avatar
     * @return
     */
    @ApiOperation(value = "更新系统用户", notes = "更新系统用户")
    @PostMapping("/user/update")
    public ResultBody updateUser(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "nickName") String nickName,
            @RequestParam(value = "status") Integer status,
            @RequestParam(value = "userType") String userType,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "userDesc", required = false) String userDesc,
            @RequestParam(value = "avatar", required = false) String avatar
    ) {
        BaseUser user = new BaseUser();
        user.setUserId(userId);
        user.setNickName(nickName);
        user.setUserType(userType);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setUserDesc(userDesc);
        user.setAvatar(avatar);
        user.setStatus(status);
        baseUserService.updateUser(user);
        return ResultBody.ok();
    }


    /**
     * 修改用户密码
     *
     * @param userId
     * @param password
     * @return
     */
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
    @PostMapping("/user/update/password")
    public ResultBody updatePassword(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "password") String password
    ) {
        baseUserService.updatePassword(userId, password);
        return ResultBody.ok();
    }

    /**
     * 用户分配角色
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @ApiOperation(value = "用户分配角色", notes = "用户分配角色")
    @PostMapping("/user/roles/add")
    public ResultBody addUserRoles(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "roleIds", required = false) String roleIds
    ) {
        baseRoleService.saveUserRoles(userId, StringUtils.isNotBlank(roleIds) ? roleIds.split(",") : new String[]{});
        return ResultBody.ok();
    }

    /**
     * 获取用户角色
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "获取用户已分配角色", notes = "获取用户已分配角色")
    @GetMapping("/user/roles")
    public ResultBody<List<BaseRole>> getUserRoles(
            @RequestParam(value = "userId") Long userId
    ) {
        return ResultBody.ok().data(baseRoleService.getUserRoles(userId));
    }



    /**
     * 修改用户密码
     * @return
     */
    @ApiOperation(value = "修改密码", notes = "修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPassword", required = false, value = "旧密码", paramType = "form"),
            @ApiImplicitParam(name = "newPassword", required = true, value = "新密码", paramType = "form")
    })
    @PostMapping("/user/update/changePassword")
    public ResultBody changePassword(
            @RequestParam(value = "oldPassword") String oldPassword,
            @RequestParam(value = "newPassword") String newPassword
    ){
        if(StringUtils.isBlank(oldPassword)) {
            return ResultBody.failed().msg("请输入原密码");
        }
        if(StringUtils.isBlank(newPassword)) {
            return ResultBody.failed().msg("请输入新密码");
        }

        BaseUser user = new BaseUser();
        user.setUserId(OpenHelper.getUserId());
        baseAccountService.changePassword(user.getUserId(),oldPassword,newPassword);

        return ResultBody.ok();
    }


    /**
     * 根据指定userId查询用户
     * @param userId
     * @return
     */
    @ApiOperation(value = "根据指定userId查询用户", notes = "根据指定userId查询用户")
    @GetMapping("/user/findUserInfoById")
    public ResultBody<BaseUser> getUserInfoById(@RequestParam(value = "userId") Long userId) {
        BaseUser baseUser = baseUserService.getUserById(userId);
        return ResultBody.ok().data(baseUser);
    }




    /**
     * 根据OpenHelpUserId查询用户信息
     * @return
     */
    @ApiOperation(value = "根据OpenHelpUserId查询用户信息", notes = "根据OpenHelpUserId查询用户信息")
    @GetMapping("/user/getBaseUserInfoByOpenHelp")
    public ResultBody<BaseUser> getBaseUserInfoByOpenHelp() {
        return ResultBody.ok().data(baseUserService.getUserById(OpenHelper.getUserId()));
    }


    /**
     * 校验手机号是否存在
     * @return
     */
    @ApiOperation(value = "校验手机号是否存在", notes = "校验手机号是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", required = true, value = "手机号", paramType = "form", dataType = "string")
    })
    @PostMapping("/user/checkMobileExist")
    public ResultBody checkMobileExist(
            @RequestParam(value = "mobile") String mobile
    ){
        if(StringUtils.isBlank(mobile)){
            return ResultBody.failed().msg("请输入手机号");
        }
        if(!StringUtils.matchMobile(mobile)) {
            return ResultBody.failed().msg("手机号格式错误").code(1001);
        }

        boolean exist = baseAccountService.checkMobileExist(mobile);
        if (!exist) {
            return ResultBody.failed().msg("该手机号尚未注册");
        }

        return ResultBody.ok();
    }

    /**
     * 重置密码
     * @return
     */
    @ApiOperation(value = "重置密码", notes = "重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", required = true, value = "手机号", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "code", required = true, value = "验证码", paramType = "form", dataType = "string"),
            @ApiImplicitParam(name = "newPassword", required = true, value = "新密码", paramType = "form", dataType = "string")
    })
    @PostMapping("/user/resetPassword")
    public ResultBody resetPassword(
            @RequestParam(value = "mobile") String mobile,
            @RequestParam(value = "code") String code,
            @RequestParam(value = "newPassword") String newPassword){
        if(StringUtils.isBlank(newPassword)){
            return ResultBody.failed().msg("请输入新密码");
        }
        if(StringUtils.isBlank(code)){
            return ResultBody.failed().msg("请输入验证码");
        }
        if(StringUtils.isBlank(mobile)){
            return ResultBody.failed().msg("请输入手机号");
        }
        if(!StringUtils.matchMobile(mobile)) {
            return ResultBody.failed().msg("手机号格式错误").code(1001);
        }

        String checkCodeRedis = (String) redisTemplate.opsForValue().get("checkCode_2_" + mobile);

        if(!(redisTemplate.hasKey("checkCode_2_" + mobile))) {
            return ResultBody.failed().msg("请重新获取验证码.");
        }
        if(StringUtils.isBlank(checkCodeRedis)) {
            return  ResultBody.failed().msg("请输入手机验证码");
        }
        if(!checkCodeRedis.equals(code)) {
            return ResultBody.failed().msg("验证码错误");
        }

        baseAccountService.resetPassword(mobile, newPassword);
        return ResultBody.ok();
    }


    /**
     * 重置分销关系闭包表
     */
    @ApiOperation(value = "重置分销关系闭包表",notes = "重置分销关系闭包表")
    @GetMapping("/resetDistributionRelationTable")
    public ResultBody resetDistributionRelationTable(){
        distributionRelationService.resetDistributionRelationTable(10000L);
        return ResultBody.ok();
    }

    @ApiOperation(value = "根据手机号码查询用户信息" , notes = "根据手机号码查询用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号码" , name = "mobile" ,required = true)
    })
    @GetMapping("/getUserInfoByMobile")
    public ResultBody getUserInfoByMobile(@RequestParam(value = "mobile")String mobile){
        Map returnMap = new HashMap();
        if(StringUtils.isNotBlank(mobile)) {
            QueryWrapper<BaseUser> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.lambda().eq(BaseUser::getMobile, mobile);
            BaseUser baseUser = baseUserService.getOne(userQueryWrapper);
        }
        return ResultBody.ok().data(returnMap);
    }


    @ApiOperation(value = "小程序注册用户" , notes = "小程序注册用户")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "openId" , name = "openId",required = true),
            @ApiImplicitParam(value = "头像url" , name = "avatarUrl",required = true),
            @ApiImplicitParam(value = "iv" , name = "iv",required = true),
            @ApiImplicitParam(value = "session_key" , name = "session_key",required = true),
            @ApiImplicitParam(value = "encryptedData" , name = "encryptedData",required = true)
    })
    @PostMapping("/wxMiniAddUser")
    public ResultBody wxMiniAddUser(@RequestParam(value = "openId")String openId,
                                        @RequestParam(value = "avatarUrl")String avatarUrl,
                                        @RequestParam(value = "iv")String iv,
                                        @RequestParam(value = "session_key")String session_key,
                                        @RequestParam(value = "encryptedData")String encryptedData){
        //先解密手机号码
        String decryptString = weChatService.wxMiniDecrypt(iv,session_key,encryptedData,"consumer");
        JSONObject decryptJson = JSONObject.parseObject(decryptString);
        String mobile = decryptJson.get("phoneNumber").toString();

        if(StringUtils.isBlank(mobile)){
            return ResultBody.failed().msg("请输入手机号");
        }
        if(!StringUtils.matchMobile(mobile)) {
            return ResultBody.failed().msg("手机号格式错误").code(1001);
        }
        //然后判断这个手机号存在不存在，存在的话，有没有绑定过小程序。
        boolean exist = baseAccountService.checkMobileExist(mobile);
        Map returnMap = new HashMap();
        if(exist){
            QueryWrapper<BaseAccount> accountQueryWrapper = new QueryWrapper<>();
            accountQueryWrapper.lambda().eq(BaseAccount::getAccount, mobile);
            BaseAccount account = baseAccountService.getOne(accountQueryWrapper);
            //判断一下这个account的角色类型
            BaseUser baseUser = baseUserService.getUserById(account.getUserId());

            //然后那这个userid去找找，如果有绑定过小程序，使用那个小程序账号进行登陆。
            accountQueryWrapper = new QueryWrapper<>();
            accountQueryWrapper.lambda().eq(BaseAccount::getUserId,account.getUserId()).eq(BaseAccount::getAccountType,BaseConstants.ACCOUNT_TYPE_MINIAPP).eq(BaseAccount::getAccount,openId);
            BaseAccount miniAppAccount = baseAccountService.getOne(accountQueryWrapper);
            if(miniAppAccount != null){
                //有绑定过小程序，使用那个小程序账号进行登陆。
                try {
                    ResultBody<OAuth2AccessToken> resultBody = loginServiceClient.getLoginToken(account.getAccount(), account.getAccount() + "lanpei");
                    returnMap.put("token", resultBody.getData().getValue());
                    returnMap.put("userType",baseUser.getUserType());
                    return ResultBody.ok().data(returnMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                //没有绑定过小程序，则为这个账号绑定小程序，并进行登陆
                consumerService.bindingUserAndWxMiniOpenid(account.getUserId(), openId);
                try {
                    ResultBody<OAuth2AccessToken> resultBody = loginServiceClient.getLoginToken(openId, openId + "lanpei");
                    returnMap.put("token", resultBody.getData().getValue());
                    returnMap.put("userType",baseUser.getUserType());
                    return ResultBody.ok().data(returnMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }else {
            //如果手机号不存在，创建账号，并进行登陆
            BaseUser baseUser = new BaseUser();
            baseUser.setAvatar(avatarUrl);
            baseUser.setMobile(mobile);
            baseUser.setPassword(UUID8Util.generateShortUuid());
            //按正常流程添加一个用户
            consumerService.addConsumer(baseUser);
            //为用户添加一条小程序账号记录
            consumerService.bindingUserAndWxMiniOpenid(baseUser.getUserId(), openId);
            try {
                ResultBody<OAuth2AccessToken> resultBody = loginServiceClient.getLoginToken(openId, openId + "lanpei");
                returnMap.put("userType",baseUser.getUserType());
                returnMap.put("token", resultBody.getData().getValue());
                return ResultBody.ok().data(returnMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResultBody.ok();
    }

}

