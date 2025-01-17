package org.yg.mallchat.common.user.dao;

import org.yg.mallchat.common.user.domain.entity.Role;
import org.yg.mallchat.common.user.mapper.RoleMapper;
import org.yg.mallchat.common.user.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">yg</a>
 * @since 2025-01-17
 */
@Service
public class RoleDao extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
