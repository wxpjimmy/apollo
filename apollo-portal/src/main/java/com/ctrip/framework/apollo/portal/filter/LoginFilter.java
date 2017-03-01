package com.ctrip.framework.apollo.portal.filter;

import com.xiaomi.passport.PassportException;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import sun.rmi.runtime.Log;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by wxp04 on 2017/2/28.
 */
public class LoginFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    SSOWebRequiredInterceptHandler requiredInterceptHandler = new SSOWebRequiredInterceptHandlerV2();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Init!");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("Do filter!");
        logger.info( "{}", servletRequest instanceof HttpServletRequest);
        logger.info("{}", servletResponse instanceof HttpServletResponse);

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
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String sid = "";//annotation.sid();
        float loginStore = -1.0f; //annotation.loginScore();
        String additionalParam = "";//annotation.additionalParam();

        List<NameValuePair> paramList = null;
        if (StringUtils.isNotBlank(additionalParam)) {
            PassportURIBuilder b = null;
            try {
                b = new PassportURIBuilder(StringUtils.startsWith(additionalParam, "?") ? additionalParam : "?" + additionalParam);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            if(b != null) {
                paramList = b.getQueryParams();
            }
        }

        RequestResult requestResult = null;
        try {
            requestResult = requiredInterceptHandler.checkResult((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, sid, loginStore, paramList);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (PassportException e) {
            e.printStackTrace();
        }
        if (requestResult != null && requestResult.shouldContinue()) {
            long userId = STSHellper.getUserIdFromAttribute((HttpServletRequest) servletRequest);
            logger.info("UserId : {}", userId);
            logger.info("Request uri: {}",((HttpServletRequest) servletRequest).getRequestURI());

            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userId, "");

            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authRequest);

            // Create a new session and add the security context.
            HttpSession session = ((HttpServletRequest) servletRequest).getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            logger.warn("login failed! {} ----- {}", httpServletResponse.getStatus(), httpServletResponse.getHeader("Location"));
        }
    }

    @Override
    public void destroy() {
        logger.info("destroy!");
    }
}
