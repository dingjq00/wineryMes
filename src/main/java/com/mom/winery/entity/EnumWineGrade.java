package com.mom.winery.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum EnumWineGrade implements EnumClass<String> {

    HEAD_WINE("HEAD_WINE"),
    FIRSTCLASS_WINE("FIRSTCLASS_WINE"),
    SECONDCLASS_WINE("SECONDCLASS_WINE"),
    THIRDCLASS_WINE("THIRDCLASS_WINE"),
    END_WINE("END_WINE");

    private final String id;

    EnumWineGrade(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static EnumWineGrade fromId(String id) {
        for (EnumWineGrade at : EnumWineGrade.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
