package org.yg.mallchat.common.user.service;

import org.yg.mallchat.common.user.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.yg.mallchat.common.user.domain.vo.resp.UserInfoResp;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">yg</a>
 * @since 2025-01-13
 */
public interface UserService{

    Long register(User insert);

    UserInfoResp getUserInfo(Long uid);

    void modifyName(Long uid, String name);
}
