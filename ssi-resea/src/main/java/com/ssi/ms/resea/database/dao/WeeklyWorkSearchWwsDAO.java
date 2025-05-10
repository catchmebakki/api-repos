package com.ssi.ms.resea.database.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "WEEKLY_WORK_SEARCH_WWS")
@Data
public class WeeklyWorkSearchWwsDAO {

    @Id
    @Column(name = "WWS_ID")
    private Long wwsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WWS_FK_CMT_ID")
    private ClaimantCmtDAO cmtDAO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WWS_FK_CCA_ID")
    private CcApplnCcaDAO ccaDAO;

    @Temporal(TemporalType.DATE)
    @Column(name = "WWS_WEEK_END_DATE")
    private Date wwsWeekEndDate;
}
