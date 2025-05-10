package com.ssi.ms.resea.database.dao;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the TRANSACTION_TRX database table.
 */
@Entity
@Table(name = "TRANSACTION_TRX")
@Data
public class TransactionTrxDAO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Primary key representing the TRANSACTION_TRX table primary ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "TRX_ID_SEQ_GEN")
    @SequenceGenerator(name = "TRX_ID_SEQ_GEN", sequenceName = "TRX_ID_SEQ", allocationSize = 1)
    @Column(name = "TRX_ID", unique = true, nullable = false, length = 15)
    private Long trxId;

    @Column(name = "TRX_TRAN_CD")
    private Long trxTranCd;

    @Temporal(TemporalType.DATE)
    @Column(name = "TRX_CREATED_TS")
    private Date trxCreatedTs;

    @Column(name = "TRX_01_IFK_CD")
    private Long trx01IfkCd;

    @Column(name = "TRX_01_IFK")
    private Long trx01Ifk;

    @Column(name = "TRX_STATUS_CD")
    private Long trxStatusCd;

    @Column(name = "TRX_CREATED_BY")
    private String trxCreatedBy;

    @Column(name = "TRX_ORG_PROG_NAME")
    private String trxOrgProdName;

    @Temporal(TemporalType.DATE)
    @Column(name = "TRX_STATUS_UPD_TS")
    private Date trxStatusUpdTs;

    @Column(name = "TRX_ADDNL_DATA", length = 254)
    private String trxAddnlData;

}