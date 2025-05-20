package com.ssi.ms.collecticase.service;

import com.ssi.ms.collecticase.database.mapper.AllowValAlvMapper;
import com.ssi.ms.collecticase.database.mapper.VwCcaseHeaderMapper;
import com.ssi.ms.collecticase.database.mapper.VwCcaseOpmMapper;
import com.ssi.ms.collecticase.database.mapper.VwCcaseRemedyMapper;
import com.ssi.ms.collecticase.database.mapper.VwCcaseHeaderEntityMapper;
import com.ssi.ms.collecticase.database.mapper.CcaseCraCorrespondenceCrcMapper;
import com.ssi.ms.collecticase.database.mapper.CcaseCmaNoticesCmnMapper;
import com.ssi.ms.collecticase.database.mapper.CcaseActivitiesCmaMapper;
import com.ssi.ms.collecticase.database.mapper.VwCcaseCaseloadMapper;
import com.ssi.ms.collecticase.database.repository.*;
import com.ssi.ms.common.database.repository.ParameterParRepository;
import com.ssi.ms.common.database.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * {@code CollecticaseBaseService} is a base service component in the application extended to all child
 *  service classes in the application.
 *
 *  Used to inject the necessary objects needed for the child service classes.
 *
 * @author Anand
 */
public class CollecticaseBaseService {

    @Autowired
    AllowValAlvRepository allowValAlvRepository;

    @Autowired
    ClaimantCmtRepository claimantCmtRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomLookupRepository customLookupRepository;

    @Autowired
    CcaseActivitiesCmaRepository ccaseActivitiesCmaRepository;

    @Autowired
    CcaseCaseRemedyCmrRepository ccaseCaseRemedyCmrRepository;

    @Autowired
    CcaseCasesCmcRepository ccaseCasesCmcRepository;

    @Autowired
    CcaseCmaNoticesCmnRepository ccaseCmaNoticesCmnRepository;

    @Autowired
    CcaseCmeIndividualCmiRepository ccaseCmeIndividualCmiRepository;

    @Autowired
    CcaseCraCorrespondenceCrcRepository ccaseCraCorrespondenceCrcRepository;

    @Autowired
    CcaseEntityCmeRepository ccaseEntityCmeRepository;

    @Autowired
    CcaseOrganizationCmoRepository ccaseOrganizationCmoRepository;

    @Autowired
    CcaseRemedyActivityCraRepository ccaseRemedyActivityCraRepository;

    @Autowired
    CorrespondenceCorRepository correspondenceCorRepository;

    @Autowired
    EmployerEmpRepository employerEmpRepository;

    @Autowired
    CcaseWageGarnishmentCwgRepository ccaseWageGarnishmentCwgRepository;

    @Autowired
    ClmLofClfRepository clmLofClfRepository;

    @Autowired
    CmtNotesCnoRepository cmtNotesCnoRepository;

    @Autowired
    ParameterParRepository commonRepository;

    @Autowired
    OpmPayPlanOppRepository opmPayPlanOppRepository;

    @Autowired
    StaffStfRepository staffStfRepository;

    @Autowired
    RepaymentRpmRepository repaymentRpmRepository;

    @Autowired
    VwCcaseCaseloadRepository vwCcaseCaseloadRepository;

    @Autowired
    VwCcaseCollectibleDebtsRepository vwCcaseCollectibleDebtsRepository;

    @Autowired
    VwCcaseEntityRepository vwCcaseEntityRepository;

    @Autowired
    AllowValAlvMapper allowValAlvMapper;

    @Autowired
    VwCcaseHeaderMapper vwCcaseHeaderMapper;

    @Autowired
    VwCcaseOpmMapper vwCcaseOpmMapper;

    @Autowired
    VwCcaseRemedyMapper vwCcaseRemedyMapper;

    @Autowired
    VwCcaseHeaderEntityMapper vwCcaseHeaderEntityMapper;

    @Autowired
    CcaseCraCorrespondenceCrcMapper ccaseCraCorrespondenceCrcMapper;

    @Autowired
    CcaseCmaNoticesCmnMapper ccaseCmaNoticesCmnMapper;

    @Autowired
    CcaseActivitiesCmaMapper ccaseActivitiesCmaMapper;

    @Autowired
    VwCcaseCaseloadMapper vwCcaseCaseloadMapper;
}
