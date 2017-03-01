package com.ctrip.framework.apollo.portal.spi.xiaomi;

import com.ctrip.framework.apollo.portal.entity.bo.UserInfo;
import com.ctrip.framework.apollo.portal.spi.UserInfoHolder;
import com.xiaomi.passport.sdk.utils.STSHellper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wxp04 on 2017/2/28.
 */
public class XiaomiUserInfoHolder implements UserInfoHolder {
    private static Logger logger = LoggerFactory.getLogger(XiaomiUserInfoHolder.class);

    @Override
    public UserInfo getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            logger.info("Login failed!");
            throw new RuntimeException("Login failed!");
        } else {
            logger.info("[##XiaomiUserInfoHolder##] Principle:{}, type: {}", authentication.getPrincipal(), authentication.getPrincipal().getClass().getName());
            if("anonymousUser".equalsIgnoreCase(authentication.getPrincipal().toString())) {
                logger.info("Login failed!");
                throw new RuntimeException("Login failed!");
            }
            long userId = (long) authentication.getPrincipal();
            logger.info("[##XiaomiUserInfoHolder##] User Id:{}", userId);
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(String.valueOf(userId));

            return userInfo;
        }
    }
}
