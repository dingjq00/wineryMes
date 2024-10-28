package com.mom.winery.entity;

import io.jmix.core.DeletePolicy;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDeleteInverse;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.NumberFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.OffsetDateTime;
import java.util.List;

@JmixEntity
@Table(name = "MES_AREA", indexes = {
        @Index(name = "IDX_MES_AREA_MES_SHOPFLOOR", columnList = "MES_SHOPFLOOR_ID")
})
@Entity
public class MesArea {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private Long id;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @JoinColumn(name = "MES_SHOPFLOOR_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private MesShopfloor mesShopfloor;

    @NumberFormat(pattern = "######")
    @NotNull
    @Column(name = "AREA_CODE", nullable = false)
    private Integer areaCode;

    @InstanceName
    @NotNull
    @Column(name = "AREA_NAME", nullable = false, length = 120)
    private String areaName;

    @Column(name = "WINE_TYPE")
    private String wineType;

    @Composition
    @OneToMany(mappedBy = "mesArea")
    private List<MesCell> mesCells;

    @Composition
    @OneToMany(mappedBy = "mesArea")
    private List<MesZhuanyundou> mesZhuanyundous;

    @Composition
    @OneToMany(mappedBy = "mesArea")
    private List<MesRunliangdou> mesRunliangdous;

    @Composition
    @OneToMany(mappedBy = "mesArea")
    private List<MesZhuangzengDvice> mesZhuangzengDevices;

    @Composition
    @OneToMany(mappedBy = "mesArea")
    private List<MesZengguo> mesZengguos;

    @Composition
    @OneToMany(mappedBy = "mesArea")
    private List<MesTanliangji> mesTanliangjis;

    @Composition
    @OneToMany(mappedBy = "mesArea")
    private List<MesAreaWineTank> mesAreaWineTanks;

    @Composition
    @OneToMany(mappedBy = "mesArea")
    private List<MesRunliangWaterTank> mesRunliangWaterTanks;

    @Composition
    @OneToMany(mappedBy = "mesArea")
    private List<MesLiangshuiTank> mesLiangshuiTanks;

    @Composition
    @OneToMany(mappedBy = "mesArea")
    private List<MesRecyclingBottomWaterTank> mesRecyclingBottomWaterTanks;

    @Composition
    @OneToMany(mappedBy = "mesArea")
    private List<MesDiuZaoWeiJiuTank> mesDiuZaoWeiJiuTanks;

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

    public List<MesDiuZaoWeiJiuTank> getMesDiuZaoWeiJiuTanks() {
        return mesDiuZaoWeiJiuTanks;
    }

    public void setMesDiuZaoWeiJiuTanks(List<MesDiuZaoWeiJiuTank> mesDiuZaoWeiJiuTanks) {
        this.mesDiuZaoWeiJiuTanks = mesDiuZaoWeiJiuTanks;
    }

    public List<MesRecyclingBottomWaterTank> getMesRecyclingBottomWaterTanks() {
        return mesRecyclingBottomWaterTanks;
    }

    public void setMesRecyclingBottomWaterTanks(List<MesRecyclingBottomWaterTank> mesRecyclingBottomWaterTanks) {
        this.mesRecyclingBottomWaterTanks = mesRecyclingBottomWaterTanks;
    }

    public List<MesLiangshuiTank> getMesLiangshuiTanks() {
        return mesLiangshuiTanks;
    }

    public void setMesLiangshuiTanks(List<MesLiangshuiTank> mesLiangshuiTanks) {
        this.mesLiangshuiTanks = mesLiangshuiTanks;
    }

    public List<MesRunliangWaterTank> getMesRunliangWaterTanks() {
        return mesRunliangWaterTanks;
    }

    public void setMesRunliangWaterTanks(List<MesRunliangWaterTank> mesRunliangWaterTanks) {
        this.mesRunliangWaterTanks = mesRunliangWaterTanks;
    }

    public MesShopfloor getMesShopfloor() {
        return mesShopfloor;
    }

    public void setMesShopfloor(MesShopfloor mesShopfloor) {
        this.mesShopfloor = mesShopfloor;
    }

    public EnumWineType getWineType() {
        return wineType == null ? null : EnumWineType.fromId(wineType);
    }

    public void setWineType(EnumWineType wineType) {
        this.wineType = wineType == null ? null : wineType.getId();
    }

    public List<MesAreaWineTank> getMesAreaWineTanks() {
        return mesAreaWineTanks;
    }

    public void setMesAreaWineTanks(List<MesAreaWineTank> mesAreaWineTanks) {
        this.mesAreaWineTanks = mesAreaWineTanks;
    }

    public List<MesTanliangji> getMesTanliangjis() {
        return mesTanliangjis;
    }

    public void setMesTanliangjis(List<MesTanliangji> mesTanliangjis) {
        this.mesTanliangjis = mesTanliangjis;
    }

    public List<MesZengguo> getMesZengguos() {
        return mesZengguos;
    }

    public void setMesZengguos(List<MesZengguo> mesZengguos) {
        this.mesZengguos = mesZengguos;
    }

    public List<MesZhuangzengDvice> getMesZhuangzengDevices() {
        return mesZhuangzengDevices;
    }

    public void setMesZhuangzengDevices(List<MesZhuangzengDvice> mesZhuangzengDevices) {
        this.mesZhuangzengDevices = mesZhuangzengDevices;
    }

    public List<MesRunliangdou> getMesRunliangdous() {
        return mesRunliangdous;
    }

    public void setMesRunliangdous(List<MesRunliangdou> mesRunliangdous) {
        this.mesRunliangdous = mesRunliangdous;
    }

    public List<MesZhuanyundou> getMesZhuanyundous() {
        return mesZhuanyundous;
    }

    public void setMesZhuanyundous(List<MesZhuanyundou> mesZhuanyundous) {
        this.mesZhuanyundous = mesZhuanyundous;
    }

    public List<MesCell> getMesCells() {
        return mesCells;
    }

    public void setMesCells(List<MesCell> mesCells) {
        this.mesCells = mesCells;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
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
