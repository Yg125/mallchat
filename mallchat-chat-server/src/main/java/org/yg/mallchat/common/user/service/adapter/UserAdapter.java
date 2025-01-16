package org.yg.mallchat.common.user.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import org.yg.mallchat.common.user.domain.entity.User;
import org.yg.mallchat.common.user.domain.vo.resp.UserInfoResp;

/**
 * @author yangang
 * @create 2025-01-14-下午8:37
 */
public class UserAdapter {

    public static User buildUserSave(String openId) {
        return User.builder().openId(openId).build();
    }

    public static User buildAuthorizeUser(Long uid, WxOAuth2UserInfo userInfo) {
        User user = new User();
        user.setId(uid);
        user.setName(userInfo.getNickname());
        user.setAvatar(userInfo.getHeadImgUrl());
        return user;
    }

    public static UserInfoResp buildUserInfo(User user, Integer modifyNameCount) {
        UserInfoResp vo = new UserInfoResp();
        BeanUtil.copyProperties(user, vo);
        vo.setModifyNameChance(modifyNameCount);
        return vo;
    }
}
