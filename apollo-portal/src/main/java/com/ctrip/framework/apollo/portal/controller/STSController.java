package com.ctrip.framework.apollo.portal.controller;

import com.xiaomi.passport.PassportOpFailureException;
import com.xiaomi.passport.sdk.common.SDKErrorCode;
import com.xiaomi.passport.sdk.service.STSControllerBase;
import com.xiaomi.passport.sdk.utils.ResultHelper;
import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shade.org.apache.commons.httpclient.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ExecutionException;

/**
 * Created by wxp04 on 2017/2/23.
 */
@RestController
public class STSController extends STSControllerBase {
    private static Logger logger = LoggerFactory.getLogger(STSController.class);

    public STSController() {

    }

    @RequestMapping(value = "/sts", method = RequestMethod.GET)
    public String sts(@RequestParam(value = "auth", required = false) String authToken,
                      @RequestParam(value = "followup", required = false) String followup,
                      @RequestParam(value = "clientSign", required = false) String clientSign,
                      @RequestParam(value = "sign", required = false) String sign,
                      @RequestParam(value = "tokenNeedEncrypt", required = false) boolean tokenNeedEcrypt,
                      HttpServletRequest request, HttpServletResponse response) {

        try {
            logger.info("Processing STS......");
            String result = stsRose(request, response, authToken, followup, clientSign, sign, tokenNeedEcrypt);
            logger.info("Sts result: {}", result);
            if(result.startsWith("r:")) {
                response.sendRedirect();
            }
            return result;
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            return ResultHelper.genStatusResult(HttpStatus.SC_BAD_REQUEST);
        }
    }

    protected String stsRose(HttpServletRequest request, HttpServletResponse response,
                             String authToken, String followup, String clientSign,
                             String sign, boolean tokenNeedEcrypt)
            throws PassportOpFailureException, ExecutionException, InterruptedException {
        StsResult result = super.sts(request, response, authToken, followup, clientSign, sign, tokenNeedEcrypt).get();
        if (result.sdkErrorCode != SDKErrorCode.Success) {
            return ResultHelper.genStatusResult(result.httpStatus);
        } else {
            if (result.followup != null) {
                return ResultHelper.genRedirectResult(result.followup);
            } else {
                return ResultHelper.genSuccessResult(null);
            }
        }
    }
}
