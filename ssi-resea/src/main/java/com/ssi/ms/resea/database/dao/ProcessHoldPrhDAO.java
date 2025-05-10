package com.ssi.ms.resea.database.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "PROCESS_HOLD_PRH")
@Data
public class ProcessHoldPrhDAO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PRH_ID", unique = true, nullable = false)
    private Long prhId;

    @Column(name = "FK_CMT_ID")
    private Long fkCmtId;

    @Column(name = "PRH_STATUS_CD", length = 1)
    private Long prhStatusCd;

    @Column(name = "PRH_MODULE_CD")
    private Long prhModuleCd;
}
