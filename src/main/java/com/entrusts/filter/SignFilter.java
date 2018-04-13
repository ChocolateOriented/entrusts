package com.entrusts.filter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import com.entrusts.interceptor.CommonRequestContext;
import com.entrusts.interceptor.Signature;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * 签名验证类
 */
@Order(2)
@WebFilter(filterName = "signFilter", urlPatterns = "/*")
public class SignFilter implements Filter {

    @Autowired
    private Signature signature;

    /**
     * 是否有token
     * @return  true | false
     */
    private boolean hasAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader("Access-Token");
        return StringUtils.isNotBlank(accessToken);
    }

    /**
     * 是否为web请求
     *
     * @return true | false
     */
    private boolean isWebRequest() {
        CommonRequestContext requestContext = CommonRequestContext.getInstance();
        String clientId = requestContext.getClientId();
        if (org.springframework.util.StringUtils.isEmpty(clientId)) {
            return false;
        }
        String cPlatform = clientId.substring(clientId.length() - 1, clientId.length());
        if ("3".equals(cPlatform) || "4".equals(cPlatform)) {
            return true;
        }
        return false;
    }

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//如果为web请求则直接放过, 不进行签名校验
        if (isWebRequest()) {
        	chain.doFilter(request, response);
            return;
        }

        //获取请求是否需要用户身份验证
//        Method method = ((HandlerMethod) handler).getMethod();
//        boolean needLogin = Objects.nonNull(method.getAnnotation(LoginPermission.class));
        MultiReadHttpServletRequest requestWrapper = new MultiReadHttpServletRequest((HttpServletRequest) request);
        if (!signature.validateSign(requestWrapper, hasAccessToken(requestWrapper))) {
            throw new ServletException("签名验证失败");
        }
        chain.doFilter(requestWrapper, response);
	}

	@Override
	public void destroy() {
		
	}
}