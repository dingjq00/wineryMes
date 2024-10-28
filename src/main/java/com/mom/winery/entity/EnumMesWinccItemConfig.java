package com.mom.winery.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum EnumMesWinccItemConfig implements EnumClass<String> {

    RLD_LOCATION("RLD_LOCATION"),
    HC_STEP("HC_STEP"),
    ZYD_LOCATION("ZYD_LOCATION"),
    ZG_STEP("ZG_STEP"),
    EMPTY_FULL("EMPTY_FULL"),
    DIUZAO_LIANGZAO("DIUZAO_LIANGZAO"),
    NOTQUALIFIED_QUALIFIED("NOTQUALIFIED_QUALIFIED"),
    LS_TYPE("LS_TYPE");

    private final String id;

    EnumMesWinccItemConfig(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static EnumMesWinccItemConfig fromId(String id) {
        for (EnumMesWinccItemConfig at : EnumMesWinccItemConfig.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
