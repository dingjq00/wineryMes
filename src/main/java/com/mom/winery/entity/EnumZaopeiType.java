package com.mom.winery.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum EnumZaopeiType implements EnumClass<Integer> {

    MIAN_ZAO(1),
    ZHONGSHANG_ZAO(2),
    XIACENG_ZAO(3),
    SHUANGLUN_ZAO(4),
    HONG_ZAO(5),
    SHENGCHI_ZAO(6),
    UNKNOW(0);

    private final Integer id;

    EnumZaopeiType(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static EnumZaopeiType fromId(Integer id) {
        for (EnumZaopeiType at : EnumZaopeiType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
