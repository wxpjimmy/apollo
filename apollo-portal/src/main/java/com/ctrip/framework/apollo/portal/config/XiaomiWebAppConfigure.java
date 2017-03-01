package com.ctrip.framework.apollo.portal.config;

import com.ctrip.framework.apollo.portal.filter.LoginFilter;
import com.ctrip.framework.apollo.portal.interceptor.LoginInterceptor;
import com.ctrip.framework.apollo.portal.interceptor.LoginInterceptorV2;
import com.ctrip.framework.apollo.portal.interceptor.RedirectInterceptorV2;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eagle on 17/2/21.
 */
@Configuration
public class XiaomiWebAppConfigure extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptorV2()).addPathPatterns("/**").excludePathPatterns("/sts").excludePathPatterns("/login/status");
        //registry.addInterceptor(new RedirectInterceptor()).addPathPatterns("/**");
        //registry.addInterceptor(new RedirectInterceptorV2()).addPathPatterns("/user");
        super.addInterceptors(registry);
    }

//    @Bean
//    public FilterRegistrationBean filterRegistrationBean() {
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        LoginFilter actionFilter = new LoginFilter();
//        registrationBean.setFilter(actionFilter);
//        List<String> urlPatterns = new ArrayList<String>();
//        urlPatterns.add("/apps/*");
//        urlPatterns.add("/consumers/*");
//        urlPatterns.add("/envs/*");
//        urlPatterns.add("/envs");
//        urlPatterns.add("/favorites/*");
//        urlPatterns.add("/appnamespaces/*");
//        urlPatterns.add("/organizations");
//        urlPatterns.add("/permissions/*");
//        urlPatterns.add("/server/*");
//        urlPatterns.add("/sso_heartbeat");
//        urlPatterns.add("/user");
//        urlPatterns.add("/user/*");
//        urlPatterns.add("/users");
//        urlPatterns.add("/users/*");
//        registrationBean.setUrlPatterns(urlPatterns);
//        //egistrationBean.registrationBean
//        return registrationBean;
//    }
}
