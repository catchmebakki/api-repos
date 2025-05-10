package com.ssi.ms.resea.database.dao;

import lombok.Data;
import org.hibernate.annotations.Formula;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "CMT_PHONE_NBR_CPO")
@Data
public class CmtPhoneNbrCpoDAO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * Primary key representing the claimant's ID.
     */
    @Id
    @Column(name = "CPO_ID", unique = true, nullable = false, length = 15)
    private Long cpoId;

    @Column(name = "CPO_AREA_CODE")
    private String cpoAreaCode;

    @Column(name = "CPO_PHONE_NBR")
    private String cpoPhoneNbr;

    @Column(name = "CPO_PHONE_EXT")
    private String cpoPhoneExt;

    @Column(name = "FK_CMT_ID")
    private Long fkCmtId;

    @Column(name = "CPO_PHONE_NBR_PREF")
    private Short cpoPhoneNbrPref;

    @Formula("'('||CPO_AREA_CODE||') '||substr(CPO_PHONE_NBR, 1, 3) || '-' || substr(CPO_PHONE_NBR, 4)")
    private String phoneNbr;
}
