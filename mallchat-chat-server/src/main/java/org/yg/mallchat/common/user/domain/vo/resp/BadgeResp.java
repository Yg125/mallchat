package org.yg.mallchat.common.user.domain.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yangang
 * @create 2025-01-16-下午2:40
 */
@Data
public class BadgeResp {
    @ApiModelProperty(value="徽章id")
    private Long id;
    @ApiModelProperty(value="徽章图标")
    private String img;
    @ApiModelProperty(value="徽章描述")
    private String describe;
    @ApiModelProperty(value="是否拥有 0否 1是")
    private Integer obtain;
    @ApiModelProperty(value="是否佩戴 0否 1是")
    private Integer wearing;
}
