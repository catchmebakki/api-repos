package com.ssi.ms.resea.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssi.ms.platform.validator.EnumValidator;
import com.ssi.ms.resea.constant.ReseaConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.*;
import static com.ssi.ms.resea.constant.ReseaConstants.DATE_FORMAT;

/**
 * {@code ReturnToWorkReqDTO}  used for transferring data from controller to service layer.
 *
 * <p>
 * This DTO encapsulates the data required for saving the RTW details.
 * </p>
 *
 * @author Anand
 */
@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@NoArgsConstructor
public class ReturnToWorkReqDTO {

    /**
     * @for RSIC ID
     */
    @NotNull(message = EVENT_ID_MANDATORY)
    Long eventId;

    @NotNull(message = EMP_START_DT_MANDATORY)
    @JsonFormat(pattern = DATE_FORMAT)
    LocalDate employmentStartDt;

    @NotNull(message = COMPANY_NAME_MANDATORY)
    String empName;

    @NotNull(message = JOB_TITLE_MANDATORY)
    String exactJobTitle;

    @NotNull(message = WORK_SCHEDULE_MANDATORY)
    @EnumValidator(enumClazz = ReseaConstants.WORK_SCHEDULE_TYPE.class, message = WORK_SCHEDULE_INVALID)
    String partFullTimeInd;

    @NotNull(message = HOURLY_PAY_RATE)
    BigDecimal hourlyPayRate;

    @NotNull(message = STATE_MANDATORY)
    String empWorkLocState;

    @NotNull(message = CITY_MANDATORY)
    String empWorkLocCity;

    //@EnumValidator(enumClazz = ReseaAlvEnumConstant.RsrwWorkModeAlvCd.class, message = WORK_MODE_VALUE_NOT_VALID)
    @NotNull(message = WORK_MODE_MANDATORY)
    Long workMode;

    @Size(max = 4000, message = STAFF_NOTES_MAX_CHARACTERS)
    String staffNotes;

    String jms890Ind, jmsReferralInd, jmsCloseGoalsInd, jmsCloseIEPInd, jmsCaseNotesInd, jmsResumeOffInd, epChecklistUploadInd;

    String rtwMode;

    Boolean futureRtw = false;

    boolean includeThisNoteInCNO;
}
