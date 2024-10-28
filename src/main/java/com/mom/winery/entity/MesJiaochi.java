package com.mom.winery.entity;

import io.jmix.core.DeletePolicy;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.OnDeleteInverse;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.OffsetDateTime;

@JmixEntity
@Table(name = "MES_JIAOCHI", indexes = {
        @Index(name = "IDX_MES_JIAOCHI_MES_CELL", columnList = "MES_CELL_ID")
})
@Entity
public class MesJiaochi {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private Long id;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @JoinColumn(name = "MES_CELL_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private MesCell mesCell;

    @InstanceName
    @Column(name = "JIAOCHI_CODE", length = 120)
    private String jiaochiCode;

    @Column(name = "JIAOCHI_NO")
    private Integer jiaochiNo;

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

    public Integer getJiaochiNo() {
        return jiaochiNo;
    }

    public void setJiaochiNo(Integer jiaochiNo) {
        this.jiaochiNo = jiaochiNo;
    }

    public MesCell getMesCell() {
        return mesCell;
    }

    public void setMesCell(MesCell mesCell) {
        this.mesCell = mesCell;
    }

    public String getJiaochiCode() {
        return jiaochiCode;
    }

    public void setJiaochiCode(String jiaochiCode) {
        this.jiaochiCode = jiaochiCode;
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
