package com.ctrip.framework.apollo.portal.controller;

import com.ctrip.framework.apollo.portal.entity.bo.UserInfo;
import com.ctrip.framework.apollo.portal.spi.UserInfoHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wxp04 on 2017/3/1.
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserInfoHolder userInfoHolder;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public void login(HttpServletRequest request, HttpServletResponse response) {
        logger.info("[##checkLoginStatus##] login");
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public boolean checkLoginStatus(HttpServletRequest request, HttpServletResponse response) {
        try {
            UserInfo userInfo = userInfoHolder.getUser();
            logger.info("[##checkLoginStatus##] get User Name: {}", userInfo.getUserId());
            return true;
        }catch (Exception ex) {
            logger.info("[##checkLoginStatus##] not login!");
            return false;
        }
    }
}
