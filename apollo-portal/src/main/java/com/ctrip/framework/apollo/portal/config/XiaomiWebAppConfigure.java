package com.ctrip.framework.apollo.portal.config;

import com.ctrip.framework.apollo.portal.interceptor.LoginInterceptor;
import com.ctrip.framework.apollo.portal.interceptor.LoginInterceptorV2;
import com.ctrip.framework.apollo.portal.interceptor.RedirectInterceptorV2;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by eagle on 17/2/21.
 */
@Configuration
public class XiaomiWebAppConfigure extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptorV2()).addPathPatterns("/**").excludePathPatterns("/sts");
        //registry.addInterceptor(new RedirectInterceptor()).addPathPatterns("/**");
        //registry.addInterceptor(new RedirectInterceptorV2()).addPathPatterns("/user");
        super.addInterceptors(registry);
    }
}
