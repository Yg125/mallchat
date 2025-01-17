package org.yg.mallchat.common.websocket.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yg.mallchat.common.common.domain.vo.resp.ApiResult;
import org.yg.mallchat.common.common.utils.JsonUtils;
import org.yg.mallchat.common.user.dao.UserDao;
import org.yg.mallchat.common.user.domain.entity.IpDetail;
import org.yg.mallchat.common.user.domain.entity.IpInfo;
import org.yg.mallchat.common.user.domain.entity.User;
import org.yg.mallchat.common.websocket.service.IpService;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.Date;

/**
 * @author yangang
 * @create 2025-01-17-下午2:19
 */
@Service
@Slf4j
public class IpServiceImpl implements IpService {
    private static ExecutorService executor = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(500), new NamedThreadFactory("refresh-ipDetail", false));

    @Autowired
    private UserDao userDao;
    @Override
    public void refreshIpDetailAsyns(Long uid) {
        executor.execute(()->{
           User user = userDao.getById(uid);
            IpInfo ipInfo = user.getIpInfo();
            if(Objects.isNull(ipInfo)){
                return ;
            }
            String ip = ipInfo.needRefreshIp();
            if(StringUtils.isBlank(ip)){
                return ;
            }
            IpDetail ipDetail = tryGetIpDetailOrNullThreeTimes(ip);
            if(Objects.nonNull(ipDetail)){
                ipInfo.refreshIpDetail(ipDetail);
                User update = new User();
                update.setId(uid);
                update.setIpInfo(ipInfo);
                userDao.updateById(update);
            }
        });

    }

    private static IpDetail tryGetIpDetailOrNullThreeTimes(String ip) {
        for(int i = 0;i < 3;i ++){
            IpDetail ipDetail = getIpDetailOrNull(ip);
            if(Objects.nonNull(ipDetail)){
                return ipDetail;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                log.error("tryGetIpDetailOrNullThreeTimes InterruptedException", e);
            }
        }
        return null;
    }

    public static IpDetail getIpDetailOrNull(String ip) {
        try {
            // 淘宝解析ip
            String url = "https://ip.taobao.com/outGetIpInfo?ip=" + ip + "&accessKey=alibaba-inc";
            String data = HttpUtil.get(url);
            ApiResult<IpDetail> result = JsonUtils.toObj(data, new TypeReference<ApiResult<IpDetail>>() {});
            IpDetail detail = result.getData();
            return detail;
        }catch(Exception e){
            return null;
        }
    }

}
