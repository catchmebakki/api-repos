package com.ssi.ms.resea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.CHANGE_REASON_TEXT_FOR_SWITCH_MEETING_MODE_MAX_CHARACTERS;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.CURRENT_MEETING_MODE_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.EVENT_ID_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.REASON_FOR_SWITCH_MEETING_MODE_MANDATORY;
import static com.ssi.ms.resea.constant.ErrorMessageConstant.STAFF_NOTES_MAX_CHARACTERS;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
@NoArgsConstructor
public class ReseaSwitchMeetingModeReqDTO {

    /**
     * @for rsicId
     */
    @NotNull(message = EVENT_ID_MANDATORY)
    Long eventId;

    @NotNull(message = CURRENT_MEETING_MODE_MANDATORY)
    String currentMeetingMode;

    @NotNull(message = REASON_FOR_SWITCH_MEETING_MODE_MANDATORY)
    Long reasonForSwitchMeetingMode;

    @Size(max = 1000, message = CHANGE_REASON_TEXT_FOR_SWITCH_MEETING_MODE_MAX_CHARACTERS)
    String meetingModeChgReasonTxt;

    @Size(max = 4000, message = STAFF_NOTES_MAX_CHARACTERS)
    String staffNotes;

    List<IssuesDTO> issuesDTOList;

    boolean includeThisNoteInCNO;

}
