package com.ssi.ms.resea.database.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "LOCAL_OFFICE_LOF")
@Data
public class LocalOfficeLofDAO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "LOF_ID", precision = 15, unique = true, nullable = false)
    private Long lofId;

    @Column(name = "LOF_NAME", length = 40, nullable = false)
    private String lofName;

    @Column(name = "LOF_DISPLAY_IND", length = 1, nullable = false)
    private String lofDisplayInd;

    @Column(name = "LOF_BU_TYPE_CD")
    private Long lofBuTypeCd;

}
