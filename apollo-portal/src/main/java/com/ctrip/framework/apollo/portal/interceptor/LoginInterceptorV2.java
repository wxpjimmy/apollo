package com.ctrip.framework.apollo.portal.interceptor;

import com.ctrip.framework.apollo.portal.annotation.SSOWebRequired;
import com.xiaomi.passport.sdk.common.SDKConstants;
import com.xiaomi.passport.sdk.handler.SSORequiredHandler;
import com.xiaomi.passport.sdk.handler.SSORequiredHandlerV2;
import com.xiaomi.passport.sdk.http.utils.RequestResult;
import com.xiaomi.passport.sdk.interceptors.handler.SSOWebRequiredInterceptHandler;
import com.xiaomi.passport.sdk.interceptors.handler.SSOWebRequiredInterceptHandlerV2;
import com.xiaomi.passport.sdk.url.utils.PassportURIBuilder;
import com.xiaomi.passport.sdk.utils.PaServiceConfiguration;
import com.xiaomi.passport.sdk.utils.STSHellper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by wangxiaopeng on 17/2/21.
 */
public class LoginInterceptorV2 implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(LoginInterceptorV2.class);

    SSOWebRequiredInterceptHandler requiredInterceptHandler = new SSOWebRequiredInterceptHandlerV2();

    private SSORequiredHandler ssoRequiredHandler = new SSORequiredHandlerV2(SDKConstants.COOKIE_KEY_SERVICE_TOKEN, true);

    public LoginInterceptorV2() {

    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        logger.info("[##TEST##] Pre Interceptor process");
        //logger.info(handler.getClass().getName());

        //logger.info("Found marked annotation!");
//        logger.info("HttpOnly Token: {}", PaServiceConfiguration.getInstance().isHttpOnlyToken());
//        logger.info("SdkDomain: {}", PaServiceConfiguration.getInstance().getSdkCookieDomain());
//        logger.info("ServiceUrl: {}", PaServiceConfiguration.getInstance().getServiceUrl());
//        logger.info("Sid: {}", PaServiceConfiguration.getInstance().getServiceId());
//        logger.info("PaServers: {}", PaServiceConfiguration.getInstance().getPaServers());


        Validate.isTrue(PaServiceConfiguration.getInstance().isHttpOnlyToken(), "Service token must be http only cookie");

        Validate.isTrue(StringUtils.isNotBlank(PaServiceConfiguration.getInstance().getSdkCookieDomain()), "Service cookie domain is null");

        //SSOWebRequired annotation = .getDeclaringClass().getAnnotation(SSOWebRequired.class);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || "anonymousUser".equalsIgnoreCase(authentication.getPrincipal().toString())) {
            logger.info("No Login info!");
            String sid = "";//annotation.sid();
            float loginStore = -1.0f; //annotation.loginScore();
            String additionalParam = "";//annotation.additionalParam();

            List<NameValuePair> paramList = null;
            if (StringUtils.isNotBlank(additionalParam)) {
                PassportURIBuilder b = new PassportURIBuilder(StringUtils.startsWith(additionalParam, "?") ? additionalParam : "?" + additionalParam);
                paramList = b.getQueryParams();
            }

            RequestResult requestResult = requiredInterceptHandler.checkResult(httpServletRequest, httpServletResponse, sid, loginStore, paramList);
            if (requestResult.shouldContinue()) {
                long userId = STSHellper.getUserIdFromAttribute(httpServletRequest);
                logger.info("UserId : {}", userId);
                logger.info("Request uri: {}", httpServletRequest.getRequestURI());

                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userId, "");

                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authRequest);

                // Create a new session and add the security context.
                HttpSession session = httpServletRequest.getSession(true);
                session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
                //httpServletResponse.sendRedirect("/");

                return true;
            }
        } else {
            return true;
        }

        logger.warn("login failed! {} ----- {}", httpServletResponse.getStatus(), httpServletResponse.getHeader("Location"));
        return false;

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView)
            throws Exception {
        logger.info("[##TEST##] post Interceptor process");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e)
            throws Exception {
        logger.info("[##TEST##] after completion process");
    }
}
