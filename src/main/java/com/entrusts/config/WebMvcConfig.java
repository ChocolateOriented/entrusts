package com.entrusts.config;

import com.entrusts.interceptor.RequestContextInterceptor;
import com.entrusts.interceptor.SignInterceptor;
import com.mo9.nest.auth.AuthInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * Created by sxu on 2018/2/28.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Autowired
    private RequestContextInterceptor requestContextInterceptor;

    @Autowired
    private SignInterceptor signInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(authInterceptor).addPathPatterns("/**");
        registry.addInterceptor(requestContextInterceptor).addPathPatterns("/**");
        registry.addInterceptor(signInterceptor).addPathPatterns("/**");
    }

}
