package com.ssi.ms.configuration.dto.wswc;

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
public class ConfigWswcListItemResDTO {
    private Long wswcId;
    private String scenario, reason, autoOverwrite;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate endDate;
    private boolean editFlag, deleteFlag;
    private int childListCount;

    public ConfigWswcListItemResDTO(Long wswcId, String scenario, String reason, String autoOverwrite,
                                   Date startDate, Date endDate, int childListCount) {
        this.wswcId = wswcId;
        this.reason = reason;
        this.scenario = scenario;
        this.autoOverwrite = autoOverwrite;
        this.startDate = dateToLocalDate.apply(startDate);
        this.endDate = dateToLocalDate.apply(endDate);
        this.childListCount = childListCount;
    }

    /*
    * This constructor is used in getting ChildList, this is not an unused constructor, please do not delete.
    * */
    public ConfigWswcListItemResDTO(Long wswcId, String scenario, String reason, String autoOverwrite,
                                    Date startDate, Date endDate) {
        this.wswcId = wswcId;
        this.reason = reason;
        this.scenario = scenario;
        this.autoOverwrite = autoOverwrite;
        this.startDate = dateToLocalDate.apply(startDate);
        this.endDate = dateToLocalDate.apply(endDate);
    }
}
