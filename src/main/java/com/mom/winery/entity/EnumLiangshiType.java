package com.mom.winery.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum EnumLiangshiType implements EnumClass<Integer> {

    CUO_LIANG(0),
    XI_LIANG(1);

    private final Integer id;

    EnumLiangshiType(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static EnumLiangshiType fromId(Integer id) {
        for (EnumLiangshiType at : EnumLiangshiType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
