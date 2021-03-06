package com.ctrip.framework.apollo.portal.controller;

import com.ctrip.framework.apollo.portal.annotation.SSOWebRequired;
import com.ctrip.framework.apollo.portal.spi.LogoutHandler;
import com.ctrip.framework.apollo.portal.spi.UserInfoHolder;
import com.ctrip.framework.apollo.portal.entity.bo.UserInfo;
import com.ctrip.framework.apollo.portal.spi.UserService;


import com.xiaomi.passport.sdk.utils.STSHellper;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@SSOWebRequired
public class UserInfoController {
  private static Logger logger = LoggerFactory.getLogger(UserInfoController.class);
  @Autowired
  private UserInfoHolder userInfoHolder;
  @Autowired
  private LogoutHandler logoutHandler;

  @Autowired
  private UserService userService;

  @RequestMapping(value = "/user", method = RequestMethod.GET)
  public UserInfo getCurrentUserName(HttpServletRequest request) {
    logger.info("[##TEST##] get User Name");
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || "anonymousUser".equalsIgnoreCase(authentication.getPrincipal().toString())) {
      Assertion assertion = (Assertion) request.getSession().getAttribute("_const_cas_assertion_");
      String username = assertion.getPrincipal().getName();

      UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, "", null);
      SecurityContext securityContext = SecurityContextHolder.getContext();
      securityContext.setAuthentication(authRequest);

    }
    return userInfoHolder.getUser();
  }

  @RequestMapping(value = "/user/logout", method = RequestMethod.GET)
  public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
    logoutHandler.logout(request, response);
  }

  @RequestMapping(value = "/users", method = RequestMethod.GET)
  public List<UserInfo> searchUsersByKeyword(@RequestParam(value = "keyword") String keyword,
                                             @RequestParam(value = "offset", defaultValue = "0") int offset,
                                             @RequestParam(value = "limit", defaultValue = "10") int limit) {
    return userService.searchUsers(keyword, offset, limit);
  }

  @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
  public UserInfo getUserByUserId(@PathVariable String userId) {
    return userService.findByUserId(userId);
  }
}
