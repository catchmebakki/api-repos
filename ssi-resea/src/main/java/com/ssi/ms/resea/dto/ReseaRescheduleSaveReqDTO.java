package com.ssi.ms.resea.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.LATE_SCHEDULING_REASON_MAX_CHARACTERS;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.NEW_EVENT_ID_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.NON_COMPLAIANCE_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.OLD_EVENT_ID_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.REASON_FOR_RESCHEDULING_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.STAFF_NOTES_MAX_CHARACTERS;
import static com.ssi.ms.resea.constant.ReseaConstants.DATE_FORMAT;

/**
 * {@code ReseaRescheduleSaveReqDTO}  used for transferring data from controller to service layer.
 *
 * <p>
 * This DTO encapsulates the data required for saving the Reschedule details.
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
public class ReseaRescheduleSaveReqDTO {

    /**
     * @for oldRsicId
     */
    @NotNull(message = OLD_EVENT_ID_MANDATORY)
    Long oldEventId;

    /**
     * @for newRsicId
     */
    @NotNull(message = NEW_EVENT_ID_MANDATORY)
    Long newEventId;

    String selectedPrefMtgModeInPerson;

    String selectedPrefMtgModeVirtual;

    @NotNull(message = NON_COMPLAIANCE_MANDATORY)
    String nonComplianceInd;

    @NotNull(message = REASON_FOR_RESCHEDULING_MANDATORY)
    Long reasonForRescheduling;

    @Size(max = 4000, message = LATE_SCHEDULING_REASON_MAX_CHARACTERS)
    String lateSchedulingReason;

    //String tempSuspendedInd;

    @Size(max = 4000, message = STAFF_NOTES_MAX_CHARACTERS)
    String staffNotes;

    List<IssuesDTO> issuesDTOList;

    String entityName;

    String entityCity;

    String entityState;

    String entityTeleNumber;

    String jobTitle;

    String partFullTimeInd;

    @JsonFormat(pattern = DATE_FORMAT)
    LocalDate appointmentDate;

    String appointmentTime;

    boolean includeThisNoteInCNO;
}
