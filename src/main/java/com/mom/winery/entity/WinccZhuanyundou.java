package com.mom.winery.entity;

import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.NumberFormat;
import io.jmix.core.metamodel.annotation.Store;
import io.jmix.data.DdlGeneration;
import jakarta.persistence.*;

import java.util.Date;

@DdlGeneration(value = DdlGeneration.DbScriptGenerationMode.DISABLED)
@JmixEntity
@Store(name = "winccdata")
@Table(name = "wincc_zhuanyundou")
@Entity
public class WinccZhuanyundou {
    @NumberFormat(pattern = "############")
    @Column(name = "areaNo")
    private Integer areaNo;

    @Column(name = "comment")
    @Lob
    private String comment;

    @Column(name = "devtype")
    private Integer devtype;

    @Id
    @Column(name = "Idall", nullable = false)
    private Integer id;

    @NumberFormat(pattern = "############")
    @Column(name = "Id")
    private Integer winccId;

    @Column(name = "isDealed")
    private Integer isDealed;

    @Column(name = "starttime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date starttime;

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Integer getIsDealed() {
        return isDealed;
    }

    public void setIsDealed(Integer isDealed) {
        this.isDealed = isDealed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDevtype() {
        return devtype;
    }

    public void setDevtype(Integer devtype) {
        this.devtype = devtype;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getAreaNo() {
        return areaNo;
    }

    public void setAreaNo(Integer areaNo) {
        this.areaNo = areaNo;
    }

    public Integer getWinccId() {
        return winccId;
    }

    public void setWinccId(Integer winccId) {
        this.winccId = winccId;
    }
}
