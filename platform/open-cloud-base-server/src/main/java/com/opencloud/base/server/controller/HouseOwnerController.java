package com.opencloud.base.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.opencloud.base.client.constants.BaseConstants;
import com.opencloud.base.client.model.entity.BaseUser;
import com.opencloud.base.client.model.entity.HouseOwner;
import com.opencloud.base.server.service.BaseUserService;
import com.opencloud.base.server.service.HouseOwnerService;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.model.ResultBody;
import com.opencloud.common.security.OpenHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

/**
 * 房东表 前端控制器
 *
 * @author yanjiajun
 * @date 2020-10-28
 */
@Api(value = "房东管理", tags = "房东管理")
@RestController
@RequestMapping("houseOwner")
public class HouseOwnerController {

    @Autowired
    private HouseOwnerService targetService;

    @Autowired
    private BaseUserService userService;

    @ApiOperation(value = "用户成为房东" ,notes = "用户成为房东")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "userId" , name = "userId" ,required = true)
    })
    @PostMapping("/transferHouseOwner")
    public ResultBody transferHouseOwner(@RequestParam("userId")Long userId){
        //校验执行该方法的是不是客服
        BaseUser myself = userService.getById(OpenHelper.getUserId());
        if(myself.getUserType().equals(BaseConstants.USER_TYPE_SUPPORT)){
            BaseUser user = userService.getById(userId);
            user.setUserType(BaseConstants.USER_TYPE_HOUSE_OWNER);
            userService.updateUser(user);
            //去房东表加一条记录
            HouseOwner houseOwner = new HouseOwner();
            houseOwner.setStatus(1);
            houseOwner.setUserId(user.getUserId());
            targetService.save(houseOwner);
            return ResultBody.ok();
        }else {
            return ResultBody.failed().msg("该角色无权限执行");
        }
    }


}
