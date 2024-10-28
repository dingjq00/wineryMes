package com.mom.winery.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.OffsetDateTime;
import java.util.List;

@JmixEntity
@Table(name = "MES_SHOPFLOOR")
@Entity
public class MesShopfloor {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private Long id;

    @Column(name = "MES_SITE_CODE", length = 120)
    private String mesSiteCode;

    @Column(name = "MES_SITE_NAME", length = 120)
    private String mesSiteName;

    @Column(name = "MES_SHOPFLOOR_CODE", length = 120)
    private String mesShopfloorCode;

    @InstanceName
    @Column(name = "MES_SHOPFLOOR_NAME", length = 120)
    private String mesShopfloorName;

    @Composition
    @OneToMany(mappedBy = "mesShopfloor")
    private List<MesArea> mesAreas;

    @Composition
    @OneToMany(mappedBy = "mesShopfloor")
    private List<MesShopfloorWineTank> mesShopfloorWineTanks;

    @Composition
    @OneToMany(mappedBy = "mesShopfloor")
    private List<MesCleanWaterTank> mesCleanWaterTanks;

    @Composition
    @OneToMany(mappedBy = "mesShopfloor")
    private List<MesCirculatingWaterTank> mesCirculatingWaterTanks;

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

    public List<MesCirculatingWaterTank> getMesCirculatingWaterTanks() {
        return mesCirculatingWaterTanks;
    }

    public void setMesCirculatingWaterTanks(List<MesCirculatingWaterTank> mesCirculatingWaterTanks) {
        this.mesCirculatingWaterTanks = mesCirculatingWaterTanks;
    }

    public List<MesCleanWaterTank> getMesCleanWaterTanks() {
        return mesCleanWaterTanks;
    }

    public void setMesCleanWaterTanks(List<MesCleanWaterTank> mesCleanWaterTanks) {
        this.mesCleanWaterTanks = mesCleanWaterTanks;
    }

    public List<MesShopfloorWineTank> getMesShopfloorWineTanks() {
        return mesShopfloorWineTanks;
    }

    public void setMesShopfloorWineTanks(List<MesShopfloorWineTank> mesShopfloorWineTanks) {
        this.mesShopfloorWineTanks = mesShopfloorWineTanks;
    }

    public List<MesArea> getMesAreas() {
        return mesAreas;
    }

    public void setMesAreas(List<MesArea> mesAreas) {
        this.mesAreas = mesAreas;
    }

    public String getMesShopfloorName() {
        return mesShopfloorName;
    }

    public void setMesShopfloorName(String mesShopfloorName) {
        this.mesShopfloorName = mesShopfloorName;
    }

    public String getMesShopfloorCode() {
        return mesShopfloorCode;
    }

    public void setMesShopfloorCode(String mesShopfloorCode) {
        this.mesShopfloorCode = mesShopfloorCode;
    }

    public String getMesSiteName() {
        return mesSiteName;
    }

    public void setMesSiteName(String mesSiteName) {
        this.mesSiteName = mesSiteName;
    }

    public String getMesSiteCode() {
        return mesSiteCode;
    }

    public void setMesSiteCode(String mesSiteCode) {
        this.mesSiteCode = mesSiteCode;
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
