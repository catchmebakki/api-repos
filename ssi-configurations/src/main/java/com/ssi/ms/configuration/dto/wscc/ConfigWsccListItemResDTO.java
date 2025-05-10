package com.ssi.ms.configuration.dto.wscc;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Date;

import static com.ssi.ms.platform.util.DateUtil.dateToLocalDate;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class ConfigWsccListItemResDTO {
    private Long wsccId;
    private String program;
    private Long initialClaim, additionalClaim, incrementFrequency, incrementVal;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate endDate;
    private boolean editFlag, deleteFlag;
    private int childListCount;

    public ConfigWsccListItemResDTO(Long wsccId, String program, Long initialClaim, Long additionalClaim,
                                    Long incrementFrequency, Long incrementVal,
                                    Date startDate, Date endDate, int childListCount) {
        this.wsccId = wsccId;
        this.program = program;
        this.initialClaim = initialClaim;
        this.additionalClaim = additionalClaim;
        this.incrementFrequency = incrementFrequency;
        this.incrementVal = incrementVal;
        this.startDate = dateToLocalDate.apply(startDate);
        this.endDate = dateToLocalDate.apply(endDate);
        this.childListCount = childListCount;
    }

    /*
     * This constructor is used in getting ChildList, this is not an unused constructor, please do not delete.
     * */
    public ConfigWsccListItemResDTO(Long wsccId, String program, Long initialClaim, Long additionalClaim,
                                    Long incrementFrequency, Long incrementVal,
                                    Date startDate, Date endDate) {
        this.wsccId = wsccId;
        this.program = program;
        this.initialClaim = initialClaim;
        this.additionalClaim = additionalClaim;
        this.incrementFrequency = incrementFrequency;
        this.incrementVal = incrementVal;
        this.startDate = dateToLocalDate.apply(startDate);
        this.endDate = dateToLocalDate.apply(endDate);
    }
}
