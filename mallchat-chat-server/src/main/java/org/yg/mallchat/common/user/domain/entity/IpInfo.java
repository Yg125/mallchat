package org.yg.mallchat.common.user.domain.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * @author yangang
 * @create 2025-01-17-下午2:02
 */
@Data
public class IpInfo implements Serializable {
    // 注册时的IP
    private String createIp;
    // 最新登录的IP
    private String updateIp;
    // 注册时的IP详情
    private IpDetail createIpDetail;
    // 最新登录的IP详情
    private IpDetail updateIpDetail;

    public void refreshIp(String ip) {
        if(StringUtils.isBlank(ip)) {
            return;
        }
        if(StringUtils.isBlank(createIp)){
            createIp = ip;
        }
        updateIp = ip;
    }


    public String needRefreshIp() {
        boolean noNeedRefresh = Optional.ofNullable(updateIpDetail)
                .map(IpDetail::getIp)
                .filter(ip -> Objects.equals(ip, updateIp))
                .isPresent();
        return noNeedRefresh ? null: updateIp;
    }


    public void refreshIpDetail(IpDetail ipDetail) {
        if(Objects.equals(createIp, ipDetail.getIp())) {
            createIpDetail = ipDetail;
        }
        if(Objects.equals(updateIp, ipDetail.getIp())) {
            updateIpDetail = ipDetail;
        }
    }
}
