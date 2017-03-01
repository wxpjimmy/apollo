package com.ctrip.framework.apollo.portal.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by eagle on 17/2/23.
 */
public class RedirectInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(RedirectInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("Test Redirect[1]!");
        //response.sendRedirect(request.getContextPath() + "/redirect");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        logger.info("Test Redirect[1] -- Response status: {}, Location: {}", response.getStatus(), response.getHeader("Location"));
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
