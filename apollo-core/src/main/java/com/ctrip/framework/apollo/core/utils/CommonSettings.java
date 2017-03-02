package com.ctrip.framework.apollo.core.utils;

import com.ctrip.framework.foundation.internals.NetworkInterfaceManager;
import com.ctrip.framework.foundation.internals.Utils;
import com.ctrip.framework.foundation.internals.provider.DefaultApplicationProvider;
import com.xiaomi.miliao.zookeeper.EnvironmentType;
import com.xiaomi.miliao.zookeeper.ZKFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Created by eagle on 17/2/26.
 */
public class CommonSettings {
    private static Logger logger = LoggerFactory.getLogger(CommonSettings.class);

    public static final String APP_PROPERTIES_CLASSPATH = "/META-INF/app.properties";
    private static Properties m_appProperties = new Properties();
    private static String m_appId;

    static {
        try {
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(APP_PROPERTIES_CLASSPATH);
            if (in == null) {
                in = DefaultApplicationProvider.class.getResourceAsStream(APP_PROPERTIES_CLASSPATH);
            }

            if (in == null) {
                logger.info("Cannot create InputStream by classpath: " + APP_PROPERTIES_CLASSPATH + ".");
            }
            initialize(in);
        } catch (Exception ex) {
            logger.error("Initial Property failed!", ex);
        }
    }

    private static void initialize(InputStream in) {
        try {
            if (in != null) {
                try {
                    m_appProperties.load(new InputStreamReader(in, StandardCharsets.UTF_8));
                } finally {
                    in.close();
                }
            }

            initAppId();
        } catch (Exception ex) {
            logger.error("Initial Property failed!", ex);
        }
    }

    private static void initAppId() {
        // 1. Try to get app id from app.properties.
        m_appId = m_appProperties.getProperty("app.id");

        if (!Utils.isBlank(m_appId)) {
            m_appId = m_appId.trim();

            logger.info("App ID is set to " + m_appId + " by app.id property in app.properties InputStream.");

        } else {
            logger.info("app.id is not available from properties InputStream."
                    + " It is set to null");
            m_appId = null;
        }
    }

    public static String getAppId() {
        return m_appId;
    }

    public static String getEnvType() {
        String env = System.getProperty("apollo_profile");
        if(StringUtils.isBlank(env)) {
            env = "pro";
        }
        logger.info("Env: {}", env);
        return env;
    }

    public static String getDataCenter() {
        EnvironmentType type = ZKFacade.getZKSettings().getEnvironmentType();
        return type.name().toUpperCase();
    }

    public static String getHostAddress() {
        return NetworkInterfaceManager.INSTANCE.getLocalHostAddress();
    }
}
