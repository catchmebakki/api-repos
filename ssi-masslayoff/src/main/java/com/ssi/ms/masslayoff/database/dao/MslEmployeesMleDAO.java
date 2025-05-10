package com.ssi.ms.masslayoff.database.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;


/**
 * The persistent class for the MSL_EMPLOYEES_MLE database table.
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "MSL_EMPLOYEES_MLE")
public class MslEmployeesMleDAO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "MLE_ID_SEQ", sequenceName = "MLE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MLE_ID_SEQ")
    @Column(name = "MLE_ID")
    private Long mleId;

    @Column(name = "MLE_SSN")
    private String mleSsn;

    @Column(name = "MLE_FIRST_NAME")
    private String mleFirstName;

    @Column(name = "MLE_LAST_NAME")
    private String mleLastName;

    @Column(name = "MLE_MIDDLE_INITIAL")
    private String mleMiddleInitial;

    @Temporal(TemporalType.DATE)
    @Column(name = "MLE_EMP_BEGIN_DT")
    private Date mleEmpBeginDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "MLE_EMP_END_DT")
    private Date mleEmpEndDt;

    @Column(name = "MLE_PAY_AMT")
    private Double mlePayAmt;

    @Column(name = "FK_MSL_ID")
    private Long fkMslId;

    @Column(name = "MLE_CLM_IFK")
    private Integer mleClmIfk;

    @Column(name = "MLE_PAY_UNIT_CD")
    private Long mlePayUnitCd;

    @Column(name = "MLE_LAST_UPD_BY")
    private String mleLastUpdBy;

    @Column(name = "MLE_LAST_UPD_TS")
    private Timestamp mleLastUpdTs;

    @Column(name = "MLE_CREATED_BY")
    private String mleCreatedBy;
    @Column(name = "MLE_CREATED_USING")
    private String mleCreatedUsing;
    @Column(name = "MLE_CREATED_TS")
    private Timestamp mleCreatedTs;
    @Column(name = "MLE_LAST_UPD_USING")
    private String mleLastUpdUsing;

    public void copyMlecData(MslEntryCmtMlecDAO mlecDAO){
        setMlePayAmt((double) 0);
        setMlePayUnitCd(0L);
        Optional.ofNullable(mlecDAO.getMlecSsn()).ifPresent(this::setMleSsn);
        Optional.ofNullable(mlecDAO.getMlecFirstName()).ifPresent(this::setMleFirstName);
        Optional.ofNullable(mlecDAO.getMlecLastName()).ifPresent(this::setMleLastName);
        Optional.ofNullable(mlecDAO.getMlecCreatedBy()).ifPresent(this::setMleCreatedBy);
        Optional.ofNullable(mlecDAO.getMlecCreatedUsing()).ifPresent(this::setMleCreatedUsing);
        Optional.ofNullable(mlecDAO.getMlecCreatedTs()).ifPresent(this::setMleCreatedTs);
        Optional.ofNullable(mlecDAO.getMlecLastUpdBy()).ifPresent(this::setMleLastUpdBy);
        Optional.ofNullable(mlecDAO.getMlecLastUpdUsing()).ifPresent(this::setMleLastUpdUsing);
        Optional.ofNullable(mlecDAO.getMlecLastUpdTs()).ifPresent(this::setMleLastUpdTs);
    }
}