package org.yg.mallchat.common.user.controller;


import cn.hutool.system.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.yg.mallchat.common.common.domain.dto.RequestInfo;
import org.yg.mallchat.common.common.domain.vo.resp.ApiResult;
import org.yg.mallchat.common.common.utils.RequestHolder;
import org.yg.mallchat.common.user.domain.entity.User;
import org.yg.mallchat.common.user.domain.vo.req.ModifyNameReq;
import org.yg.mallchat.common.user.domain.vo.resp.UserInfoResp;
import org.yg.mallchat.common.user.service.UserService;

import javax.validation.Valid;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">yg</a>
 * @since 2025-01-13
 */
@RestController
@RequestMapping("/capi/user")
@Api(tags="用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    @ApiOperation("获取用户个人信息")
    public ApiResult<UserInfoResp> getUserInfo(){
        return ApiResult.success(userService.getUserInfo(RequestHolder.get().getUid()));
    }

    @PutMapping("/name")
    @ApiOperation("修改用户名")
    public ApiResult<Void> modifyName(@Valid @RequestBody ModifyNameReq req){
        userService.modifyName(RequestHolder.get().getUid(), req.getName());
        return ApiResult.success();
    }
}

