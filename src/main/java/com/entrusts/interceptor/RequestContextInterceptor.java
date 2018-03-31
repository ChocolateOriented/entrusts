package com.entrusts.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.entrusts.config.CustomRequestInfoFetcher;
import com.entrusts.interceptor.CommonRequestContext.CommonRequestContextBuilder;

@Component
public class RequestContextInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private CustomRequestInfoFetcher customRequestInfoFetcher;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		CommonRequestContextBuilder builder = new CommonRequestContextBuilder();
		builder.accessToken(customRequestInfoFetcher.getAccessToken(request));
		builder.clientId(customRequestInfoFetcher.getClientId(request));
		builder.clientVersion(customRequestInfoFetcher.getClientVersion(request));
		builder.country(customRequestInfoFetcher.getCountry(request));
		builder.deviceId(customRequestInfoFetcher.getDeviceId(request));
		builder.build();
		
		return true;
	}

	
}
