package org.yg.mallchat.common.user.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.yg.mallchat.common.user.domain.enums.BlackTypeEnum;

/**
 * <p>
 * 黑名单
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">yg</a>
 * @since 2025-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("black")
public class Black implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
      @TableId(value = "id", type = IdType.AUTO)
      private Long id;

    /**
     * 拉黑目标类型 1.ip 2uid
     * @see BlackTypeEnum
     */
    @TableField("type")
    private Integer type;

    /**
     * 拉黑目标
     */
    @TableField("target")
    private String target;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;


}
