package com.mom.winery.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum EnumWineType implements EnumClass<String> {

    NONG_XIANG("NONG_XIANG"),
    JIANG_XIANG("JIANG_XIANG");

    private final String id;

    EnumWineType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static EnumWineType fromId(String id) {
        for (EnumWineType at : EnumWineType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
