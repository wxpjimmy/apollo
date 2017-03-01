package com.ctrip.framework.apollo.portal.spi.defaultimpl;

import com.ctrip.framework.apollo.portal.spi.UserInfoHolder;
import com.ctrip.framework.apollo.portal.entity.bo.UserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 不是ctrip的公司默认提供一个假用户
 */
public class DefaultUserInfoHolder implements UserInfoHolder {


  public DefaultUserInfoHolder() {

  }

  @Override
  public UserInfo getUser() {
    UserInfo userInfo = new UserInfo();
    userInfo.setUserId("apollo");
    return userInfo;
  }
}
