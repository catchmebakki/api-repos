package com.ssi.ms.collecticase.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

import static com.ssi.ms.collecticase.constant.CollecticaseConstants.DATE_FORMAT;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.TIME_FORMAT_12_HR_AM;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.ACTIVITY_DATE_MANDATORY;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.ACTIVITY_DATE_NOT_FUTURE;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.ACTIVITY_FOLLOW_UP_DATE_FUTURE;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.ACTIVITY_METHOD_MANDATORY;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.ACTIVITY_SPECIFICS_INVALID;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.ACTIVITY_SPECIFICS_MANDATORY;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.ACTIVITY_TIME_MANDATORY;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.CASE_CHARACTERISTIC_INVALID;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.CASE_CHARACTERISTIC_MANDATORY;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.CLAIMANT_REP_MANDATORY;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.NHUIS_NOTES_LENGTH_EXCEED;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.NOTES_LENGTH_EXCEED;

@With
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Getter
@Setter
public class GeneralActivityDTO {

    @NotNull(message = ACTIVITY_DATE_MANDATORY)
    @JsonFormat(pattern = DATE_FORMAT)
    @PastOrPresent(message = ACTIVITY_DATE_NOT_FUTURE)
    private Date activityDate;

    @NotNull(message = ACTIVITY_TIME_MANDATORY)
    @JsonFormat(pattern = TIME_FORMAT_12_HR_AM)
    private String activityTime;

    @NotNull(message = CASE_CHARACTERISTIC_MANDATORY)
    @Pattern(regexp = "^[A-Za-z0-9-,'. :;/#()&@$+]*$", message = CASE_CHARACTERISTIC_INVALID)
    private String activityCaseCharacteristics;

    @NotNull(message = ACTIVITY_METHOD_MANDATORY)
    private Long activityCommunicationMethod;

    @NotNull(message = ACTIVITY_SPECIFICS_MANDATORY)
    @Pattern(regexp = "^[A-Za-z0-9-,'. :;/#()&@$+]*$", message = ACTIVITY_SPECIFICS_INVALID)
    private String activitySpecifics;

    @Future(message = ACTIVITY_FOLLOW_UP_DATE_FUTURE)
    private Date activityFollowupDate;

    private String activityFollowupShortNote;

    @NotNull(message = CLAIMANT_REP_MANDATORY)
    private Long activityClaimantRepresentative;

    private String activityEntityContact;

    private Long propertyLien;

    @Size(max = 3800, message = NOTES_LENGTH_EXCEED)
    private String activityNotes;

    private String activityNotesIncludeInNHUIS;

    @Size(max = 750, message = NHUIS_NOTES_LENGTH_EXCEED)
    private String activityNHUISNotes;

    private String[] activitySendCorrespondence;

    private String[] activityReSendCorrespondence;

    private String[] activityManualCorrespondence;

    private Long activityTypeCd;

    private Long activityRemedyTypeCd;

    private String activityAdditionalNotes;

    private Long caseId;

    private Long claimantId;

    private Long caseStatusCd;

    private Long casePriorityCd;

    private BigDecimal claimantOpmBalAmount;

    private String callingUser;

    private String usingProgramName;

}