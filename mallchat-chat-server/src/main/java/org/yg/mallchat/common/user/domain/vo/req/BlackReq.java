package org.yg.mallchat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yangang
 * @create 2025-01-16-下午8:25
 */
@Data
public class BlackReq {
    @ApiModelProperty("拉黑用户的uid")
    @NotNull
    private Long uid;
}
