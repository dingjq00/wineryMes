package com.mom.winery.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum EnumZengguoMainPhase implements EnumClass<Integer> {

    DIGUOSHUI_ADD(10),
    SHANGZENG(20),
    LIUJIU(30),
    ZHENGZHU_CHONGSUAN(40),
    POST_DEAL(50),
    OTHERS(60),
    IGNORE(70);

    private final Integer id;

    EnumZengguoMainPhase(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static EnumZengguoMainPhase fromId(Integer id) {
        for (EnumZengguoMainPhase at : EnumZengguoMainPhase.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
