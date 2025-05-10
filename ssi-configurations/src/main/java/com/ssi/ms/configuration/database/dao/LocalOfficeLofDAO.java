package com.ssi.ms.configuration.database.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LOCAL_OFFICE_LOF")
@Data
public class LocalOfficeLofDAO {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "LOF_ID", precision = 15, unique = true, nullable = false)
    private Long lofId;
    @Column(name = "LOF_NAME", length = 40, nullable = false)
    private String lofName;
    @Column(name = "LOF_DISPLAY_IND", length = 1, nullable = false)
    private String lofDisplayInd;

}
