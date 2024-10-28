package com.mom.winery.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum EnumProcessMainPhase implements EnumClass<Integer> {

    MONILIANG(5),
    HANGCHE(6),
    ZHUANYUNDOU(7),
    RUNLIANGDOU(8),
    GEILIAOJI(9),
    ZENGDEVICE(10),
    TANLIANGJI(11),
    ZENGDATA(12);

    private final Integer id;

    EnumProcessMainPhase(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static EnumProcessMainPhase fromId(Integer id) {
        for (EnumProcessMainPhase at : EnumProcessMainPhase.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
