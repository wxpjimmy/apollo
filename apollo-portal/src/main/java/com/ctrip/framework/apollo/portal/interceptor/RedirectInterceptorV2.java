package com.ctrip.framework.apollo.portal.interceptor;

import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by eagle on 17/2/23.
 */
public class RedirectInterceptorV2 implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(RedirectInterceptorV2.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("Test Redirect[2]!");
        response.setStatus(HttpResponseStatus.FOUND.getCode());
        response.setHeader("Location", "http://account.preview.n.xiaomi.net/pass/serviceLogin?callback=http%3A%2F%2Fmax.ad.xiaomi.srv%2FslfService%2Fmain.html%2Fsts%3Fsign%3Dbrep4plo4N%252BzqEhDNrjkBO4lSbM%253D%26followup%3Dhttp%253A%252F%252Flocalhost%253A8070%252Fuser&sid=max_dsp");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        logger.info("Test Redirect[2] -- Response status: {}, Location: {}", response.getStatus(), response.getHeader("Location"));
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("After completion!");
    }
}
