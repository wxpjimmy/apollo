package com.ctrip.framework.apollo.portal.spi.xiaomi;

import com.ctrip.framework.apollo.portal.entity.bo.UserInfo;
import com.ctrip.framework.apollo.portal.repository.UserRoleRepository;
import com.ctrip.framework.apollo.portal.spi.UserInfoHolder;
import com.ctrip.framework.apollo.portal.spi.UserService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * Created by eagle on 17/3/1.
 */
public class XiaomiUserService implements UserService {

    @Autowired
    private UserInfoHolder userInfoHolder;

    @Autowired
    private UserRoleRepository userRoleRepository;


    @Override
    public List<UserInfo> searchUsers(String keyword, int offset, int limit) {
        return Arrays.asList(assembleDefaultUser(), userInfoHolder.getUser());
    }

    @Override
    public UserInfo findByUserId(String userId) {
        if (userId.equals("apollo")) {
            return assembleDefaultUser();
        }
        UserInfo userInfo = userInfoHolder.getUser();
        if(userId.equalsIgnoreCase(userInfo.getUserId())) {
            return userInfo;
        }
        return null;
    }

    @Override
    public List<UserInfo> findByUserIds(List<String> userIds) {
        if (userIds.contains("apollo")) {
            return Lists.newArrayList(assembleDefaultUser());
        }
        return null;
    }

    private UserInfo assembleDefaultUser() {
        UserInfo defaultUser = new UserInfo();
        defaultUser.setUserId("apollo");
        defaultUser.setName("apollo");
        defaultUser.setEmail("apollo@acme.com");

        return defaultUser;
    }
}
