package org.yg.mallchat.common.user.dao;

import org.yg.mallchat.common.user.domain.entity.UserRole;
import org.yg.mallchat.common.user.mapper.UserRoleMapper;
import org.yg.mallchat.common.user.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户角色关系表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">yg</a>
 * @since 2025-01-17
 */
@Service
public class UserRoleDao extends ServiceImpl<UserRoleMapper, UserRole>{

    public List<UserRole> listByUid(Long uid) {
        return lambdaQuery()
                .eq(UserRole::getUid, uid)
                .list();
    }
}
