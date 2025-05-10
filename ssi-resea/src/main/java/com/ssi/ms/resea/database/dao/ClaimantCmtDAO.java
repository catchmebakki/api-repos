package com.ssi.ms.resea.database.dao;

import lombok.Data;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "CLAIMANT_CMT")
@Data
public class ClaimantCmtDAO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * Primary key representing the claimant's ID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CMT_ID_SEQ_GEN")
    @SequenceGenerator(name = "CMT_ID_SEQ_GEN", sequenceName = "CMT_ID_SEQ", allocationSize = 1)
    @Column(name = "CMT_ID", unique = true, nullable = false, length = 15)
    private Long cmtId;

    @Column(name = "CMT_SSN")
    private String ssn;

    @Column(name = "CMT_FIRST_NAME")
    private String firstName;

    @Column(name = "CMT_LAST_NAME")
    private String lastName;

    @Formula("CMT_FIRST_NAME || ' ' || CMT_LAST_NAME")
    private String claimantName;

    @Column(name = "CMT_EMAIL_ADDRESS")
    private String cmtEmailAddress;
}
