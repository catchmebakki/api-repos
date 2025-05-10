package com.ssi.ms.resea.service;

import com.ssi.ms.common.database.dao.ParameterParDao;
import com.ssi.ms.common.database.repository.ParameterParRepository;
import com.ssi.ms.common.service.UserService;
import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import com.ssi.ms.resea.constant.ErrorMessageConstant;
import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.database.dao.ReseaCaseRscsDAO;
import com.ssi.ms.resea.database.dao.ReseaIntvwerCalRsicDAO;
import com.ssi.ms.resea.database.repository.AllowValAlvRepository;
import com.ssi.ms.resea.database.repository.ClaimClmRepository;
import com.ssi.ms.resea.database.repository.CommonRepository;
import com.ssi.ms.resea.database.repository.ReseaCaseActivityRscaRepository;
import com.ssi.ms.resea.database.repository.ReseaCaseRscsRepository;
import com.ssi.ms.resea.database.repository.ReseaIntvwerCalRsicRepository;
import com.ssi.ms.resea.database.repository.ReseaRatRepository;
import com.ssi.ms.resea.dto.AvaliableApptSaveReqDTO;
import com.ssi.ms.resea.util.ReseaErrorEnum;
import com.ssi.ms.resea.util.ReseaUtilFunction;
import com.ssi.ms.resea.validator.ReseaAppointmentValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ssi.ms.platform.util.DateUtil.dateToLocalDate;
import static com.ssi.ms.platform.util.DateUtil.dateToString;
import static com.ssi.ms.platform.util.DateUtil.localDateToDate;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.EVENT_ID_NOT_FOUND;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_NO;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.INDICATOR_YES;
import static com.ssi.ms.resea.constant.ReseaConstants.*;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getRscaStageFromRscsStage;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getRscaStatusFromRscsStatus;
import static com.ssi.ms.resea.util.ReseaUtilFunction.getRscnNoteCategoryFromRscsStage;

@Slf4j
@Service
public class ReseaParameterService {
    @Autowired
    private ParameterParRepository commonParRepository;

    public Map<String, Long> getAppointmentBufferTimes() {
        return new HashMap<>(){{
            put(PAR_CURR_APP_PREP_TIME, commonParRepository.findByParShortName(PAR_CURR_APP_PREP_TIME).getParNumericValue());
            put(PAR_CURR_APP_CLSOUT_TIME, commonParRepository.findByParShortName(PAR_CURR_APP_CLSOUT_TIME).getParNumericValue());
            put(PAR_RESEA_RTW_PAST_DAYS, commonParRepository.findByParShortName(PAR_RESEA_RTW_PAST_DAYS).getParNumericValue());
            put(PAR_RESEA_RTW_FUT_DAYS, commonParRepository.findByParShortName(PAR_RESEA_RTW_FUT_DAYS).getParNumericValue());
            put(PAR_RESEA_CAL_LAPSE_TIME, commonParRepository.findByParShortName(PAR_RESEA_CAL_LAPSE_TIME).getParNumericValue());
        }};
    }
}
