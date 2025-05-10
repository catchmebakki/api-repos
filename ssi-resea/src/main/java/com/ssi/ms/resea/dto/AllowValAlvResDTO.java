package com.ssi.ms.resea.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@With
@Builder
@AllArgsConstructor
@Validated
@Getter
public class AllowValAlvResDTO {
    private Long constId, fkAlcId;
    private String alvActiveInd;
    private String alvRelatedCd;
    private String constShortDesc, alvSpShortDescTxt; //options
    private String alvLongDescTxt; //description
    private String alvDecipherCode; //type
    private Long alvDisplayOn;
    private List<Long> alvDisplayOnList;
    private String alvDisplayOnDesc;
    private Long alvSortOrderNbr;
    private String alvComments;
    private String alvCategoryCd;
    private String alvCategoryName;
}
