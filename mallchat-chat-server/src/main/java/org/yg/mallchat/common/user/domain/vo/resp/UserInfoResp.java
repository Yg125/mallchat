package org.yg.mallchat.common.user.domain.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yangang
 * @create 2025-01-16-下午2:40
 */
@Data
public class UserInfoResp {
    @ApiModelProperty(value="uid")
    private Long id;
    @ApiModelProperty(value="用户名称")
    private String name;
    @ApiModelProperty(value="用户头像")
    private String avatar;
    @ApiModelProperty(value="用户性别")
    private Integer sex;
    @ApiModelProperty(value="改名卡次数")
    private Integer modifyNameChance;
}
