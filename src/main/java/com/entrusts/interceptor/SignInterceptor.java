package com.entrusts.interceptor;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 签名验证类
 */
public class SignInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private Signature signature;

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//            throws Exception {
//
//        //如果为web请求则直接放过, 不进行签名校验
//        if (isWebRequest()) {
//            return true;
//        }
//
//        //获取请求是否需要用户身份验证
////        Method method = ((HandlerMethod) handler).getMethod();
////        boolean needLogin = Objects.nonNull(method.getAnnotation(LoginPermission.class));
//        if (!signature.validateSign(request, hasAccessToken(request))) {
//            throw new WebApiException(
//                    ApplicationErrorMessage.SIGNATURE_ERROR.getCode(),
//                    ApplicationErrorMessage.SIGNATURE_ERROR.getDescription()
//            );
//        }
//        return true;
//    }

//    /**
//     * 是否有token
//     * @return  true | false
//     */
//    private boolean hasAccessToken(HttpServletRequest request) {
//        String accessToken = request.getHeader("Access-Token");
//        return StringUtils.isNotBlank(accessToken);
//    }

//    /**
//     * 是否为web请求
//     *
//     * @return true | false
//     */
//    private boolean isWebRequest() {
//    	CommonRequestContext requestContext = CommonRequestContext.getInstance();
//        return Objects.equals(Integer.valueOf(requestContext.getClientId()), ClientIdEnum.WEB.getValue());
//    }
}