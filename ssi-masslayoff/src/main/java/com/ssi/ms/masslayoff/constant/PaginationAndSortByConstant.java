package com.ssi.ms.masslayoff.constant;

import com.ssi.ms.platform.dto.PaginationDTO;
import com.ssi.ms.platform.dto.SortByDTO;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.ssi.ms.constant.CommonConstant.DEFAULT;
import static com.ssi.ms.constant.CommonConstant.DEFAULT_PAGE_SIZE;
/**
 * @author Praveenraja Paramsivam
 * Interface containing PaginationAndSortByConstant for better code organization and reuse.
 */
public interface PaginationAndSortByConstant {
    String ASCENDING = "ASC";
    Map<String, String> MASSLAYOFF_LOOKUP_SORTBY_FIELDMAPPING = Map.ofEntries(
            Map.entry("default", "mslId"),
            Map.entry("mslId", "MLRL.mlrlMslNum"),
            Map.entry("empUiAcctNbr", "MLRL.mlrlEmpAcNum"),
            Map.entry("empUiAccLoc", "MLRL.mlrlEmpAcLoc"),
            Map.entry("layoffDate", "MLRL.mlrlMslDate"),
            Map.entry("recallDate", "MLRL.mlrlRecallDate"),
            Map.entry("statusCd", "MLRL.mlrlStatusCd"),
            Map.entry("noOfClaimants", "noOfClaimants"),
            Map.entry("effectiveDate", "MLRL.mlrlMslEffDate"),
            Map.entry("deductibleIncomeInd", "MLRL.mlrlDiInd"),
            Map.entry("statusCdValue", "mlrlStatusCdValue"));
    /**
     * Create a default PaginationDTO using a supplier.
     *
     * @return {@link Supplier<PaginationDTO>} A PaginationDTO instance with default values.
     */
    Supplier<PaginationDTO> GET_DEFAULT_PAGINATION = () -> PaginationDTO.builder()
            .pageNumber(1)
            .pageSize(DEFAULT_PAGE_SIZE)
            .needTotalCount(true)
            .build();
    /**
     * Create a SortByDTO using the provided mapping in a default manner.
     *
     * @param mapping {@link Function<Map<String, String>, SortByDTO>} The mapping of fields to sort direction as a Map of
     * field names and sort directions.
     * @return {@link SortByDTO} A SortByDTO instance based on the provided mapping.
     */
    Function<Map<String, String>, SortByDTO> GET_DEFAULT_SORTBY = (mapping) -> SortByDTO.builder()
            .field(mapping.get(DEFAULT))
            .direction(ASCENDING)
            .build();


    Map<String, String> MASSLAYOFF_CLAIMANT_LOOKUP_SORTBY_FIELDMAPPING = Map.ofEntries(
            Map.entry("default", "mlecSsn"),
            Map.entry("firstName", "mlecFirstName"),
            Map.entry("lastName", "mlecLastName"),
            Map.entry("ssnNumber", "mlecSsn"),
            Map.entry("statusCd", "mlecStatusCd"),
            Map.entry("statusCdValue", "mlecStatusCd"),
            Map.entry("sourceCd", "mlecSourceCd"),
            Map.entry("sourceCdValue", "mlecSourceCd"));
}
