package com.entrusts.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import com.entrusts.interceptor.CommonRequestContext;
import com.entrusts.interceptor.CommonRequestContext.CommonRequestContextBuilder;

@Order(1)
@WebFilter(filterName = "requestContextFilter", urlPatterns = "/*")
public class RequestContextFilter implements Filter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request;
		CommonRequestContextBuilder builder = new CommonRequestContextBuilder();
		builder.accessToken(httpReq.getHeader("Access-Token"));
		builder.clientId(httpReq.getHeader("Client-Id"));
		builder.clientVersion(httpReq.getHeader("Client-Version"));
		builder.country(httpReq.getHeader("Country"));
		builder.deviceId(httpReq.getHeader("Device-Id"));
		builder.build();
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		
	}

	
}
