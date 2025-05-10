package com.ssi.ms.resea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import static com.ssi.ms.resea.constant.ErrorMessageConstant.EVENT_ID_MANDATORY;

/**
 * {@code ReseaRescheduleGetAvailableSlotsReqDTO}  used for transferring data from controller to service layer.
 *
 * <p>
 * This DTO encapsulates the data required for fetching the list of available slots.
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
public class ReseaRescheduleGetAvailableSlotsReqDTO {

    /**
     * @for oldRsicId
     */
    @NotNull(message = EVENT_ID_MANDATORY)
    Long eventId;

    String meetingModeInperson;

    String meetingModeVirtual;

}
