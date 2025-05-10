package com.ssi.ms.configuration.database.dao;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "CLM_PROG_SPEC_CPS")
@Data
public class ClmProgSpecCpsDAO {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CPS_ID", precision = 15, unique = true, nullable = false)
    private Long cpsId;

    @ManyToOne
    @JoinColumn(name = "CPS_PROGRAM_CD")
    private AllowValAlvDAO cpsProgramCdAlvDAO;

}
