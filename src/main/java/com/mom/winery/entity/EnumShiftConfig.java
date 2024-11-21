package com.mom.winery.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum EnumShiftConfig implements EnumClass<String> {

    DAY_SHIFT("DAY_SHIFT"),
    LONG_NIGHT_SHIFT("LONG_NIGHT_SHIFT"),
    SHORT_NIGHT_SHIFT("SHORT_NIGHT_SHIFT");

    private final String id;

    EnumShiftConfig(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static EnumShiftConfig fromId(String id) {
        for (EnumShiftConfig at : EnumShiftConfig.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
