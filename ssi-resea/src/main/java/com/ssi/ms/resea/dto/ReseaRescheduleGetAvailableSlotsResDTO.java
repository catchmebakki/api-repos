package com.ssi.ms.resea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.validation.annotation.Validated;


/**
 * {@code ReseaRescheduleGetAvailableSlotsResDTO}  used for transferring data from service to controller layer.
 *
 * <p>
 * This DTO encapsulates the carries the data required for list of available slots to display in UI
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
public class ReseaRescheduleGetAvailableSlotsResDTO {

    /**
     * @for newRsicId
     */
    Long newRschRecNum;

    String rsicCalEventDate;

    String rsicCalEventStartTime;

    String rsicCalEventEndTime;

    String nonComplianceInd;

}
