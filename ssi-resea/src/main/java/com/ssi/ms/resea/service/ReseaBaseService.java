package com.ssi.ms.resea.service;

import com.ssi.ms.common.database.repository.ParameterParRepository;
import com.ssi.ms.common.service.UserService;
import com.ssi.ms.resea.database.mapper.AllowValAlvMapper;
import com.ssi.ms.resea.database.mapper.NonMonIssuesNmiMapper;
import com.ssi.ms.resea.database.mapper.ReseaRescheduleRequestMapper;
import com.ssi.ms.resea.database.mapper.ReseaReturnToWorkMapper;
import com.ssi.ms.resea.database.mapper.StateMapper;
import com.ssi.ms.resea.database.repository.*;
import com.ssi.ms.resea.validator.ReseaRescheduleRequestValidator;
import com.ssi.ms.resea.validator.ReseaReturnToWorkValidator;
import com.ssi.ms.resea.validator.ReseaSwitchMeetingModeValidator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * {@code ReseaBaseService} is a base service component in the application extended to all child
 *  service classes in the application.
 *
 *  Used to inject the necessary objects needed for the child service classes.
 *
 * @author Anand
 */


public class ReseaBaseService {

    @Autowired
    ParameterParRepository parRepo;

    @Autowired
    ReseaIntvwerCalRsicRepository rsicRepo;

    @Autowired
    ClmLofClfRepository clfRepo;

    @Autowired
    CommonRepository commonRepo;

    @Autowired
    AllowValAlvRepository alvRepo;

    @Autowired
    ReseaIssueIdentifiedRsiiRepository rsiiRepo;

    @Autowired
    NonMonIssuesNmiRepository nmiRepo;

    @Autowired
    ReseaCaseActivityRscaRepository rscaRepo;

    @Autowired
    ReseaRescheduleRequestRsrsRepository rsrsRepo;

    @Autowired
    ReseaRescheduleRequestMapper rschRequestMapper;

    @Autowired
    ReseaRescheduleRequestValidator rschValidator;

    @Autowired
    UserService userService;

    @Autowired
    AllowValAlvMapper allowValAlvMapper;

    @Autowired
    ReseaSwitchMeetingModeValidator switchMtgModeValidator;

    @Autowired
    ReseaReturnToWorkMapper rtwMapper;

    @Autowired
    ReseaReturnToWorkRsrwRepository rtwRsrwRepo;

    @Autowired
    ReseaReturnToWorkValidator rtwValidator;

    @Autowired
    StateRepository stateRepo;

    @Autowired
    StateMapper stateMapper;

    @Autowired
    NonMonIssuesNmiMapper nmiMapper;

    @Autowired
    ReseaCaseRscsRepository rscsRepo;

    @Autowired
	ReseaJobReferralRsjrRepository rsjrRepo;
    
    @Autowired
	StaffStfRepository stfRepo;

    @Autowired
    CmtNotesCnoRepository cnoRepo;

}
