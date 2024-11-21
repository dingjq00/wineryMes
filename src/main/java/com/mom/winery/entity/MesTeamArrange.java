package com.mom.winery.entity;

import io.jmix.core.DeletePolicy;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDeleteInverse;
import io.jmix.core.metamodel.annotation.Comment;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.OffsetDateTime;

@JmixEntity
@Table(name = "MES_TEAM_ARRANGE", indexes = {
        @Index(name = "IDX_MES_TEAM_ARRANGE_DAY_SHIFT_TEAM", columnList = "DAY_SHIFT_TEAM_ID"),
        @Index(name = "IDX_MES_TEAM_ARRANGE_SHORT_NIGHT_TEAM", columnList = "SHORT_NIGHT_TEAM_ID"),
        @Index(name = "IDX_MES_TEAM_ARRANGE_LONG_NIGHT_TEAM", columnList = "LONG_NIGHT_TEAM_ID"),
        @Index(name = "IDX_MES_TEAM_ARRANGE_MES_SHOPFLOOR", columnList = "MES_SHOPFLOOR_ID")
})
@Entity
public class MesTeamArrange {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private Long id;

    @Comment("周期开始时间")
    @Column(name = "START_DATE_STR", length = 50)
    private String periodStartDateStr;

    @Comment("周期结束时间")
    @Column(name = "PERIOD_END_DATE_STR", length = 50)
    private String periodEndDateStr;

    @Comment("白班班组")
    @JoinColumn(name = "DAY_SHIFT_TEAM_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesShiftTeam dayShiftTeam;

    @Comment("小夜班班组")
    @JoinColumn(name = "SHORT_NIGHT_TEAM_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesShiftTeam shortNightTeam;

    @Comment("长夜班班组")
    @JoinColumn(name = "LONG_NIGHT_TEAM_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MesShiftTeam longNightTeam;

    @Comment("是否生效")
    @Column(name = "IS_ACTIVE")
    private Boolean isActive;

    @CreatedBy
    @Column(name = "CREATED_BY")
    private String createdBy;

    @CreatedDate
    @Column(name = "CREATED_DATE")
    private OffsetDateTime createdDate;

    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY")
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    private OffsetDateTime lastModifiedDate;

    @Comment("生产车间")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @JoinColumn(name = "MES_SHOPFLOOR_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private MesShopfloor mesShopfloor;

    public void setShortNightTeam(MesShiftTeam shortNightTeam) {
        this.shortNightTeam = shortNightTeam;
    }

    public MesShiftTeam getShortNightTeam() {
        return shortNightTeam;
    }

    public MesShopfloor getMesShopfloor() {
        return mesShopfloor;
    }

    public void setMesShopfloor(MesShopfloor mesShopfloor) {
        this.mesShopfloor = mesShopfloor;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public MesShiftTeam getLongNightTeam() {
        return longNightTeam;
    }

    public void setLongNightTeam(MesShiftTeam longNightTeam) {
        this.longNightTeam = longNightTeam;
    }

    public MesShiftTeam getDayShiftTeam() {
        return dayShiftTeam;
    }

    public void setDayShiftTeam(MesShiftTeam dayShiftTeam) {
        this.dayShiftTeam = dayShiftTeam;
    }

    public String getPeriodEndDateStr() {
        return periodEndDateStr;
    }

    public void setPeriodEndDateStr(String periodEndDateStr) {
        this.periodEndDateStr = periodEndDateStr;
    }

    public String getPeriodStartDateStr() {
        return periodStartDateStr;
    }

    public void setPeriodStartDateStr(String startDateStr) {
        this.periodStartDateStr = startDateStr;
    }

    public OffsetDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(OffsetDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(OffsetDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
