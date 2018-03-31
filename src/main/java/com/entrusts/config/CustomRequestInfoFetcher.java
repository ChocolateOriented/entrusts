package com.entrusts.config;

import com.mo9.nest.client.info.DefaultRequestInfoFetcher;
import com.mo9.nest.util.enums.SystemPlatformEnum;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by sxu on 2018/2/28.
 */
@Component
public class CustomRequestInfoFetcher  extends DefaultRequestInfoFetcher {

    /**
     * 获取用户账户编号, 必须传递
     */
    @Override
    public String getAccountCode(HttpServletRequest request) {
        return request.getHeader("Account-Code");
    }

    /**
     * 访问令牌, 必须传递
     */
    @Override
    public String getAccessToken(HttpServletRequest request) {
        return request.getHeader("Access-Token");
    }

    /**
     * 获取用户账户编号, 包含APP/WEB, 必须传递
     */
    @Override
    public SystemPlatformEnum getSystemPlatform(HttpServletRequest request) {
        return super.getSystemPlatform(request);
    }

    /**
     * 获取客户端版本号, APP传递, WEB返回null即可
     */
    @Override
    public String getClientId(HttpServletRequest request) {
        return super.getClientId(request);
    }

    /**
     * 获取客户端版本号, APP传递, WEB返回null即可
     */
    @Override
    public String getClientVersion(HttpServletRequest request) {
        return super.getClientVersion(request);
    }

    /**
     * 获取设备ID, APP传递, WEB返回null即可
     */
    @Override
    public String getDeviceId(HttpServletRequest request) {
        return super.getDeviceId(request);
    }

    /**
     * 获取产品地区
     */
    public String getCountry(HttpServletRequest request) {
        return request.getHeader("Country");
    }

    /**
     * 获取登录失效时间, 这里一般按照WEB/APP区分一下, 如果不传默认30分钟
     */
    @Override
    public long getExpireSeconds(HttpServletRequest request) {
        return super.getExpireSeconds(request);
    }

    /**
     * AuthInterceptor认证失败的处理方式, 默认返回403
     */
    @Override
    public void error(HttpServletRequest request, HttpServletResponse response) {
        super.error(request, response);
    }
}
