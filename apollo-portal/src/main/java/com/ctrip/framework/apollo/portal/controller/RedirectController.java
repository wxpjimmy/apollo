package com.ctrip.framework.apollo.portal.controller;

import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by eagle on 17/2/23.
 */
@RestController
public class RedirectController {
    private static Logger logger = LoggerFactory.getLogger(RedirectController.class);

    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public RedirectView redirect(HttpServletResponse response) throws IOException {
        logger.info("Calling redirect method!");
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://www.yahoo.com");
        return redirectView;
//        response.setStatus(HttpResponseStatus.FOUND.getCode());
//        response.setHeader("Location", "http://account.preview.n.xiaomi.net/pass/serviceLogin?callback=http%3A%2F%2Fmax.ad.xiaomi.srv%2FslfService%2Fmain.html%2Fsts%3Fsign%3Dbrep4plo4N%252BzqEhDNrjkBO4lSbM%253D%26followup%3Dhttp%253A%252F%252Flocalhost%253A8070%252Fuser&sid=max_dsp");
    }
}
