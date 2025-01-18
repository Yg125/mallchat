package org.yg.mallchat.common.user.service;

import org.yg.mallchat.common.user.domain.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import org.yg.mallchat.common.user.domain.enums.RoleEnum;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">yg</a>
 * @since 2025-01-17
 */
public interface IRoleService{

    /**
     * 是否拥有某个权限
     */
    boolean hasPower(Long uid, RoleEnum roleEnum);
}
