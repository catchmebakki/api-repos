package com.ssi.ms.resea.constant;

import com.ssi.ms.platform.dto.PaginationDTO;
import com.ssi.ms.platform.dto.SortByDTO;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.ssi.ms.constant.CommonConstant.DEFAULT;

public interface PaginationAndSortByConstant {
    String ASCENDING = "ASC";

    Supplier<PaginationDTO> GET_DEFAULT_PAGINATION = () -> PaginationDTO.builder()
            .pageNumber(1)
            .pageSize(Integer.MAX_VALUE)
            .needTotalCount(true)
            .build();

    Function<Map<String, String>, SortByDTO> GET_DEFAULT_SORT_BY = (mapping) -> SortByDTO.builder()
            .field(mapping.get(DEFAULT))
            .direction(ASCENDING)
            .build();

    Map<String, String> APPOINTMENT_LOOKUP_SORTBY_FIELDMAPPING = Map.ofEntries(
            Map.entry("default", "rsic.rsicCalEventDt sort_dir, rsic.rsicCalEventStTime sort_dir, rsic.rsicId sort_dir"),
            Map.entry("officeName", "lof.lofName sort_dir, rsic.rsicId sort_dir"),
            Map.entry("caseManagerName", "stf.stfFirstName sort_dir, stf.stfLastName sort_dir, rsic.rsicId sort_dir"),
            Map.entry("eventDateTime", "rsic.rsicCalEventDt sort_dir, rsic.rsicCalEventStTime sort_dir, rsic.rsicId sort_dir"),
            Map.entry("eventType", "rsicEventType.alvShortDecTxt sort_dir, rsic.rsicId sort_dir"),
            Map.entry("eventUsage", "rsicUsage.alvShortDecTxt sort_dir, rsic.rsicId sort_dir"),
            Map.entry("meetingStatus", "rsicMtgStatus.alvShortDecTxt sort_dir, rsic.rsicId sort_dir"),
            Map.entry("claimantName", "cmt.firstName sort_dir, cmt.lastName sort_dir, rsic.rsicId sort_dir"),
            Map.entry("byeDt", "clm.clmBenYrEndDt sort_dir, rsic.rsicId sort_dir"));

    Map<String, String> CASE_LOOKUP_SORTBY_FIELDMAPPING = Map.ofEntries(
            Map.entry("default", "clm.clmBenYrEndDt sort_dir, rscs.rscsId sort_dir"),
            Map.entry("caseId", "rscs.rscsId sort_dir"),
            Map.entry("officeName", "lof.lofName sort_dir, rscs.rscsId sort_dir"),
            Map.entry("caseManagerName", "stf.stfFirstName sort_dir, stf.stfLastName sort_dir, rscs.rscsId sort_dir"),
            Map.entry("stage", "rscs.rscsStageCdALV.alvShortDecTxt sort_dir, rscs.rscsId sort_dir"),
            Map.entry("status", "rscs.rscsStatusCdALV.alvShortDecTxt sort_dir, rscs.rscsId sort_dir"),
            //Map.entry("followUp", "rscs.rscsNxtFolUpTypeCdALV.alvShortDecTxt sort_dir, rscs.rscsNxtFolUpDt sort_dir, rscs.rscsId sort_dir"),
            Map.entry("claimantName", "cmt.firstName sort_dir, cmt.lastName sort_dir, rscs.rscsId sort_dir"),
            Map.entry("byeDt", "clm.clmBenYrEndDt sort_dir, rscs.rscsId sort_dir"));

    Map<String, String> INIT_PENDING_LOOKUP_SORTBY_FIELDMAPPING = Map.ofEntries(
            Map.entry("default", "rsps.rspsOrientationDt sort_dir, rsps.rspsId sort_dir"),
            Map.entry("officeName", "lof.lofName sort_dir, rsps.rspsId sort_dir"),
            Map.entry("caseManagerName", "stf.stfFirstName sort_dir, stf.stfLastName sort_dir, rsps.rspsId sort_dir"),
            Map.entry("stage", "rsps.rspsOrientationDt sort_dir, rsps.rspsId sort_dir"),
            Map.entry("status", "rsps.rspsOrientationDt sort_dir, rsps.rspsId sort_dir"),
            Map.entry("followUp", "rsps.rspsOrientationDt sort_dir, rsps.rspsId sort_dir"),
            Map.entry("claimantName", "cmt.firstName sort_dir, cmt.lastName sort_dir, rsps.rspsId sort_dir"),
            Map.entry("byeDt", "clm.clmBenYrEndDt sort_dir, rsps.rspsId sort_dir"));
    
    Map<String, List<String>> UNAVAILABILITY_LOOKUP_SORTBY_FIELDMAPPING = Map.ofEntries(
            Map.entry("default", List.of("sunrStartDt", "sunrEndDt", "sunrStartTime", "sunrEndTime", "sunrId")),
            Map.entry("staffName", List.of("stfDAO.staffName", "sunrStartDt", "sunrStartTime", "sunrId")),
            Map.entry("type", List.of("sunrTypeInd", "sunrStartDt", "sunrStartTime", "sunrId")),
            Map.entry("period", List.of("sunrStartDt", "sunrEndDt", "sunrStartTime", "sunrEndTime", "sunrId")),
            Map.entry("reason", List.of("sunrReasonTypeCdAlv.alvShortDecTxt", "sunrStartDt", "sunrStartTime", "sunrId")),
            Map.entry("status", List.of("sunrStatusCdAlv.alvShortDecTxt", "sunrStartDt", "sunrStartTime", "sunrId")),
            Map.entry("notes", List.of("sunrNote", "sunrStartDt", "sunrStartTime", "sunrId")));
}
