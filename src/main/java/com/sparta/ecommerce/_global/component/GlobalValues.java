package com.sparta.ecommerce._global.component;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class GlobalValues {
    private static final int MAIN_VERSION = 0;
    private static final int DEV_VERSION = 0;
    private static final int STAGE_VERSION = 0;

    public static String getBEVersion() {
        return MAIN_VERSION + "." + DEV_VERSION + "." + STAGE_VERSION;
    }

    @Getter
    private static String PREFIX ;

    @Value("${STAGE}")
    public void setPublicPrefix(String stage) {
        GlobalValues.PREFIX = "public/" + stage + "/";
    }
}
