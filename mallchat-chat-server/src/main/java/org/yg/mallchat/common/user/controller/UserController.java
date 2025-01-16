package org.yg.mallchat.common.user.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yg.mallchat.common.common.domain.dto.RequestInfo;
import org.yg.mallchat.common.common.domain.vo.resp.ApiResult;
import org.yg.mallchat.common.common.utils.RequestHolder;
import org.yg.mallchat.common.user.domain.vo.resp.UserInfoResp;
import org.yg.mallchat.common.user.service.UserService;

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
}

