package com.ctrip.framework.apollo.portal.spi.xiaomi;

import com.ctrip.framework.apollo.portal.spi.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by wxp04 on 2017/3/1.
 */
public class XiaomiLogoutHandler implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        try {
            response.sendRedirect("https://casdev.mioffice.cn/logout?service=http://localhost:8080");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
