package com.ctrip.framework.apollo.portal.config;

import com.ctrip.framework.apollo.portal.interceptor.LoginInterceptorV2;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
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
        //registry.addInterceptor(new LoginInterceptorV2()).addPathPatterns("/**").excludePathPatterns("/sts").excludePathPatterns("/login/status");
        //registry.addInterceptor(new RedirectInterceptor()).addPathPatterns("/**");
        //registry.addInterceptor(new RedirectInterceptorV2()).addPathPatterns("/user");
        super.addInterceptors(registry);
    }

    @Bean
    public FilterRegistrationBean casAuthFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        AuthenticationFilter actionFilter = new AuthenticationFilter();
        registrationBean.setFilter(actionFilter);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);
        registrationBean.addInitParameter("casServerLoginUrl", "https://casdev.mioffice.cn/login");
        registrationBean.addInitParameter("serverName", "http://localhost:8070");
        //egistrationBean.registrationBean
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean casTokenValidateFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        Cas20ProxyReceivingTicketValidationFilter actionFilter = new Cas20ProxyReceivingTicketValidationFilter();
        registrationBean.setFilter(actionFilter);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);
        registrationBean.addInitParameter("casServerUrlPrefix", "https://casdev.mioffice.cn");
        registrationBean.addInitParameter("serverName", "http://localhost:8070");
        registrationBean.addInitParameter("redirectAfterValidation", "true");
        //egistrationBean.registrationBean
        return registrationBean;
    }
}
